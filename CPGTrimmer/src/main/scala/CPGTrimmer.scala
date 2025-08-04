package cpgtrimmer

import java.nio.file.{Path, Paths}
import io.shiftleft.codepropertygraph.Cpg
import io.shiftleft.codepropertygraph._
import io.shiftleft.passes.{CpgPass, DiffGraph}
import io.shiftleft.semanticcpg.language._
import io.shiftleft.semanticcpg.layers.{LayerCreator, LayerCreatorContext, LayerCreatorOptions}
import scala.jdk.CollectionConverters._


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

	override def run(): Iterator[DiffGraph] = {
		val graphBuilder: DiffGraph.Builder = DiffGraph.newBuilder

		val emptyNodes: Unit = cpg.method.ast.isBlock.code("<empty>").foreach { node =>
			val edges = node.inE()
			edges.forEachRemaining(edge => graphBuilder.removeEdge(edge))
			graphBuilder.removeNode(node)
		}
		Iterator(graphBuilder.build())
	}
}
