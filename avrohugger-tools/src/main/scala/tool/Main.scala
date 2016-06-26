package avrohugger
package tool


/** Command-line driver.*/
object Main {

  def main(args: Array[String]) = {
    val r = new Runner(System.in, System.out, System.err)
    val exitCode = r.run(args)
    System.exit(exitCode)
  }

}
