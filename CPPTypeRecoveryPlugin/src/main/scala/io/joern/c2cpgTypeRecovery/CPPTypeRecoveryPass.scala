package io.joern.c2cpgTypeRecovery

import io.shiftleft.codepropertygraph.Cpg
import overflowdb.BatchedUpdate.DiffGraphBuilder
import io.shiftleft.passes.CpgPass
import io.shiftleft.semanticcpg.language.*
import io.shiftleft.codepropertygraph.generated.nodes.*
import io.shiftleft.codepropertygraph.generated.PropertyNames


class CPPTypeRecoveryPass(cpg: Cpg) extends CpgPass(cpg) {

  override def run(dstGraph: DiffGraphBuilder): Unit = {

  }

  def fixAmbiguousCalls(dstGraph: DiffGraphBuilder): Unit = {
    val calls = cpg.call.toList
    val operatorCalls = calls.filter(call => call.methodFullName.startsWith("<operator"))
    val otherAmbiguous = calls.filter(call=> !call.methodFullName.startsWith("<operator") && call.typeFullName == "ANY")
    
    fixOperatorCalls(operatorCalls, dstGraph)
    fixOtherAmbiguousCalls(otherAmbiguous, dstGraph)
  }

  def fixOperatorCalls(operatorCalls: List[Call], dstGraph: DiffGraphBuilder) :Unit = {
    val assignmentCalls = operatorCalls.filter(call => call.methodFullName.endsWith("assignment"))
    val pointerDerefCalls = operatorCalls.filter(call => call.methodFullName.endsWith("pointer"))
    
    // TO IMPLEMENT:
    /*
      indirectIndexAccess
      Addition, Multiplication, Subtraction, etc
      Cast
      
     */
  }

  def fixAssignmentCalls(assignmentCalls: List[Call], dstGraph: DiffGraphBuilder) = {
    assignmentCalls.foreach(call => {
      val identifiers: Iterator[Identifier] = call.astChildren.isIdentifier.cast[Identifier]
      val assignee: Identifier = identifiers.argumentIndex(1).head

      dstGraph.setNodeProperty(call, PropertyNames.TYPE_FULL_NAME, assignee.typeFullName)
    })
  }

  def fixOtherAmbiguousCalls(otherAmbiguous: List[Call], dstGraph: DiffGraphBuilder): Unit = {

  }

  def cleanPointerTypes(dstGraph: DiffGraphBuilder): Unit = {

  }
}
