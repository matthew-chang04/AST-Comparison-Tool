package io.joern.c2cpgTypeRecovery

// *** BIT sizes assume 64 bit C++ standard, but are only used for comparisons to prioritize larger types
// Assume char is signed, because the other operator will always dictate the outcome
sealed trait NumericCategory
case object CharType extends NumericCategory
case object IntType extends NumericCategory
case object LongIntType extends NumericCategory
case object LongLongIntType extends NumericCategory
case object FloatType extends NumericCategory
case object DoubleType extends NumericCategory
case object LongDoubleType extends NumericCategory
case object PointerType extends NumericCategory
case object NonNumeric extends NumericCategory


case class CNumType(kind: NumericCategory, bits: Int, isPointer: Boolean, isFloatingPoint : Boolean, isUnsigned: Boolean, typeName: String)

def parseCNumType(typeName: String): CNumType = {

  val isPointer: Boolean = typeName.contains("*")

  val fixedWidthSignedPattern = """^int(8|16|32|64)_t""".r
  val fixedWidthUnsignedPattern = """^uint(8|16|32|64)_t""".r
  
  val sizesToType: Map[Int, NumericCategory] = Map(
    8 -> CharType,
    16 -> IntType,
    32 -> LongIntType,
    64 -> LongLongIntType
  )
  val typeToSize: Map[NumericCategory, Int] = Map(
    CharType -> 8,
    IntType -> 16,
    LongIntType -> 32,
    LongLongIntType -> 64,
  )
  val kind: NumericCategory =
    if (isPointer) PointerType
    else typeName match {
      case fixedWidthSignedPattern(bits) => sizesToType(bits.toInt)
      case fixedWidthUnsignedPattern(bits) => sizesToType(bits.toInt)
      case "size_t" => IntType
      case "ssize_t" => IntType

      case _ if typeName.contains("char") => CharType
      case _ if typeName.sliding("long".length).count(_ == "long") == 2 => LongLongIntType
      case _ if typeName.contains("long") => LongIntType
      case _ if typeName.contains("float") => FloatType
      case _ if typeName.contains("double") => DoubleType
      case _ => NonNumeric
    }

  val isUnsigned: Boolean = typeName.contains("unsigned")
  val isFloatingPoint: Boolean = kind == FloatType || kind == DoubleType || kind == LongDoubleType
  val bits: Int = typeToSize.getOrElse(kind, 0) // if it is not an integer type, the priority is simple (ptr > long double > double > float)
  
  CNumType(kind, bits, isPointer, isFloatingPoint, isUnsigned, typeName)
}