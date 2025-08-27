package io.joern.CPGTrimmer

import java.util.Iterator
import io.shiftleft.codepropertygraph.generated.PropertyNames
import io.shiftleft.codepropertygraph.generated.GeneratedNodeStarterExt
import io.shiftleft.codepropertygraph.generated.Cpg
import io.shiftleft.semanticcpg.language
import io.shiftleft.codepropertygraph.generated.nodes.*
import org.slf4j.LoggerFactory

class CPGTrimLogger(emptyBlocks: List[StoredNode]) {
  private val logger = LoggerFactory.getLogger(getClass[CPGTrimLogger])

  def logNodes(cpg: Cpg): Unit = {
    emptyBlocks.foreach(node => logNodeDetails(node))
  }

  def logNodeDetails(node: StoredNode): Unit = {
    val cfgChildren = node.out("CFG")
    val astChildren = node._astOut.toList.foreach(child => )
    val text =
      s"""
         |Node ID = ${node.id()}
         |Node Children: ${node._astOut.toList.mkString(", ")}
         |""".stripMargin
  }
  logger.
}
