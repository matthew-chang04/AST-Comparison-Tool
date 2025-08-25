package io.joern.CPGTrimmer

import java.util.Iterator
import io.shiftleft.codepropertygraph.Cpg
import io.shiftleft.codepropertygraph.generated.nodes
import org.slf4j.LoggerFactory

class CPGTrimLogger {
  private val logger = LoggerFactory.getLogger(getClass[CPGTrimLogger])

  def logProblems(cpg: Cpg): Unit = {
    val blocks: java.util.Iterator[overflowdb.Node] = cpg.graph.nodes("BLOCK")
    blocks.forEachRemaining()
  }
}
