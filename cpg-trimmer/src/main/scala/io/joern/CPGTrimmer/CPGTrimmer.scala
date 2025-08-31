package io.joern.CPGTrimmer

import io.shiftleft.semanticcpg.language.*
import io.shiftleft.semanticcpg.layers.{LayerCreator, LayerCreatorContext, LayerCreatorOptions}
import io.shiftleft.codepropertygraph.generated.Properties
import io.shiftleft.codepropertygraph.generated.NodeTypes
import java.io.{FileWriter, PrintWriter, File}
import scala.util.Using

object CPGTrimmer {
  val overlayName = "cpg-trimmer"
  val description = "Logs context for unnecessary empty nodes in a Joern CPG"

  def defaultOpts = CPGTrimmerOpts(".")
}

case class CPGTrimmerOpts(var outputDir: String) extends LayerCreatorOptions {}

class CPGTrimmer(options: CPGTrimmerOpts) extends LayerCreator {
  override val overlayName: String = CPGTrimmer.overlayName
  override val description: String = CPGTrimmer.description

  override def create(context: LayerCreatorContext): Unit = {
    val cpg = context.cpg
    val blocks = cpg.graph.nodes(NodeTypes.BLOCK).toList
    val emptyBlocks = blocks.filter(node => node.property(Properties.Code).equals("<empty>"))

    if (emptyBlocks.nonEmpty) {
      val logDir = new File(options.outputDir)
      logDir.mkdirs()

      val logFilename: String = options.outputDir + "/" + overlayName + ".log"
      println(s"Writing empty block node details to ${logFilename}")

      Using(new PrintWriter(new FileWriter(logFilename))) { writer =>
        writer.println(s"Empty Block List - ${emptyBlocks.size} empty blocks")
        writer.println("=" * 50)

        val logger = new CPGTrimLogger(emptyBlocks, cpg, Some(writer))
        logger.logNodes()

        writer.println("=" * 50)
      }.recover {
        case ex => println(s"Failed to write log: ${ex.getMessage}")
      }

      println(s"Details written to: ${logFilename}")

    } else {
      println("No empty nodes found.")
    }
  }
}