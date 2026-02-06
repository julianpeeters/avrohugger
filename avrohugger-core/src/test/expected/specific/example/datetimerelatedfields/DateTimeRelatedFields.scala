/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package example.datetimerelatedfields

import scala.annotation.switch

/**
 * Record with the fields that relate to date and time
 * @param field01 
 * @param field02 
 * @param field03 
 * @param field04 
 * @param field05 
 * @param field06 
 * @param field07 
 */
final case class DateTimeRelatedFields(var field01: Int, var field02: Long, var field03: Long, var field04: Long, var field05: Long, var field06: Int, var field07: Int) extends org.apache.avro.specific.SpecificRecordBase {
  def this() = this(0, 0L, 0L, 0L, 0L, 0, 0)
  def get(field$: Int): AnyRef = {
    (field$: @switch) match {
      case 0 => {
        field01
      }.asInstanceOf[AnyRef]
      case 1 => {
        field02
      }.asInstanceOf[AnyRef]
      case 2 => {
        field03
      }.asInstanceOf[AnyRef]
      case 3 => {
        field04
      }.asInstanceOf[AnyRef]
      case 4 => {
        field05
      }.asInstanceOf[AnyRef]
      case 5 => {
        field06
      }.asInstanceOf[AnyRef]
      case 6 => {
        field07
      }.asInstanceOf[AnyRef]
      case _ => throw new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }
  def put(field$: Int, value: Any): Unit = {
    (field$: @switch) match {
      case 0 => this.field01 = {
        value
      }.asInstanceOf[Int]
      case 1 => this.field02 = {
        value
      }.asInstanceOf[Long]
      case 2 => this.field03 = {
        value
      }.asInstanceOf[Long]
      case 3 => this.field04 = {
        value
      }.asInstanceOf[Long]
      case 4 => this.field05 = {
        value
      }.asInstanceOf[Long]
      case 5 => this.field06 = {
        value
      }.asInstanceOf[Int]
      case 6 => this.field07 = {
        value
      }.asInstanceOf[Int]
      case _ => throw new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  def getSchema: org.apache.avro.Schema = example.datetimerelatedfields.DateTimeRelatedFields.SCHEMA$
}

object DateTimeRelatedFields {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"DateTimeRelatedFields\",\"namespace\":\"example.datetimerelatedfields\",\"doc\":\"Record with the fields that relate to date and time\",\"fields\":[{\"name\":\"field01\",\"type\":\"int\",\"logicalType\":\"date\"},{\"name\":\"field02\",\"type\":\"long\",\"logicalType\":\"timestamp-millis\"},{\"name\":\"field03\",\"type\":\"long\",\"logicalType\":\"timestamp-micros\"},{\"name\":\"field04\",\"type\":\"long\",\"logicalType\":\"local-timestamp-micros\"},{\"name\":\"field05\",\"type\":\"long\",\"logicalType\":\"local-timestamp-millis\"},{\"name\":\"field06\",\"type\":\"int\",\"logicalType\":\"time-millis\"},{\"name\":\"field07\",\"type\":\"int\",\"logicalType\":\"time-micros\"}],\"messages\":{}}")
}