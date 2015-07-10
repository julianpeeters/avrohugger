package avrohugger
package tool


/** Command-line driver.*/
object Main {

  def main(args: Array[String]) = {
    val r = new Runner()
    val _ = r.run(args)
  }

}