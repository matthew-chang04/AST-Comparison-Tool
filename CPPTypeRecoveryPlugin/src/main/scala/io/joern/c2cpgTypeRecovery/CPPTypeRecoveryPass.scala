package io.joern.c2cpgTypeRecovery

import io.shiftleft.codepropertygraph.Cpg
import overflowdb.BatchedUpdate.DiffGraphBuilder
import io.shiftleft.passes.CpgPass
import io.shiftleft.semanticcpg.language.*


object CPPTypeRecoveryPass {

}

class CPPTypeRecoveryPass(cpg: Cpg) extends CpgPass(cpg) {

  override def run(dstGraph: DiffGraphBuilder): Unit = {

  }

  def fixAmbiguousCalls(dstGraph: DiffGraphBuilder): Unit = {
    val calls = cpg.call.toList
    val operatorCalls = calls.filter(call => call.methodFullName.startsWith("<operator"))
    val otherAmbiguous = calls.filter(call=> call.typeFullName == "ANY")
    
    fixOperatorCalls(dstGraph)
    fixOtherAmbiguousCalls(dstGraph)
  }

  def fixOperatorCalls(dstGraph: DiffGraphBuilder) :Unit = {

  }

  def fixOtherAmbiguousCalls(dstGraph: DiffGraphBuilder): Unit = {

  }

  def cleanPointerTypes(dstGraph: DiffGraphBuilder): Unit = {

  }

  def
}
