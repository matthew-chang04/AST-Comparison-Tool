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


case class CNumType(kind: NumericCategory, priority: Int, isPointer: Boolean, isFloatingPoint : Boolean, isUnsigned: Boolean, typeName: String)

def parseCNumType(typeName: String): CNumType = {

  val isPointer: Boolean = typeName.contains("*")

  val fixedWidthSignedPattern = """^int(8|16|32|64)_t""".r
  val fixedWidthUnsignedPattern = """^uint(8|16|32|64)_t""".r
  
  val intPriorityToType: Map[Int, NumericCategory] = Map( // just to parse fixed width ints
    1 -> CharType,
    2 -> IntType,
    3 -> LongIntType,
    4 -> LongLongIntType,
  )
  val typeToPriority: Map[NumericCategory, Int] = Map(
    CharType -> 8,
    IntType -> 7,
    LongIntType -> 6,
    LongLongIntType -> 5,
    FloatType -> 4,
    DoubleType -> 3,
    LongDoubleType -> 2,
    PointerType -> 1
  )
  val kind: NumericCategory =
    if (isPointer) PointerType
    else typeName match {
      case fixedWidthSignedPattern(bits) => intPriorityToType(8 * bits.toInt)
      case fixedWidthUnsignedPattern(bits) => intPriorityToType(8 * bits.toInt)
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
  val priority: Int = typeToPriority.getOrElse(kind, 0) // if it is not an integer type, the priority is simple (ptr > long double > double > float)
  
  CNumType(kind, priority, isPointer, isFloatingPoint, isUnsigned, typeName)
}

def numTypePriority(num1: CNumType, num2: CNumType): String = {
  if (num1.isPointer && num2.isPointer) "UNDEF"
  else if (num1.priority == num2.priority) {
    if (num1.isUnsigned) num1.typeName else num2.typeName
  } else if (num1.priority > num2.priority) num1.typeName
  else num2.typeName
}