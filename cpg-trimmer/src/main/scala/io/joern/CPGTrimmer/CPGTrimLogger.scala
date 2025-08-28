package io.joern.CPGTrimmer

import io.shiftleft.codepropertygraph.generated.nodes._
import org.slf4j.LoggerFactory
import flatgraph.GNode

class CPGTrimLogger(emptyBlocks: List[GNode]) {
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
    val parent: String = if (node. .toList.size > 1) {
      "Multiple Parent Nodes"
    } else {
      s"""
         |Parent ID = ${node._astIn.toList.head.id()}
         |  Parent Type = ${node._astIn.toList.head.label}
         |""".stripMargin
    }

    val children: List[String] = node._astOut.toList.map(node => {
      s"""
         |Child ID = ${node.id()}
         |  Child Type = ${node.label}
         |""".stripMargin
    })

    val text =
      s"""
         |Node ID = ${node.id()}
         |  Node Type = ${node.label}
         |  Parent = ${parent}
         |  Children = ${children.mkString(", ")}
         |  Code = ${node.propertiesMap.getOrDefault("code")}
         |""".stripMargin
    text
  }
}
