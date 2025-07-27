package CPGTrimmer


import java.nio.file.{Path, Paths}

import better.files._
import io.shiftleft.codepropertygraph.Cpg
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
			
	}
}
