package utils

val DECIMAL_FORMAT_BASE_BINARY_16 = DecimalFormat(10u, 5u)
val DECIMAL_FORMAT_BASE_BINARY_32 = DecimalFormat(23u, 8u)
val DECIMAL_FORMAT_BASE_BINARY_64 = DecimalFormat(52u, 11u)
val DECIMAL_FORMAT_BASE_BINARY_128 = DecimalFormat(112u, 15u)

class DecimalFormat(
    val bits: UInt,
    val expBits: UInt
) {

    val bias = (pow2(expBits - 1u) - 1u).toUInt()
    val size = bits + expBits + 1u
}

fun floatingPoint2Binary(format: DecimalFormat, significand: ULong, exp: UInt, negative: Boolean = false): ULong {
    var r = 0uL

    r = r or (negative.toBit() shl (format.bits + format.expBits))
    r = r or (exp.maskBits(format.expBits) shl format.bits).toULong()
    r = r or significand.maskBits(format.bits)

    return r
}