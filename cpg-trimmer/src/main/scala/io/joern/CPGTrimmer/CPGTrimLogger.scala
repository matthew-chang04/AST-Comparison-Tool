package io.joern.CPGTrimmer

import io.shiftleft.semanticcpg.language.*
import io.shiftleft.codepropertygraph.generated.{Cpg, Properties}
import io.shiftleft.codepropertygraph.generated.nodes
import flatgraph.GNode
import io.shiftleft.codepropertygraph.generated.nodes.StoredNode
import java.io.PrintWriter


class CPGTrimLogger(emptyBlocks: List[GNode], cpg: Cpg, fileWriter: Option[PrintWriter]) {

  def logNodes(): Unit = {
    emptyBlocks.foreach(node => logNodeDetails(node))
  }

  private def logNodeDetails(node: GNode): Unit = {
    val text = getNodeString(node)

    fileWriter.foreach { writer =>
      writer.println(text)
      writer.println("-" * 50)
      writer.flush()
    }

  }

  private def getNodeString(node: GNode): String = {

    val nodeContext: StoredNode = cpg.id(node.id()).head

    val parentsList: List[String] = nodeContext._astIn.toList.map(snode =>
      s"""
         |{
         |  Parent ID: ${snode.id()}
         |  Parent Type: "${snode.label()}"
         |  Parent Name: "${snode.property(Properties.FullName)}"
         |  Parent Code: "${snode.property(Properties.Code)}"
         |}""".stripMargin
    )

    val childrenList: List[String] = nodeContext._astOut.toList.map(node => {
      s"""
         |{
         |  Child ID: ${node.id()},
         |  Child Type = "${node.label()}",
         |}""".stripMargin
    })

    val text =
      s"""
         |Node ID: ${node.id()}
         |Node Type: "${node.label()}",
         |Parents: {${parentsList.mkString(",\n\t")}\n},
         |Children: {${childrenList.mkString(",\n\t ")}\n},
         |""".stripMargin

    text
  }
}
