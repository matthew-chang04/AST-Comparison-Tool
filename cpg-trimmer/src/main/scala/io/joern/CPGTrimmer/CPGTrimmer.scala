package io.joern.CPGTrimmer

import io.shiftleft.codepropertygraph.Cpg
import io.shiftleft.codepropertygraph.generated.PropertyNames
import io.shiftleft.codepropertygraph.generated.nodes.{Block, NewBlock}
import io.shiftleft.passes.CpgPass
import io.shiftleft.codepropertygraph.generated.DiffGraphBuilder
import io.shiftleft.semanticcpg.language.*
import io.shiftleft.semanticcpg.layers.{LayerCreator, LayerCreatorContext}

object CPGTrimmerExt {
  val overlayName = "CPG Trimmer"
  val description = "Logs context for unnecessary empty nodes in a Joern CPG"
}

class CPGTrimmerExt extends LayerCreator {
  override val overlayName = CPGTrimmerExt.overlayName
  override val description = CPGTrimmerExt.description

  override def create(context: LayerCreatorContext): Unit = {
    val cpg = context.cpg
    val logger = new CPGTrimLogger()
  }
}
class CPGTrimmer(cpg: Cpg) extends CpgPass(cpg){

  def run(dstBuilder: DiffGraphBuilder) : Unit = {

  }
  def removeEmptyBlocks(dstBuilder: DiffGraphBuilder): Unit = {
    val emptyNodes: List[overflowdb.Node] = cpg.graph.nodes("BLOCK").toList
  }
