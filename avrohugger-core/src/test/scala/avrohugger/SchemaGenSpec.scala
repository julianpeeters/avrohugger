package avrohugger
package test

import java.io.File

import avrohugger._
import avrohugger.format.SpecificRecord
import org.specs2._

class SchemaGenSpec extends mutable.Specification {
  // Scala 2.10 can't parse package declarations, so rather than than maintain 
  // a separate branch, these tests (that pass for 2.11) get commented out until 
  // 2.10 is phased out.
  skipAll
  "Schema generator" should {
    

    "Expand a standard case class in the default package to implement SpecificRecord" in {
      val schemaString = 
        """/**
          | * Auto-Generated Schema
          | * @param name Auto-Generated Field
          | */
          |case class Person(name: String)""".stripMargin
      val gen = new Generator(SpecificRecord)
      val List(source) = gen.stringToStrings(schemaString)

       
      source ===
        """/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
          |import scala.annotation.switch
          |
          |/**
          | * Auto-Generated Schema
          | * @param name Auto-Generated Field
          | */
          |case class Person(var name: String) extends org.apache.avro.specific.SpecificRecordBase {
          |  def this() = this("")
          |  def get(field$: Int): AnyRef = {
          |    (field$: @switch) match {
          |      case pos if pos == 0 => {
          |        name
          |      }.asInstanceOf[AnyRef]
          |      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
          |    }
          |  }
          |  def put(field$: Int, value: Any): Unit = {
          |    (field$: @switch) match {
          |      case pos if pos == 0 => this.name = {
          |        value.toString
          |      }.asInstanceOf[String]
          |      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
          |    }
          |    ()
          |  }
          |  def getSchema: org.apache.avro.Schema = Person.SCHEMA$
          |}
          |
          |object Person {
          |  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Person\",\"doc\":\"Auto-Generated Schema\",\"fields\":[{\"name\":\"name\",\"type\":\"string\",\"doc\":\"Auto-Generated Field\"}]}")
          |}""".stripMargin.trim
    }

    
    "Expand an empty case class in the default package to implement SpecificRecord" in {
      val schemaString = 
        """/** Auto-Generated Schema */
          |case class Reset()""".stripMargin
      val gen = new Generator(SpecificRecord)
      val List(source) = gen.stringToStrings(schemaString)

       
      source ===
        """/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
          |import scala.annotation.switch
          |
          |/** Auto-Generated Schema */
          |case class Reset() extends org.apache.avro.specific.SpecificRecordBase {
          |  def get(field$: Int): AnyRef = {
          |    (field$: @switch) match {
          |      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
          |    }
          |  }
          |  def put(field$: Int, value: Any): Unit = {
          |    (field$: @switch) match {
          |      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
          |    }
          |    ()
          |  }
          |  def getSchema: org.apache.avro.Schema = Reset.SCHEMA$
          |}
          |
          |object Reset {
          |  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Reset\",\"doc\":\"Auto-Generated Schema\",\"fields\":[]}")
          |}""".stripMargin.trim
    }



    "Expand an empty case class in the default package to implement SpecificRecord" in {
      val schemaString = 
        """/** Auto-Generated Schema */
          |case class Raise()""".stripMargin
      val gen = new Generator(SpecificRecord)
      val List(source) = gen.stringToStrings(schemaString)

       
      source ===
        """/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
          |import scala.annotation.switch
          |
          |/** Auto-Generated Schema */
          |case class Raise() extends org.apache.avro.specific.SpecificRecordBase {
          |  def get(field$: Int): AnyRef = {
          |    (field$: @switch) match {
          |      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
          |    }
          |  }
          |  def put(field$: Int, value: Any): Unit = {
          |    (field$: @switch) match {
          |      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
          |    }
          |    ()
          |  }
          |  def getSchema: org.apache.avro.Schema = Raise.SCHEMA$
          |}
          |
          |object Raise {
          |  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Raise\",\"doc\":\"Auto-Generated Schema\",\"fields\":[]}")
          |}""".stripMargin.trim
    }
  
    "Expand a standard case class with immutable fields to implement SpecificRecord" in {
      val schemaString = 
        """package example
          |
          |/**
          | * Auto-Generated Schema
          | * @param name Auto-Generated Field
          | */
          |case class Person(name: String)""".stripMargin
      val gen = new Generator(SpecificRecord)
      val List(source) = gen.stringToStrings(schemaString)
       
      source ===
        """/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
          |package example
          |
          |import scala.annotation.switch
          |
          |/**
          | * Auto-Generated Schema
          | * @param name Auto-Generated Field
          | */
          |case class Person(var name: String) extends org.apache.avro.specific.SpecificRecordBase {
          |  def this() = this("")
          |  def get(field$: Int): AnyRef = {
          |    (field$: @switch) match {
          |      case pos if pos == 0 => {
          |        name
          |      }.asInstanceOf[AnyRef]
          |      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
          |    }
          |  }
          |  def put(field$: Int, value: Any): Unit = {
          |    (field$: @switch) match {
          |      case pos if pos == 0 => this.name = {
          |        value.toString
          |      }.asInstanceOf[String]
          |      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
          |    }
          |    ()
          |  }
          |  def getSchema: org.apache.avro.Schema = Person.SCHEMA$
          |}
          |
          |object Person {
          |  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Person\",\"namespace\":\"example\",\"doc\":\"Auto-Generated Schema\",\"fields\":[{\"name\":\"name\",\"type\":\"string\",\"doc\":\"Auto-Generated Field\"}]}")
          |}""".stripMargin.trim
    }


    "Expand a two packages to implement SpecificRecord" in {
      val schemaString = 
        """package example
          |
          |/**
          | * Auto-Generated Schema
          | * @param name Auto-Generated Field
          | */
          |case class Person(name: String)
          |
          |package test
          |
          |/**
          | * Auto-Generated Schema
          | * @param name Auto-Generated Field
          | */
          |case class Vehicle(name: String)""".stripMargin
      val gen = new Generator(SpecificRecord)
      val List(sourceVehicle, sourcePerson) = gen.stringToStrings(schemaString)
       
      sourcePerson ===
        """/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
          |package example
          |
          |import scala.annotation.switch
          |
          |/**
          | * Auto-Generated Schema
          | * @param name Auto-Generated Field
          | */
          |case class Person(var name: String) extends org.apache.avro.specific.SpecificRecordBase {
          |  def this() = this("")
          |  def get(field$: Int): AnyRef = {
          |    (field$: @switch) match {
          |      case pos if pos == 0 => {
          |        name
          |      }.asInstanceOf[AnyRef]
          |      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
          |    }
          |  }
          |  def put(field$: Int, value: Any): Unit = {
          |    (field$: @switch) match {
          |      case pos if pos == 0 => this.name = {
          |        value.toString
          |      }.asInstanceOf[String]
          |      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
          |    }
          |    ()
          |  }
          |  def getSchema: org.apache.avro.Schema = Person.SCHEMA$
          |}
          |
          |object Person {
          |  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Person\",\"namespace\":\"example\",\"doc\":\"Auto-Generated Schema\",\"fields\":[{\"name\":\"name\",\"type\":\"string\",\"doc\":\"Auto-Generated Field\"}]}")
          |}""".stripMargin.trim

      sourceVehicle ===
        """/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
          |package test
          |
          |import scala.annotation.switch
          |
          |/**
          | * Auto-Generated Schema
          | * @param name Auto-Generated Field
          | */
          |case class Vehicle(var name: String) extends org.apache.avro.specific.SpecificRecordBase {
          |  def this() = this("")
          |  def get(field$: Int): AnyRef = {
          |    (field$: @switch) match {
          |      case pos if pos == 0 => {
          |        name
          |      }.asInstanceOf[AnyRef]
          |      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
          |    }
          |  }
          |  def put(field$: Int, value: Any): Unit = {
          |    (field$: @switch) match {
          |      case pos if pos == 0 => this.name = {
          |        value.toString
          |      }.asInstanceOf[String]
          |      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
          |    }
          |    ()
          |  }
          |  def getSchema: org.apache.avro.Schema = Vehicle.SCHEMA$
          |}
          |
          |object Vehicle {
          |  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Vehicle\",\"namespace\":\"test\",\"doc\":\"Auto-Generated Schema\",\"fields\":[{\"name\":\"name\",\"type\":\"string\",\"doc\":\"Auto-Generated Field\"}]}")
          |}""".stripMargin.trim
          
    }



    "Expand a two dependent packages to implement SpecificRecord" in {
      val schemaString = 
        """package example
          |
          |/**
          | * Auto-Generated Schema
          | * @param vehicle Auto-Generated Field
          | */
          |case class Person(vehicle: test.major.Vehicle)
          |
          |package test.major
          |
          |/**
          | * Auto-Generated Schema
          | * @param name Auto-Generated Field
          | */
          |case class Vehicle(name: String)""".stripMargin
      val gen = new Generator(SpecificRecord)
      val List(sourceVehicle, sourcePerson) = gen.stringToStrings(schemaString)

       
      sourcePerson ===
       """/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
         |package example
         |
         |import scala.annotation.switch
         |
         |import test.major.Vehicle
         |
         |/**
         | * Auto-Generated Schema
         | * @param vehicle Auto-Generated Field
         | */
         |case class Person(var vehicle: Vehicle) extends org.apache.avro.specific.SpecificRecordBase {
         |  def this() = this(new Vehicle)
         |  def get(field$: Int): AnyRef = {
         |    (field$: @switch) match {
         |      case pos if pos == 0 => {
         |        vehicle
         |      }.asInstanceOf[AnyRef]
         |      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
         |    }
         |  }
         |  def put(field$: Int, value: Any): Unit = {
         |    (field$: @switch) match {
         |      case pos if pos == 0 => this.vehicle = {
         |        value
         |      }.asInstanceOf[Vehicle]
         |      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
         |    }
         |    ()
         |  }
         |  def getSchema: org.apache.avro.Schema = Person.SCHEMA$
         |}
         |
         |object Person {
         |  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Person\",\"namespace\":\"example\",\"doc\":\"Auto-Generated Schema\",\"fields\":[{\"name\":\"vehicle\",\"type\":{\"type\":\"record\",\"name\":\"Vehicle\",\"namespace\":\"test.major\",\"doc\":\"Auto-Generated Schema\",\"fields\":[{\"name\":\"name\",\"type\":\"string\",\"doc\":\"Auto-Generated Field\"}]},\"doc\":\"Auto-Generated Field\"}]}")
         |}""".stripMargin

      sourceVehicle ===
        """/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
          |package test.major
          |
          |import scala.annotation.switch
          |
          |/**
          | * Auto-Generated Schema
          | * @param name Auto-Generated Field
          | */
          |case class Vehicle(var name: String) extends org.apache.avro.specific.SpecificRecordBase {
          |  def this() = this("")
          |  def get(field$: Int): AnyRef = {
          |    (field$: @switch) match {
          |      case pos if pos == 0 => {
          |        name
          |      }.asInstanceOf[AnyRef]
          |      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
          |    }
          |  }
          |  def put(field$: Int, value: Any): Unit = {
          |    (field$: @switch) match {
          |      case pos if pos == 0 => this.name = {
          |        value.toString
          |      }.asInstanceOf[String]
          |      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
          |    }
          |    ()
          |  }
          |  def getSchema: org.apache.avro.Schema = Vehicle.SCHEMA$
          |}
          |
          |object Vehicle {
          |  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Vehicle\",\"namespace\":\"test.major\",\"doc\":\"Auto-Generated Schema\",\"fields\":[{\"name\":\"name\",\"type\":\"string\",\"doc\":\"Auto-Generated Field\"}]}")
          |}""".stripMargin.trim
          
    }




   
      //TODO: handle imports. Eight possible package depths, eight possible 
      //      import depths per import. Perhaps not feasible unless it becomes easier
      //      to match arbitrary packages and arbitrary imports. 
/*
      // imports are not supported as of Scala 2.11.x: not legal package stat: import test.major.Vehicle
    "Expand a two dependent packages with imports to implement SpecificRecord" in {
      val schemaString = 
        """package example
          |
          |import test.major.Vehicle
          |
          |/**
          | * Auto-Generated Schema
          | * @param name Auto-Generated Field
          | */
          |case class Person(vehicle: Vehicle)
          |
          |package test.major
          |
          |/**
          | * Auto-Generated Schema
          | * @param vehicle Auto-Generated Field
          | */
          |case class Vehicle(name: String)""".stripMargin
      val gen = new Generator(SpecificRecord)
      val List(sourceVehicle, sourcePerson) = gen.stringToStrings(schemaString)

      sourcePerson ===
        """/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
          |package example
          |
          |import scala.annotation.switch
          |
          |import test.major.Vehicle
          |
          |/**
          | * Auto-Generated Schema
          | * @param vehicle Auto-Generated Field
          | */
          |case class Person(var vehicle: Vehicle) extends org.apache.avro.specific.SpecificRecordBase {
          |  def this() = this(new Vehicle)
          |  def get(field$: Int): AnyRef = {
          |    (field$: @switch) match {
          |      case pos if pos == 0 => {
          |        vehicle
          |      }.asInstanceOf[AnyRef]
          |      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
          |    }
          |  }
          |  def put(field$: Int, value: Any): Unit = {
          |    (field$: @switch) match {
          |      case pos if pos == 0 => this.vehicle = {
          |        value
          |      }.asInstanceOf[Vehicle]
          |      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
          |    }
          |    ()
          |  }
          |  def getSchema: org.apache.avro.Schema = Person.SCHEMA$
          |}
          |
          |object Person {
          |  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Person\",\"namespace\":\"example\",\"doc\":\"Auto-Generated Schema\",\"fields\":[{\"name\":\"vehicle\",\"type\":{\"type\":\"record\",\"name\":\"Vehicle\",\"namespace\":\"test.major\",\"doc\":\"Auto-Generated Schema\",\"fields\":[{\"name\":\"name\",\"type\":\"string\",\"doc\":\"Auto-Generated Field\"}]},\"doc\":\"Auto-Generated Field\"}]}")
          |}""".stripMargin

      sourceVehicle ===
        """/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
          |package test.major
          |
          |import scala.annotation.switch
          |
          |/**
          | * Auto-Generated Schema
          | * @param name Auto-Generated Field
          | */
          |case class Vehicle(var name: String) extends org.apache.avro.specific.SpecificRecordBase {
          |  def this() = this("")
          |  def get(field$: Int): AnyRef = {
          |    (field$: @switch) match {
          |      case pos if pos == 0 => {
          |        name
          |      }.asInstanceOf[AnyRef]
          |      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
          |    }
          |  }
          |  def put(field$: Int, value: Any): Unit = {
          |    (field$: @switch) match {
          |      case pos if pos == 0 => this.name = {
          |        value.toString
          |      }.asInstanceOf[String]
          |      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
          |    }
          |    ()
          |  }
          |  def getSchema: org.apache.avro.Schema = Vehicle.SCHEMA$
          |}
          |
          |object Vehicle {
          |  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Vehicle\",\"namespace\":\"test.major\",\"doc\":\"Auto-Generated Schema\",\"fields\":[{\"name\":\"name\",\"type\":\"string\",\"doc\":\"Auto-Generated Field\"}]}")
          |}""".stripMargin.trim
          
    }
    */

    "Expand a standard case class in more than one package to implement SpecificRecord" in {
      val schemaString = 
        """
          |package test
          |package testsub
          |
          |/**
          | * Auto-Generated Schema
          | * @param name Auto-Generated Field
          | */
          |case class Person(name: String)""".stripMargin
      val gen = new Generator(SpecificRecord)
      val List(source) = gen.stringToStrings(schemaString)

       
      source ===
        """/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
          |package test.testsub
          |
          |import scala.annotation.switch
          |
          |/**
          | * Auto-Generated Schema
          | * @param name Auto-Generated Field
          | */
          |case class Person(var name: String) extends org.apache.avro.specific.SpecificRecordBase {
          |  def this() = this("")
          |  def get(field$: Int): AnyRef = {
          |    (field$: @switch) match {
          |      case pos if pos == 0 => {
          |        name
          |      }.asInstanceOf[AnyRef]
          |      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
          |    }
          |  }
          |  def put(field$: Int, value: Any): Unit = {
          |    (field$: @switch) match {
          |      case pos if pos == 0 => this.name = {
          |        value.toString
          |      }.asInstanceOf[String]
          |      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
          |    }
          |    ()
          |  }
          |  def getSchema: org.apache.avro.Schema = Person.SCHEMA$
          |}
          |
          |object Person {
          |  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Person\",\"namespace\":\"test.testsub\",\"doc\":\"Auto-Generated Schema\",\"fields\":[{\"name\":\"name\",\"type\":\"string\",\"doc\":\"Auto-Generated Field\"}]}")
          |}""".stripMargin.trim
    }

    "Expand a standard case class in a package with dots to implement SpecificRecord" in {
      val schemaString = 
        """
          |package test.example.testsub
          |
          |/**
          | * Auto-Generated Schema
          | * @param name Auto-Generated Field
          | */
          |case class Person(name: String)""".stripMargin
      val gen = new Generator(SpecificRecord)
      val List(source) = gen.stringToStrings(schemaString)

       
      source ===
        """/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
          |package test.example.testsub
          |
          |import scala.annotation.switch
          |
          |/**
          | * Auto-Generated Schema
          | * @param name Auto-Generated Field
          | */
          |case class Person(var name: String) extends org.apache.avro.specific.SpecificRecordBase {
          |  def this() = this("")
          |  def get(field$: Int): AnyRef = {
          |    (field$: @switch) match {
          |      case pos if pos == 0 => {
          |        name
          |      }.asInstanceOf[AnyRef]
          |      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
          |    }
          |  }
          |  def put(field$: Int, value: Any): Unit = {
          |    (field$: @switch) match {
          |      case pos if pos == 0 => this.name = {
          |        value.toString
          |      }.asInstanceOf[String]
          |      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
          |    }
          |    ()
          |  }
          |  def getSchema: org.apache.avro.Schema = Person.SCHEMA$
          |}
          |
          |object Person {
          |  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Person\",\"namespace\":\"test.example.testsub\",\"doc\":\"Auto-Generated Schema\",\"fields\":[{\"name\":\"name\",\"type\":\"string\",\"doc\":\"Auto-Generated Field\"}]}")
          |}""".stripMargin.trim
    }

    "Expand two case classes in the default package to implement SpecificRecord" in {
      val schemaString = 
        """case class Person(name: String)
          |case class Pet(name: String)""".stripMargin
      val gen = new Generator(SpecificRecord)
      val List(source2, source1) = gen.stringToStrings(schemaString)

       
      source1 ===
        """/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
          |import scala.annotation.switch
          |
          |case class Person(var name: String) extends org.apache.avro.specific.SpecificRecordBase {
          |  def this() = this("")
          |  def get(field$: Int): AnyRef = {
          |    (field$: @switch) match {
          |      case pos if pos == 0 => {
          |        name
          |      }.asInstanceOf[AnyRef]
          |      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
          |    }
          |  }
          |  def put(field$: Int, value: Any): Unit = {
          |    (field$: @switch) match {
          |      case pos if pos == 0 => this.name = {
          |        value.toString
          |      }.asInstanceOf[String]
          |      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
          |    }
          |    ()
          |  }
          |  def getSchema: org.apache.avro.Schema = Person.SCHEMA$
          |}
          |
          |object Person {
          |  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Person\",\"fields\":[{\"name\":\"name\",\"type\":\"string\"}]}")
          |}""".stripMargin.trim

      source2 ===
        """/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
          |import scala.annotation.switch
          |
          |case class Pet(var name: String) extends org.apache.avro.specific.SpecificRecordBase {
          |  def this() = this("")
          |  def get(field$: Int): AnyRef = {
          |    (field$: @switch) match {
          |      case pos if pos == 0 => {
          |        name
          |      }.asInstanceOf[AnyRef]
          |      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
          |    }
          |  }
          |  def put(field$: Int, value: Any): Unit = {
          |    (field$: @switch) match {
          |      case pos if pos == 0 => this.name = {
          |        value.toString
          |      }.asInstanceOf[String]
          |      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
          |    }
          |    ()
          |  }
          |  def getSchema: org.apache.avro.Schema = Pet.SCHEMA$
          |}
          |
          |object Pet {
          |  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Pet\",\"fields\":[{\"name\":\"name\",\"type\":\"string\"}]}")
          |}""".stripMargin.trim
    }

    "Expand two case classes with immutable fields to implement SpecificRecord" in {
      val schemaString = 
        """
          |package example
          |
          |/**
          | * Auto-Generated Schema
          | * @param name Auto-Generated Field
          | */
          |case class Person(name: String)
          |/**
          | * Auto-Generated Schema
          | * @param name Auto-Generated Field
          | */
          |case class Pet(name: String)""".stripMargin
      val gen = new Generator(SpecificRecord)
      val List(source2, source1) = gen.stringToStrings(schemaString)

       
      source1 ===
        """/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
          |package example
          |
          |import scala.annotation.switch
          |
          |/**
          | * Auto-Generated Schema
          | * @param name Auto-Generated Field
          | */
          |case class Person(var name: String) extends org.apache.avro.specific.SpecificRecordBase {
          |  def this() = this("")
          |  def get(field$: Int): AnyRef = {
          |    (field$: @switch) match {
          |      case pos if pos == 0 => {
          |        name
          |      }.asInstanceOf[AnyRef]
          |      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
          |    }
          |  }
          |  def put(field$: Int, value: Any): Unit = {
          |    (field$: @switch) match {
          |      case pos if pos == 0 => this.name = {
          |        value.toString
          |      }.asInstanceOf[String]
          |      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
          |    }
          |    ()
          |  }
          |  def getSchema: org.apache.avro.Schema = Person.SCHEMA$
          |}
          |
          |object Person {
          |  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Person\",\"namespace\":\"example\",\"doc\":\"Auto-Generated Schema\",\"fields\":[{\"name\":\"name\",\"type\":\"string\",\"doc\":\"Auto-Generated Field\"}]}")
          |}""".stripMargin.trim

      source2 ===
        """/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
          |package example
          |
          |import scala.annotation.switch
          |
          |/**
          | * Auto-Generated Schema
          | * @param name Auto-Generated Field
          | */
          |case class Pet(var name: String) extends org.apache.avro.specific.SpecificRecordBase {
          |  def this() = this("")
          |  def get(field$: Int): AnyRef = {
          |    (field$: @switch) match {
          |      case pos if pos == 0 => {
          |        name
          |      }.asInstanceOf[AnyRef]
          |      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
          |    }
          |  }
          |  def put(field$: Int, value: Any): Unit = {
          |    (field$: @switch) match {
          |      case pos if pos == 0 => this.name = {
          |        value.toString
          |      }.asInstanceOf[String]
          |      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
          |    }
          |    ()
          |  }
          |  def getSchema: org.apache.avro.Schema = Pet.SCHEMA$
          |}
          |
          |object Pet {
          |  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Pet\",\"namespace\":\"example\",\"doc\":\"Auto-Generated Schema\",\"fields\":[{\"name\":\"name\",\"type\":\"string\",\"doc\":\"Auto-Generated Field\"}]}")
          |}""".stripMargin.trim
    }


    "Expand two dependent case classes in the default package to implement SpecificRecord" in {
      val schemaString = 
        """case class Person(pet: Pet)
          |case class Pet(name: String)""".stripMargin
      val gen = new Generator(SpecificRecord)
      val List(source2, source1) = gen.stringToStrings(schemaString)

       
      source1 ===
        """/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
          |import scala.annotation.switch
          |
          |case class Person(var pet: Pet) extends org.apache.avro.specific.SpecificRecordBase {
          |  def this() = this(new Pet)
          |  def get(field$: Int): AnyRef = {
          |    (field$: @switch) match {
          |      case pos if pos == 0 => {
          |        pet
          |      }.asInstanceOf[AnyRef]
          |      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
          |    }
          |  }
          |  def put(field$: Int, value: Any): Unit = {
          |    (field$: @switch) match {
          |      case pos if pos == 0 => this.pet = {
          |        value
          |      }.asInstanceOf[Pet]
          |      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
          |    }
          |    ()
          |  }
          |  def getSchema: org.apache.avro.Schema = Person.SCHEMA$
          |}
          |
          |object Person {
          |  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Person\",\"fields\":[{\"name\":\"pet\",\"type\":{\"type\":\"record\",\"name\":\"Pet\",\"fields\":[{\"name\":\"name\",\"type\":\"string\"}]}}]}")
          |}""".stripMargin.trim

      source2 ===
        """/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
          |import scala.annotation.switch
          |
          |case class Pet(var name: String) extends org.apache.avro.specific.SpecificRecordBase {
          |  def this() = this("")
          |  def get(field$: Int): AnyRef = {
          |    (field$: @switch) match {
          |      case pos if pos == 0 => {
          |        name
          |      }.asInstanceOf[AnyRef]
          |      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
          |    }
          |  }
          |  def put(field$: Int, value: Any): Unit = {
          |    (field$: @switch) match {
          |      case pos if pos == 0 => this.name = {
          |        value.toString
          |      }.asInstanceOf[String]
          |      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
          |    }
          |    ()
          |  }
          |  def getSchema: org.apache.avro.Schema = Pet.SCHEMA$
          |}
          |
          |object Pet {
          |  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Pet\",\"fields\":[{\"name\":\"name\",\"type\":\"string\"}]}")
          |}""".stripMargin.trim
    }

    "Expand two dependent case classes in a package to implement SpecificRecord" in {
      val schemaString = 
        """
          |package test
          |
          |/**
          | * Auto-Generated Schema
          | * @param pet Auto-Generated Field
          | */
          |case class Person(pet: Pet)
          |/**
          | * Auto-Generated Schema
          | * @param name Auto-Generated Field
          | */
          |case class Pet(name: String)""".stripMargin
      val gen = new Generator(SpecificRecord)
      val List(source2, source1) = gen.stringToStrings(schemaString)

      source1 ===
        """/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
          |package test
          |
          |import scala.annotation.switch
          |
          |/**
          | * Auto-Generated Schema
          | * @param pet Auto-Generated Field
          | */
          |case class Person(var pet: Pet) extends org.apache.avro.specific.SpecificRecordBase {
          |  def this() = this(new Pet)
          |  def get(field$: Int): AnyRef = {
          |    (field$: @switch) match {
          |      case pos if pos == 0 => {
          |        pet
          |      }.asInstanceOf[AnyRef]
          |      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
          |    }
          |  }
          |  def put(field$: Int, value: Any): Unit = {
          |    (field$: @switch) match {
          |      case pos if pos == 0 => this.pet = {
          |        value
          |      }.asInstanceOf[Pet]
          |      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
          |    }
          |    ()
          |  }
          |  def getSchema: org.apache.avro.Schema = Person.SCHEMA$
          |}
          |
          |object Person {
          |  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Person\",\"namespace\":\"test\",\"doc\":\"Auto-Generated Schema\",\"fields\":[{\"name\":\"pet\",\"type\":{\"type\":\"record\",\"name\":\"Pet\",\"doc\":\"Auto-Generated Schema\",\"fields\":[{\"name\":\"name\",\"type\":\"string\",\"doc\":\"Auto-Generated Field\"}]},\"doc\":\"Auto-Generated Field\"}]}")
          |}""".stripMargin.trim

      source2 ===
        """/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
          |package test
          |
          |import scala.annotation.switch
          |
          |/**
          | * Auto-Generated Schema
          | * @param name Auto-Generated Field
          | */
          |case class Pet(var name: String) extends org.apache.avro.specific.SpecificRecordBase {
          |  def this() = this("")
          |  def get(field$: Int): AnyRef = {
          |    (field$: @switch) match {
          |      case pos if pos == 0 => {
          |        name
          |      }.asInstanceOf[AnyRef]
          |      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
          |    }
          |  }
          |  def put(field$: Int, value: Any): Unit = {
          |    (field$: @switch) match {
          |      case pos if pos == 0 => this.name = {
          |        value.toString
          |      }.asInstanceOf[String]
          |      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
          |    }
          |    ()
          |  }
          |  def getSchema: org.apache.avro.Schema = Pet.SCHEMA$
          |}
          |
          |object Pet {
          |  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Pet\",\"namespace\":\"test\",\"doc\":\"Auto-Generated Schema\",\"fields\":[{\"name\":\"name\",\"type\":\"string\",\"doc\":\"Auto-Generated Field\"}]}")
          |}""".stripMargin.trim
    }

     "Expand two dependent case classes in a complex package to implement SpecificRecord" in {
      val schemaString = 
        """
          |package test.one.a
          |
          |/**
          | * Auto-Generated Schema
          | * @param pet Auto-Generated Field
          | */
          |case class Person(pet: Pet)
          |/**
          | * Auto-Generated Schema
          | * @param name Auto-Generated Field
          | */
          |case class Pet(name: String)""".stripMargin
      val gen = new Generator(SpecificRecord)
      val List(source2, source1) = gen.stringToStrings(schemaString)

       
      source1 ===
        """/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
          |package test.one.a
          |
          |import scala.annotation.switch
          |
          |/**
          | * Auto-Generated Schema
          | * @param pet Auto-Generated Field
          | */
          |case class Person(var pet: Pet) extends org.apache.avro.specific.SpecificRecordBase {
          |  def this() = this(new Pet)
          |  def get(field$: Int): AnyRef = {
          |    (field$: @switch) match {
          |      case pos if pos == 0 => {
          |        pet
          |      }.asInstanceOf[AnyRef]
          |      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
          |    }
          |  }
          |  def put(field$: Int, value: Any): Unit = {
          |    (field$: @switch) match {
          |      case pos if pos == 0 => this.pet = {
          |        value
          |      }.asInstanceOf[Pet]
          |      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
          |    }
          |    ()
          |  }
          |  def getSchema: org.apache.avro.Schema = Person.SCHEMA$
          |}
          |
          |object Person {
          |  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Person\",\"namespace\":\"test.one.a\",\"doc\":\"Auto-Generated Schema\",\"fields\":[{\"name\":\"pet\",\"type\":{\"type\":\"record\",\"name\":\"Pet\",\"doc\":\"Auto-Generated Schema\",\"fields\":[{\"name\":\"name\",\"type\":\"string\",\"doc\":\"Auto-Generated Field\"}]},\"doc\":\"Auto-Generated Field\"}]}")
          |}""".stripMargin.trim

      source2 ===
        """/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
          |package test.one.a
          |
          |import scala.annotation.switch
          |
          |/**
          | * Auto-Generated Schema
          | * @param name Auto-Generated Field
          | */
          |case class Pet(var name: String) extends org.apache.avro.specific.SpecificRecordBase {
          |  def this() = this("")
          |  def get(field$: Int): AnyRef = {
          |    (field$: @switch) match {
          |      case pos if pos == 0 => {
          |        name
          |      }.asInstanceOf[AnyRef]
          |      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
          |    }
          |  }
          |  def put(field$: Int, value: Any): Unit = {
          |    (field$: @switch) match {
          |      case pos if pos == 0 => this.name = {
          |        value.toString
          |      }.asInstanceOf[String]
          |      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
          |    }
          |    ()
          |  }
          |  def getSchema: org.apache.avro.Schema = Pet.SCHEMA$
          |}
          |
          |object Pet {
          |  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Pet\",\"namespace\":\"test.one.a\",\"doc\":\"Auto-Generated Schema\",\"fields\":[{\"name\":\"name\",\"type\":\"string\",\"doc\":\"Auto-Generated Field\"}]}")
          |}""".stripMargin.trim
    }


    "Expand a standard case class with mutable fields to implement SpecificRecord" in {
      val schemaString = 
        """
          |package test
          |
          |/**
          | * Auto-Generated Schema
          | * @param name Auto-Generated Field
          | */
          |case class Person(var name: String)""".stripMargin
      val gen = new Generator(SpecificRecord)
      val List(source) = gen.stringToStrings(schemaString)

       
      source ===
        """/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
          |package test
          |
          |import scala.annotation.switch
          |
          |/**
          | * Auto-Generated Schema
          | * @param name Auto-Generated Field
          | */
          |case class Person(var name: String) extends org.apache.avro.specific.SpecificRecordBase {
          |  def this() = this("")
          |  def get(field$: Int): AnyRef = {
          |    (field$: @switch) match {
          |      case pos if pos == 0 => {
          |        name
          |      }.asInstanceOf[AnyRef]
          |      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
          |    }
          |  }
          |  def put(field$: Int, value: Any): Unit = {
          |    (field$: @switch) match {
          |      case pos if pos == 0 => this.name = {
          |        value.toString
          |      }.asInstanceOf[String]
          |      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
          |    }
          |    ()
          |  }
          |  def getSchema: org.apache.avro.Schema = Person.SCHEMA$
          |}
          |
          |object Person {
          |  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Person\",\"namespace\":\"test\",\"doc\":\"Auto-Generated Schema\",\"fields\":[{\"name\":\"name\",\"type\":\"string\",\"doc\":\"Auto-Generated Field\"}]}")
          |}""".stripMargin.trim
    }



  "Expand a case class with mixed mutable and immutable fields to implement SpecificRecord" in {
      val schemaString = 
        """
          |package test
          |
          |/**
          | * Auto-Generated Schema
          | * @param name Auto-Generated Field
          | * @param age Auto-Generated Field
          | */
          |case class Person(name: String, var age: Int)""".stripMargin
      val gen = new Generator(SpecificRecord)
      val List(source) = gen.stringToStrings(schemaString)

      source ===
        """/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
          |package test
          |
          |import scala.annotation.switch
          |
          |/**
          | * Auto-Generated Schema
          | * @param name Auto-Generated Field
          | * @param age Auto-Generated Field
          | */
          |case class Person(var name: String, var age: Int) extends org.apache.avro.specific.SpecificRecordBase {
          |  def this() = this("", 0)
          |  def get(field$: Int): AnyRef = {
          |    (field$: @switch) match {
          |      case pos if pos == 0 => {
          |        name
          |      }.asInstanceOf[AnyRef]
          |      case pos if pos == 1 => {
          |        age
          |      }.asInstanceOf[AnyRef]
          |      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
          |    }
          |  }
          |  def put(field$: Int, value: Any): Unit = {
          |    (field$: @switch) match {
          |      case pos if pos == 0 => this.name = {
          |        value.toString
          |      }.asInstanceOf[String]
          |      case pos if pos == 1 => this.age = {
          |        value
          |      }.asInstanceOf[Int]
          |      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
          |    }
          |    ()
          |  }
          |  def getSchema: org.apache.avro.Schema = Person.SCHEMA$
          |}
          |
          |object Person {
          |  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Person\",\"namespace\":\"test\",\"doc\":\"Auto-Generated Schema\",\"fields\":[{\"name\":\"name\",\"type\":\"string\",\"doc\":\"Auto-Generated Field\"},{\"name\":\"age\",\"type\":\"int\",\"doc\":\"Auto-Generated Field\"}]}")
          |}""".stripMargin.trim
    }


    "Expand a scala enum to implement SpecificRecord" in {
      val codeString =
      """package example
        |
        |object Suit extends Enumeration {
        |  type Suit = Value
        |  val SPADES, DIAMONDS, CLUBS, HEARTS = Value
        |}
      """.stripMargin.trim
      val gen = new Generator(SpecificRecord)
      val List(source) = gen.stringToStrings(codeString)

      source ====
        """/**
          | * Autogenerated by Avro
          | * 
          | * DO NOT EDIT DIRECTLY
          | */
          |package example;  
          |@SuppressWarnings("all")
          |@org.apache.avro.specific.AvroGenerated
          |public enum Suit { 
          |  SPADES, DIAMONDS, CLUBS, HEARTS  ;
          |  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"enum\",\"name\":\"Suit\",\"namespace\":\"example\",\"symbols\":[\"SPADES\",\"DIAMONDS\",\"CLUBS\",\"HEARTS\"]}");
          |  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }
          |}
          |""".stripMargin
    }

    "Expand a case class with an enum field to implement SpecificRecord" in {
      val codeString =
        """
          |package example
          |
          |case class Card(suit: Suit = Suit.SPADES, number: Int)
          |
          |object Suit extends Enumeration {
          |  type Suit = Value
          |  val SPADES, DIAMONDS, CLUBS, HEARTS = Value
          |}
        """.stripMargin.trim
      val gen = new Generator(SpecificRecord)
      val List(sourceEnum, sourceRecord) = gen.stringToStrings(codeString)

      sourceRecord ====
        """/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
          |package example
          |
          |import scala.annotation.switch
          |
          |case class Card(var suit: Suit = Suit.SPADES, var number: Int) extends org.apache.avro.specific.SpecificRecordBase {
          |  def this() = this(Suit.SPADES, 0)
          |  def get(field$: Int): AnyRef = {
          |    (field$: @switch) match {
          |      case pos if pos == 0 => {
          |        suit
          |      }.asInstanceOf[AnyRef]
          |      case pos if pos == 1 => {
          |        number
          |      }.asInstanceOf[AnyRef]
          |      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
          |    }
          |  }
          |  def put(field$: Int, value: Any): Unit = {
          |    (field$: @switch) match {
          |      case pos if pos == 0 => this.suit = {
          |        value
          |      }.asInstanceOf[Suit]
          |      case pos if pos == 1 => this.number = {
          |        value
          |      }.asInstanceOf[Int]
          |      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
          |    }
          |    ()
          |  }
          |  def getSchema: org.apache.avro.Schema = Card.SCHEMA$
          |}
          |
          |object Card {
          |  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Card\",\"namespace\":\"example\",\"fields\":[{\"name\":\"suit\",\"type\":{\"type\":\"enum\",\"name\":\"Suit\",\"symbols\":[\"SPADES\",\"DIAMONDS\",\"CLUBS\",\"HEARTS\"]},\"default\":\"SPADES\"},{\"name\":\"number\",\"type\":\"int\"}]}")
          |}""".stripMargin.trim


      sourceEnum ====
        """/**
          | * Autogenerated by Avro
          | * 
          | * DO NOT EDIT DIRECTLY
          | */
          |package example;  
          |@SuppressWarnings("all")
          |@org.apache.avro.specific.AvroGenerated
          |public enum Suit { 
          |  SPADES, DIAMONDS, CLUBS, HEARTS  ;
          |  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"enum\",\"name\":\"Suit\",\"namespace\":\"example\",\"symbols\":[\"SPADES\",\"DIAMONDS\",\"CLUBS\",\"HEARTS\"]}");
          |  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }
          |}
          |""".stripMargin
    }

  }

}
