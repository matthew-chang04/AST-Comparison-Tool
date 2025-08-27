package io.joern.c2cpgTypeRecovery

import scala.collection.mutable
import io.shiftleft.codepropertygraph.generated.Cpg
import io.shiftleft.passes.CpgPass
import io.shiftleft.semanticcpg.language.*
import io.shiftleft.codepropertygraph.generated.nodes.*
import io.shiftleft.codepropertygraph.generated.PropertyNames


class CPPTypeRecoveryPass(cpg: Cpg) extends CpgPass(cpg) {

  override def run(dstGraph: DiffGraphBuilder): Unit = {
    fixAmbiguousCalls(dstGraph)    // Fixes calls of type ANY/void (inaccurate types)
    removeTypeTokens(dstGraph)    // removes nodes where the program has misidentified type tokens (ie: char, int, etc.) for identifiers

  }

  private def fixAmbiguousCalls(dstGraph: DiffGraphBuilder): Unit = {
    val calls: List[Call] = cpg.call.toList
    val operatorCalls: List[Call] = calls.filter(call => call.methodFullName.startsWith("<operator"))
    fixOperatorCalls(operatorCalls, dstGraph)
    fixMathOps(dstGraph)
  }

  private def fixOperatorCalls(operatorCalls: List[Call], dstGraph: DiffGraphBuilder) :Unit = {
    val assignmentCalls: List[Call] = operatorCalls.filter(call => call.methodFullName.endsWith("assignment") && call.typeFullName.equals("ANY"))
    // val pointerDerefCalls: List[Call] = operatorCalls.filter(call => call.typeFullName.equals("void"))
    val indexAccessCalls: List[Call] = operatorCalls.filter(call => call.methodFullName.endsWith("indirectIndexAccess"))

    fixAssignmentCalls(assignmentCalls, dstGraph)
    fixIndexAccess(indexAccessCalls, dstGraph)
  }

  private def fixAssignmentCalls(assignmentCalls: List[Call], dstGraph: DiffGraphBuilder): Unit = {
    assignmentCalls.foreach(call => {
      val identifiers: Iterator[Identifier] = call.astChildren.isIdentifier.cast[Identifier]
      val assignee: Identifier = identifiers.argumentIndex(1).head

      dstGraph.setNodeProperty(call, PropertyNames.TypeFullName, assignee.typeFullName)
    })
  }
  // done
  private def fixIndexAccess(calls: List[Call], dstGraph: DiffGraphBuilder): Unit = {
    calls.foreach(call => {
      val arrayObj: Identifier = call.astChildren.isIdentifier.head // First identifier will always be the array indexed
      val ret = arrayObj.typeFullName.dropRight(1) // Drop * from ptr type
      dstGraph.setNodeProperty(call, PropertyNames.TypeFullName, ret)
    })
  }

  // IN testing
  private def fixMathOps(dstGraph: DiffGraphBuilder): Unit = {
    val mathOps: List[Call] = cpg.call.filter(call => call.methodFullName.endsWith("addition") || call.methodFullName.endsWith("subtraction") || call.methodFullName.endsWith("multiplication") || call.methodFullName.endsWith("division")).toList
    val memo: mutable.HashMap[Call, String] = mutable.HashMap[Call, String]()
    mathOps.foreach(call => fixMathExprType(call, dstGraph, memo))
  }

  private def fixMathExprType(mathOp: Call, dstGraph: DiffGraphBuilder, memo: mutable.HashMap[Call, String]): String = {
    if (memo.contains(mathOp)) memo(mathOp)

    if (mathOp.astChildren.isIdentifier.toList.size == mathOp.astChildren.toList.size) {
      val operandTypes: List[CNumType] = mathOp.astChildren.isIdentifier.toList.map(id => parseCNumType(id.typeFullName))

      if (operandTypes(0).kind == NonNumeric || operandTypes(1).kind == NonNumeric) {
        // FOR NOW: we just parse the value of the first operand
        dstGraph.setNodeProperty(mathOp, PropertyNames.TypeFullName, operandTypes.head.typeName)
        memo.put(mathOp, operandTypes.head.typeName)
        operandTypes.head.typeName
      } else {
        val typeName: String = numTypePriority(operandTypes(0), operandTypes(1))
        dstGraph.setNodeProperty(mathOp, PropertyNames.TypeFullName, typeName)
        memo.put(mathOp, typeName)
        typeName
      }
    } else {
      val childTypes: List[String] = mathOp.astChildren.isCall.toList.map(call => fixMathExprType(call, dstGraph, memo))
      val childCNumTypes: List[CNumType] = childTypes.map(child => parseCNumType(child))
      val highestPriority: CNumType = childCNumTypes.maxBy(cnum => cnum.priority)
      highestPriority.typeName
    }
  }

  // done
  private def removeTypeTokens(dstGraph: DiffGraphBuilder): Unit = {
    val typeToks: List[Identifier] = cpg.identifier.toList.filter(identifier => identifier.code == identifier.typeFullName)

    typeToks.foreach(identifier => {
      dstGraph.removeNode(identifier)
    })
  }
}
