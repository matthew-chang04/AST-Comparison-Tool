package io.TypeRecover

import io.shiftleft.codepropertygraph.Cpg
import io.shiftleft.codepropertygraoh.generated.nodes_
import io.shiftleft.codepropertygraph.generated.{NodeTypes, Properties, EdgeTypes}
import io.shiftleft.passes.{CpgPass, DiffGraphBuilder}
import io.shiftleft.semanticcpg.language._

import scala.util.{Try, Success, Failure}
import scala.util.matching.Regex

class CPPTypeRecoverPass(cpg: Cpg) extens CpgPass(cpg) {
	
	override def run(dstGraph: DiffGraphBuilder): Unit = {
		revisitUnkownAny(dstGraph)
	}

	def revisitUnkownAny(dstGraph: DiffGraphBuilder): Unit = {
		cpg.local.typeFullName("ANY|UNKNOWN|").foreach { local => 
			inferFromUsage(dstGraph, local) orElse
			inferFromAssignment(
		}
	}
}
