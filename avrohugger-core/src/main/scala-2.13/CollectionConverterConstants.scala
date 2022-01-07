package avrohugger
package internal

object CollectionConverterConstants {
  val asScalaIteratorConverter =
    "scala.jdk.CollectionConverters.IteratorHasAsScala"
  val mapAsScalaMapConverter =
    "scala.jdk.CollectionConverters.MapHasAsScala"
  val bufferAsJavaListConverter =
    "scala.jdk.CollectionConverters.BufferHasAsJava"
}
