package CPGTrimmer


import java.nio.file.{Path, Paths}

import better.files._
import io.shiftleft.codepropertygraph.Cpg
import io.shiftleft.codepropertygraph.schema.CpgSchema
import io.shiftleft.passes.{CpgPass, DiffGraph}
import io.shiftleft.semanticcpg.layers.{
  LayerCreator,
  LayerCreatorContext,
  LayerCreatorOptions
}
import io.shiftleft.semanticcpg.language._
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.diff.DiffFormatter
import org.eclipse.jgit.treewalk.CanonicalTreeParser
import org.eclipse.jgit.util.io.DisabledOutputStream

import scala.jdk.CollectionConverters._


object CPGTrimmer {
	val overlayName = "CPGTrimmer"
	val description = "Removes Empty BLOCK nodes from CPG to reduce graph size, improving processing speed and graph clarity"
	def defaultOpts = CPGTrimmerOpts(".")
}

case class CPGTrimmerOpts(var pathToSource: String) extends LayerCreatorOptions

class CPGTrimmer(options: CPGTrimmerOpts) extends LayerCreator {
	override val overlayName = CPGTrimmer.overlayName
	override val description = CPGTrimmer.description

	override def create(context: LayerCreatorContext) {
		val cpg = context.cpg
		val removalPass = new EmptyRemovalPass(cpg)

		val newGraphs = removalPass.run()
		newGraphs.foreach(cpg.apply)
	}
}

class EmptyRemovalPass(cpg: Cpg) extends CpgPass(cpg) {

	override def run(): Iterator[DiffGraph] = {
		val graphBuilder = DiffGraph.newBuilder

		val emptyNodes = cpg.block.where(_.astChildren.isEmpty).l

		emptyNodes.foreach{ node =>
			graphBuilder.removeNode(node)
		}

		Iterator(graphBuilder.build())
	}
}
