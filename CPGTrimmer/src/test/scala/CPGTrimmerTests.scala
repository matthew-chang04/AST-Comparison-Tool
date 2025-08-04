import io.shiftleft.codepropertygraph.Cpg
import io.shiftleft.semanticcpg.testing.MockCpg
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import java.nio.file.{Files, Paths}


// TODO: keep working on basic test implementation (get the test.c file liked (or find an easier way))

class CPGTrimmerPassTests extends AnyWordSpec with Matchers {
  
  "CPGTrimmerPass" should {
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
