import io.shiftleft.codepropertygraph.Cpg
import io.shiftleft.passes.NewDiffGraphBuilder
import io.shiftleft.passes.CpgPass
import io.shiftleft.semanticcpg.language._
import io.shiftleft.passes.layers.LayerCreator
import io.shiftleft.passes.layers.LayerCreatorContext
import io.shiftleft.passes.layers.LayerCreatorOptions


object CPGTrimmer {
	val overlayName = "CPGTrimmer"
	val description = "Removes Empty BLOCK nodes from CPG to reduce graph size, improving processing speed and graph clarity"
	def defaultOpts = CPGTrimmerOpts(".")
}

case class CPGTrimmerOpts(var pathToSource: String) extends LayerCreatorOptions

class CPGTrimmer(options: CPGTrimmerOpts) extends LayerCreator {
	override val overlayName: String = CPGTrimmer.overlayName
	override val description: String = CPGTrimmer.description

	override def create(context: LayerCreatorContext, storeUndoInfo: Boolean = true): Unit = {
		val cpg = context.cpg
		val serializedGraph = initSerializedCpg(context.outputDir, "EmptyNodePass")

		new EmptyRemovalPass(cpg).createApplySerializeAndStore(serializedGraph, storeUndoInfo)
		serializedGraph.close()
	}
}

class EmptyRemovalPass(cpg: Cpg) extends CpgPass(cpg) {

	override def run(dstGraph: DiffGraphBuilder): Unit = {
		val emptyNodes: Unit = cpg.method.ast.isBlock.code("<empty>").foreach { node =>
			val edges = node.inE()
			edges.forEachRemaining(edge => dstGraph.removeEdge(edge))
			dstGraph.removeNode(node)
		}
	}
}
