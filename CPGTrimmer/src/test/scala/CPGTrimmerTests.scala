import io.shiftleft.codepropertygraph.Cpg
import io.joern.x2cpg.X2Cpg
import io.joern.x2cpg.passes.base
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import java.nio.file.{Files, Paths}
import io.joern.x2cpg.



class CPGTrimmerPassTests extends AnyWordSpec with Matchers {
  
  "CPGTrimmerPass" should {
    val cpg = code(
      """
        |
        |""".stripMargin)
   "Remove all empty BLOCK Nodes" in {
	   val dir = "resources/test.c"
	   val 
    }
    
    "handle empty CPG gracefully" in {
      // Test implementation
    }
    
    "process multiple files correctly" in {
      // Test implementation  
    }
  }
}
