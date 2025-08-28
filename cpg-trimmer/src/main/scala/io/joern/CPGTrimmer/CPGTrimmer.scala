package io.joern.CPGTrimmer

import io.shiftleft.semanticcpg.language.*
import io.shiftleft.semanticcpg.layers.{LayerCreator, LayerCreatorContext, LayerCreatorOptions}
import io.shiftleft.codepropertygraph.generated.{Cpg, Properties}
import io.shiftleft.codepropertygraph.generated.NodeTypes


object CPGTrimmer {
  val overlayName = "CPG Trimmer"
  val description = "Logs context for unnecessary empty nodes in a Joern CPG"

  def defaultOpts = CPGTrimmerOpts(".")
}

case class CPGTrimmerOpts(var pathToRepo: String)
  extends LayerCreatorOptions {}

class CPGTrimmer(options: CPGTrimmerOpts) extends LayerCreator {
  override val overlayName: String = CPGTrimmer.overlayName
  override val description: String = CPGTrimmer.description

  override def create(context: LayerCreatorContext): Unit = {
    val cpg = context.cpg
    val blocks = cpg.graph.nodes(NodeTypes.BLOCK).toList
    val emptyBlocks = blocks.filter(node => node.property(Properties.Code).equals("<empty>"))
    val logger = new CPGTrimLogger(emptyBlocks)
    logger.logNodes()
  }
}