package io.RemoveEmpty

import io.shiftleft.codepropertygraph.Cpg
import io.shiftleft.codepropertygraoh.generated.nodes_
import io.shiftleft.codepropertygraph.generated.{NodeTypes, Properties, EdgeTypes}
import io.shiftleft.passes.{CpgPass, DiffGraphBuilder}
import io.shiftleft.semanticcpg.language._

class RemoveEmptyNodePass(cpg: Cpg) {
	
	
	cpg.block.where(_.astChildren.isEmpty)

}
