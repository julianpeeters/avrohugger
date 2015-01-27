package test

import com.julianpeeters.avro.annotations._

//Primitive Types
@AvroRecord
case class AvroRecordTest00(var x: Int) 
@AvroRecord
case class AvroRecordTest01(var x: Float) 
@AvroRecord
case class AvroRecordTest02(var x: Long) 
@AvroRecord
case class AvroRecordTest03(var x: Double) 
@AvroRecord
case class AvroRecordTest04(var x: Boolean) 
@AvroRecord
case class AvroRecordTest05(var x: String) 
@AvroRecord
case class AvroRecordTest06(var x: Null) 
@AvroRecord
case class AvroRecordTest07(var x: Option[String])
@AvroRecord
case class AvroRecordTest08(var x: Option[Int])

@AvroRecord
case class AvroRecordTest10(var x: List[String])
@AvroRecord
case class AvroRecordTest11(var x: List[Int])
@AvroRecord
case class AvroRecordTest12(var x: Option[String])
@AvroRecord
case class AvroRecordTest13(var x: Option[Int])


//Primitive, 2-arity records
@AvroRecord
case class AvroRecordTest14(var x: Int, var y: Int) 
@AvroRecord
case class AvroRecordTest15(var x: Float, var y: Float) 
@AvroRecord
case class AvroRecordTest16(var x: Long, var y: Long) 
@AvroRecord
case class AvroRecordTest17(var x: Double, var y: Double) 
@AvroRecord
case class AvroRecordTest18(var x: Boolean, var y: Boolean) 
@AvroRecord
case class AvroRecordTest19(var x: String, var y: String) 
@AvroRecord
case class AvroRecordTest20(var x: Null, var y: Null) 

@AvroRecord
case class AvroRecordTest24(var x: List[String], var y: List[String])
@AvroRecord
case class AvroRecordTest25(var x: List[Int], var y: List[Int])
@AvroRecord
case class AvroRecordTest26(var x: Option[String], var y: Option[String])
@AvroRecord
case class AvroRecordTest27(var x: Option[Int], var y: Option[Int])


//Primitive nested
@AvroRecord
case class AvroRecordTest28(var x: List[List[String]])
@AvroRecord
case class AvroRecordTest29(var x: List[List[Int]])
@AvroRecord
case class AvroRecordTest30(var x: Option[List[String]])
@AvroRecord
case class AvroRecordTest31(var x: Option[List[Int]])
@AvroRecord
case class AvroRecordTest32(var x: List[Option[String]])
@AvroRecord
case class AvroRecordTest33(var x: List[Option[Int]])
@AvroRecord
case class AvroRecordTest34(var x: Option[List[Option[String]]])
@AvroRecord
case class AvroRecordTest35(var x: Option[List[Option[Int]]])
@AvroRecord
case class AvroRecordTest36(var x: List[Option[List[Option[String]]]])
@AvroRecord
case class AvroRecordTest37(var x: List[Option[List[Option[Int]]]])

//Primitive nested, 2-arity
@AvroRecord
case class AvroRecordTest38(var x: List[List[String]], var y: List[List[String]])
@AvroRecord
case class AvroRecordTest39(var x: List[List[Int]], var y: List[List[Int]])
@AvroRecord
case class AvroRecordTest40(var x: Option[List[String]], var y: Option[List[String]])
@AvroRecord
case class AvroRecordTest41(var x: Option[List[Int]], var y: Option[List[Int]])
@AvroRecord
case class AvroRecordTest42(var x: List[Option[String]], var y: List[Option[String]])
@AvroRecord
case class AvroRecordTest43(var x: List[Option[Int]], var y: List[Option[Int]])
@AvroRecord
case class AvroRecordTest44(var x: Option[List[Option[String]]], var y: Option[List[Option[String]]])
@AvroRecord
case class AvroRecordTest45(var x: Option[List[Option[Int]]], var y: Option[List[Option[Int]]])
@AvroRecord
case class AvroRecordTest46(var x: List[Option[List[Option[String]]]], var y: List[Option[List[Option[String]]]])
@AvroRecord
case class AvroRecordTest47(var x: List[Option[List[Option[Int]]]], var y: List[Option[List[Option[Int]]]])


//Primitive, 2-arity, heterogenous members
@AvroRecord
case class AvroRecordTest48(var x: Int, var y: String) 
@AvroRecord
case class AvroRecordTest49(var x: String, var y: Int) 
@AvroRecord
case class AvroRecordTest50(var x: Option[String], var y: Option[Int])
@AvroRecord
case class AvroRecordTest51(var x: Option[Int], var y: Option[String])
@AvroRecord
case class AvroRecordTest52(var x: List[String], var y: List[Int])
@AvroRecord
case class AvroRecordTest53(var x: List[Int], var y: List[String])
@AvroRecord
case class AvroRecordTest54(var x: Option[List[Option[String]]], var y: Option[List[Option[Int]]])
@AvroRecord
case class AvroRecordTest55(var x: Option[List[Option[Int]]], var y: Option[List[Option[String]]])
@AvroRecord
case class AvroRecordTest56(var x: List[Option[List[Option[String]]]], var y: List[Option[List[Option[Int]]]])
@AvroRecord
case class AvroRecordTest57(var x: List[Option[List[Option[Int]]]], var y: List[Option[List[Option[String]]]])


//User-defined types
@AvroRecord
case class AvroRecordTest58(var x: AvroRecordTest00) 
@AvroRecord
case class AvroRecordTest59(var x: AvroRecordTest58) 
@AvroRecord
case class AvroRecordTest60(var x: AvroRecordTest00, var y: AvroRecordTest58)
@AvroRecord
case class AvroRecordTest61(var x: List[AvroRecordTest00])
@AvroRecord
case class AvroRecordTest62(var x: List[AvroRecordTest58])
@AvroRecord
case class AvroRecordTest63(var x: List[AvroRecordTest00], var y: List[AvroRecordTest60])
@AvroRecord
case class AvroRecordTest64(var x: Option[AvroRecordTest00]) 
@AvroRecord
case class AvroRecordTest65(var x: Option[AvroRecordTest00]) 
@AvroRecord
case class AvroRecordTest66(var x: Option[AvroRecordTest58]) 
@AvroRecord
case class AvroRecordTest67(var x: Option[AvroRecordTest00], var y: Option[AvroRecordTest60])
@AvroRecord
case class AvroRecordTest68(var x: Option[List[Option[AvroRecordTest00]]], var y: List[Option[List[AvroRecordTest01]]])

