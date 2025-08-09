package io.joern.CPGTrimmer

import io.shiftleft.codepropertygraph.Cpg
import io.shiftleft.codepropertygraph.generated.PropertyNames
import io.shiftleft.codepropertygraph.generated.nodes.{Block, NewBlock}
import io.shiftleft.passes.CpgPass
import io.shiftleft.codepropertygraph.generated.DiffGraphBuilder
import io.shiftleft.semanticcpg.language.*

class CPGTrimmer(cpg: Cpg) extends CpgPass(cpg){

  def run(dstBuilder: DiffGraphBuilder) : Unit = {

  }
  def removeEmptyBlocks(dstBuilder: DiffGraphBuilder): Unit = {
    val emptyNodes: List[overflowdb.Node] = cpg.graph.nodes("BLOCK").toList
  }
