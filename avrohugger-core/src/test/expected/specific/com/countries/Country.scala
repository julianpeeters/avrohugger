/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package com.countries

import scala.annotation.switch

import com.europe.City

import com.america.City

final case class Country(var key: Option[String] = None, var paris: Option[com.europe.City] = None, var london: Option[com.europe.City] = None, var newYork: Option[com.america.City] = None) extends org.apache.avro.specific.SpecificRecordBase {
  def this() = this(None, None, None, None)
  def get(field$: Int): AnyRef = {
    (field$: @switch) match {
      case 0 => {
        key match {
          case Some(x) => x
          case None => null
        }
      }.asInstanceOf[AnyRef]
      case 1 => {
        paris match {
          case Some(x) => x
          case None => null
        }
      }.asInstanceOf[AnyRef]
      case 2 => {
        london match {
          case Some(x) => x
          case None => null
        }
      }.asInstanceOf[AnyRef]
      case 3 => {
        newYork match {
          case Some(x) => x
          case None => null
        }
      }.asInstanceOf[AnyRef]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }
  def put(field$: Int, value: Any): Unit = {
    (field$: @switch) match {
      case 0 => this.key = {
        value match {
          case null => None
          case _ => Some(value.toString)
        }
      }.asInstanceOf[Option[String]]
      case 1 => this.paris = {
        value match {
          case null => None
          case _ => Some(value)
        }
      }.asInstanceOf[Option[com.europe.City]]
      case 2 => this.london = {
        value match {
          case null => None
          case _ => Some(value)
        }
      }.asInstanceOf[Option[com.europe.City]]
      case 3 => this.newYork = {
        value match {
          case null => None
          case _ => Some(value)
        }
      }.asInstanceOf[Option[com.america.City]]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  def getSchema: org.apache.avro.Schema = Country.SCHEMA$
}

object Country {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Country\",\"namespace\":\"com.countries\",\"fields\":[{\"name\":\"key\",\"type\":[\"null\",\"string\"],\"default\":null},{\"name\":\"paris\",\"type\":[\"null\",{\"type\":\"record\",\"name\":\"City\",\"namespace\":\"com.europe\",\"fields\":[{\"name\":\"key\",\"type\":\"string\",\"default\":\"Paris\"}]}],\"default\":null},{\"name\":\"london\",\"type\":[\"null\",\"com.europe.City\"],\"default\":null},{\"name\":\"newYork\",\"type\":[\"null\",{\"type\":\"record\",\"name\":\"City\",\"namespace\":\"com.america\",\"fields\":[{\"name\":\"name\",\"type\":\"string\",\"default\":\"Paris\"}]}],\"default\":null}]}")
}