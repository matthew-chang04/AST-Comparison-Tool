package io.joern.c2cpgTypeRecovery

import io.shiftleft.codepropertygraph.Cpg
import overflowdb.BatchedUpdate.DiffGraphBuilder
import io.shiftleft.passes.CpgPass
import io.shiftleft.semanticcpg.language.*
import io.shiftleft.codepropertygraph.generated.nodes.*
import io.shiftleft.codepropertygraph.generated.PropertyNames


class CPPTypeRecoveryPass(cpg: Cpg) extends CpgPass(cpg) {

  override def run(dstGraph: DiffGraphBuilder): Unit = {
    // Fixes calls of type ANY/void (inaccurate types)
    fixAmbiguousCalls(dstGraph)

    // removes nodes where the program has misidentified type tokens (ie: char, int, etc) for identifiers
    removeTypeTokens(dstGraph)

  }

  private def fixAmbiguousCalls(dstGraph: DiffGraphBuilder): Unit = {
    val calls = cpg.call.toList
    val operatorCalls = calls.filter(call => call.methodFullName.startsWith("<operator"))
    fixOperatorCalls(operatorCalls, dstGraph)
  }

  private def fixOperatorCalls(operatorCalls: List[Call], dstGraph: DiffGraphBuilder) :Unit = {
    val assignmentCalls: List[Call] = operatorCalls.filter(call => call.methodFullName.endsWith("assignment") && call.typeFullName.equals("ANY"))
    val pointerDerefCalls: List[Call] = operatorCalls.filter(call => call.typeFullName.equals("void"))
    val indexAccessCalls: List[Call] = operatorCalls.filter(call => call.methodFullName.endsWith("indirectIndexAccess"))
    // TO IMPLEMENT:
    /*
      Addition, Multiplication, Subtraction, etc.
      Cast
      
     */

    fixAssignmentCalls(assignmentCalls, dstGraph)
    fixIndexAccess(indexAccessCalls, dstGraph)
  }

  private def fixAssignmentCalls(assignmentCalls: List[Call], dstGraph: DiffGraphBuilder): Unit = {
    assignmentCalls.foreach(call => {
      val identifiers: Iterator[Identifier] = call.astChildren.isIdentifier.cast[Identifier]
      val assignee: Identifier = identifiers.argumentIndex(1).head

      dstGraph.setNodeProperty(call, PropertyNames.TYPE_FULL_NAME, assignee.typeFullName)
    })
  }
  // in progress
  private def fixMathOps(dstGraph: DiffGraphBuilder): Unit = {
    /* needs to handle numeric and non-numeric separately
    
    1. parse argument types (either c/c++ type or user defined)
    2. numeric types: built in, so priority is already defined, we just have to implement a function
       to compare the types of the two args
    3. non-numeric: search cpg for a method def w/name +, -, /, *, =. if one exists, use it's return type
       if not, we must leave it, as we don't know
    
    */
    val mathOps: List[Call] = cpg.call.filter(call => call.methodFullName.endsWith("addition") || call.methodFullName.endsWith("subtraction") || call.methodFullName.endsWith("multiplication") || call.methodFullName.endsWith("division")).toList

    val simpleStmt: List[Call] = mathOps.filter(call => call.astChildren.isIdentifier.toList.size == call.astChildren.toList.size)
    val compoundStmt: List[Call] = mathOps.filter(call => call.astChildren.isIdentifier.toList.size != call.astChildren.toList.size)

    // TODO: implement the pass that separates numeric from non-numerics and implement the fixNumericOps function below
    simpleStmt.foreach(call => {
      call.astChildren.isIdentifier.toList.foreach(id => {
        val idType: CNumType = parseCNumType(id.typeFullName)


      })
    })
  }

  private def fixNumericOps(): Unit = {

  }

  private def compoundStmtPass(): Unit = {

  }
  
  // done
  private def fixIndexAccess(calls: List[Call], dstGraph: DiffGraphBuilder): Unit = {
    calls.foreach(call => {
      val arrayObj: Identifier = call.astChildren.isIdentifier.head // First identifier will always be the array indexed
      val ret = arrayObj.typeFullName.dropRight(1) // Drop * from ptr type
      dstGraph.setNodeProperty(call, PropertyNames.TYPE_FULL_NAME, ret)
    })
  }
  // done
  private def removeTypeTokens(dstGraph: DiffGraphBuilder): Unit = {
    val typeToks: List[Identifier] = cpg.identifier.toList.filter(identifier => identifier.code == identifier.typeFullName)

    typeToks.foreach(identifier => {
      dstGraph.removeNode(identifier)
    })
  }
}
