package avrohugger
package tool


/** Command-line driver.*/
object Main extends App{

  override def main(args: Array[String]) = {
    val r = new Runner()
    r.run(args)
  }

}