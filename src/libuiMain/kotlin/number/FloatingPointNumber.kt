package number

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.DecimalMode
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.toBigInteger
import platform.posix.pow
import utils.*
import kotlin.math.absoluteValue

class FloatingPointNumber constructor(
    val format: DecimalFormat,
    val significand: ULong,
    val exp: UInt,
    override val negative: Boolean = false
) : BitNumber(
    floatingPoint2Binary(format, significand, exp, negative),
    format.bits + format.expBits + 1u,
    format.size,
    true
) {

    override val signedValue: Long
        get() = value.toLong()

    init {
        println("fp sig: ${significand.toBase(2u)}")
        println("fp exp: ${exp.toULong().toBase(2u)}")
        println("fp negative: $negative")
        println("fp nvalue: ${value.toBase(2u)}")
    }

    fun toDecimalString(): String {
        var decimalValue = BigDecimal.fromUInt(1u)

        for (i in 0u until format.bits) {
            val weight = format.bits - i
            val bit = BigDecimal.fromULong(significand[i].toBit())

            val d = (BigDecimal.ONE.divide(
                BigDecimal.fromBigInteger(pow2big(weight)),
                DecimalMode((format.bits * 2u).toLong())
            ))

            if (!bit.isZero()) {
                println("toDecimal bit $i weight: $weight")
            }

            decimalValue += bit * d
        }

        println("toDecimal value: $decimalValue")
        println("toDecimal exp: ${exp.fromOffset(format.bias)}")

        val r = decimalValue * pow(2.0, exp.fromOffset(format.bias).toDouble()).toBigDecimal()
        return (if (negative) "-" else "") + r.toStringExpanded().trimEnd('.')
    }

    companion object {

        fun fromBinary(format: DecimalFormat, value: BigInteger): FloatingPointNumber {
            val sig = value and (pow2big(format.bits) - BigInteger.ONE)
            val exp = (value shr format.bits.toInt()) and (pow2big(format.expBits) - BigInteger.ONE)
            val s = (value shr (format.expBits + format.bits).toInt()) and BigInteger.ONE

            println("value: $value")
            println("value: ${value.ulongValue().toBase(2u)}")
            println("sig: ${sig.ulongValue().toBase(2u)}")
            println("exp: ${exp.ulongValue().toBase(2u)}")

            return FloatingPointNumber(format, sig.ulongValue(), exp.uintValue(), s.bitAt(0))
        }

        fun fromDecimalString(format: DecimalFormat, value: String): FloatingPointNumber? {
            val parts = value.split('.')

            if (parts.size !in 0..2) {
                return null
            }

            var intPart = parts[0].toBigIntegerOrNull() ?: return null
            val decPart =
                if (parts.size == 2) ("0." + parts[1]).toBigDecimalOrNull() ?: return null else BigDecimal.fromInt(0)

            val negative = intPart.isNegative
            intPart = intPart.abs()

            println("intpart: $intPart")
            println("decpart: ${decPart.toStringExpanded()}")

            var decBinary = BigInteger.fromInt(0)
            if (decPart > 0) {
                var bd = decPart

                for (i in parts[1].indices) {
                    val d = (BigDecimal.fromInt(1).divide(pow2(i + 1).toBigDecimal(), LONG_DEC_MODE))

                    if (bd / d >= 1) {
                        println("bit $i val ${d.toStringExpanded()} rest ${bd.toStringExpanded()}")
                        bd -= d
                        decBinary = decBinary or (1 shl (format.bits - i.toUInt() - 1u).toInt()).toBigInteger()
                    }

                    if (bd <= 0) {
                        break
                    }
                }
            }

            println("decbinary: ${decBinary.ulongValue().toBase(2u)}")

            val msb = intPart.findMSB()
            val decLsb = decBinary.findLSB()
            val decMsb = decBinary.findMSB()

            val exp: Int
            exp = if (!intPart.isZero()) {
                msb.toInt()
            } else {
                -(decLsb.toInt() + 1)
            }

            if (exp < 0) {
                decBinary = decBinary shr exp.absoluteValue
            }

            println("exp: $exp")

            println("int part: ${intPart.ulongValue().toBase(2u)}")
            println("dec part: ${decBinary.ulongValue().toBase(2u)}")
            println("msb: $msb")
            println("dec msb: $decMsb")
            println("dec lsb: $decLsb")

            val a = ((intPart and (pow2(msb + 1u) - 1u).toBigInteger()) shl (format.bits - msb).toInt()).ulongValue()
            val b = decBinary.ulongValue() shr msb//.reverseBits(to = decMsb.toInt() + 1)

            println("a: ${a.toBase(2u)}")
            println("b: ${b.toBase(2u)}")

            println("sig: ${(a or b).toBase(2u)}")
//            val significand = (a or b).reverseBits(to = format.bits.toInt())
            val significand = (a or b) and (pow2(format.bits) - 1u)
//
//            if(!a.isZero()) {
//                significand = a.ulongValue()
//            }
//
//            if(!b.isZero()) {
//                println("debug: $significand")
//                println("debug: $b")
//                significand = significand or b.ulongValue()
//                println("debug2: $significand")
//            }

//            significand = significand.reverseBits(to = format.bits.toInt())

            println("sig: ${significand.toBase(2u)}") //10010 1111000000

            return FloatingPointNumber(format, significand, exp.toOffset(format.bias), negative)
        }
    }
}