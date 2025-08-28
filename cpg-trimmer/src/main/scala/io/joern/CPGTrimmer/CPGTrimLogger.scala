package io.joern.CPGTrimmer

import io.shiftleft.semanticcpg.language.*
import io.shiftleft.codepropertygraph.generated.{Cpg, Properties}
import io.shiftleft.codepropertygraph.generated.nodes
import org.slf4j.LoggerFactory
import flatgraph.GNode
import io.shiftleft.codepropertygraph.generated.nodes.StoredNode

class CPGTrimLogger(emptyBlocks: List[GNode], cpg: Cpg) {
  private val logger = LoggerFactory.getLogger(getClass[CPGTrimLogger])

  def logNodes(): Unit = {
    emptyBlocks.foreach(node => logNodeDetails(node))
  }

  private def logNodeDetails(node: GNode): Unit = {
    val text = getNodeString(node)
    logger.info(text)
  }

  private def getNodeString(node: GNode): String = {

    // TODO: FIND GNODE equivalent of _astIn
    val nodeContext: StoredNode = cpg.id(node.id()).head

    val parentsList: List[String] = nodeContext._astIn.toList.map(snode =>
      s"""
         |Parent ID = ${snode.id()}
         |  Parent Type = ${snode.label()}
         |""".stripMargin
    )

    val childrenList: List[String] = nodeContext._astOut.toList.map(node => {
      s"""
         |Child ID = ${node.id()}
         |  Child Type = ${node.label}
         |""".stripMargin
    })

    val text =
      s"""
         |Node ID = ${node.id()}
         |  Node Type = ${node.label()}
         |  Parent = ${parentsList.mkString(",\n\t")}
         |  Children = ${childrenList.mkString(",\n\t ")}
         |  Code = ${nodeContext.property(Properties.Code)}
         |""".stripMargin
    text
  }
}
