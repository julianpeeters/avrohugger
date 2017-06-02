package avrohugger.filesorter

import spray.json.DefaultJsonProtocol._
import spray.json._

/**
  * Code adapted from https://github.com/ch4mpy/sbt-avro/blob/master/src/main/scala/com/c4soft/sbtavro/SbtAvro.scala
  * by Jerome Wascongne
  */
object ReferredTypeFinder {

  object Keys {
    val Fields = "fields"
    val Type = "type"
    val Items = "items"
    val Values = "values"
    val Array = "array"
    val Map = "map"
    val Enum = "enum"
    val Record = "record"
    val Name = "name"
  }

  def findReferredTypes(json: JsValue): List[String] = {

    def matchComplexType(fields: Map[String,JsValue]): List[String] = {
      val typeOfRef = fields(Keys.Type)
      typeOfRef match {
        case JsString(Keys.Array) => findReferredTypes(fields(Keys.Items))
        case JsString(Keys.Enum) => List(fields(Keys.Name).convertTo[String])
        case JsString(Keys.Record) => findReferredTypes(fields(Keys.Fields))
        case JsString(Keys.Map) => findReferredTypes(fields(Keys.Values))
        case nestedDefinition => findReferredTypes(nestedDefinition)
      }
    }

    json match {
      case str: JsString => List(str.value)
      case union: JsArray => union.elements.toList.flatMap(findReferredTypes(_))
      case complex: JsObject => matchComplexType(complex.fields)
      case _ => List.empty
    }

  }

}
