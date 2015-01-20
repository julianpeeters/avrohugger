import avrohugger.tool.Main
import org.apache.avro.tool.Tool

import org.specs2._
import mutable._
import specification._
import scala.collection.JavaConversions._


class MainSpec extends mutable.Specification {
  "tool descriptions" should {
    "fit in 80 characters" in {
      val m: Main = new Main()
      val descResults = m.tools.values().toSeq.map(t => {
        if (m.maxLen + 2 + t.getShortDescription().length() > 80) true
        else false  
      }) 
      //make sure there is no tool that didn't pass the desc. length test  
      descResults.exists(x => x == true) === false
    }
  }
}
