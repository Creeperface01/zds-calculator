package utils

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.DecimalMode
import com.ionspin.kotlin.bignum.decimal.RoundingMode
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.toBigInteger
import kotlinx.cinterop.cstr
import number.BitNumber
import number.FixedPoint
import platform.posix.memcpy
import platform.posix.pow
import platform.windows.*
import kotlin.math.absoluteValue

const val INT_SIZE_BITS = 32u

val LONG_DEC_MODE = DecimalMode(100)

fun Long.checkNegative(): ULong {
    val value = this.positiveUInt()
    if (this < 0) {
        return value.to2Complement()
    }

    return value
}

fun Long.toBit(size: UInt) = BitNumber(this, size)

fun Long.positiveUInt() = (if (this < 0) (-this) else this).toULong()

fun ULong.to1Complement() = inv()

fun ULong.to2Complement() = inv() + 1u

fun ULong.toBitNumber() = BitNumber(this, INT_SIZE_BITS)

infix fun Int.until(to: UInt): IntRange {
    if (to <= UInt.MIN_VALUE) return IntRange.EMPTY
    return this..(to - 1u).toInt()
}

fun Int.toFixedPoint(m: UInt, f: UInt) = this.toBigDecimal().toFixedPoint(m, f)

fun Double.toFixedPoint(m: UInt, f: UInt) = this.toBigDecimal().toFixedPoint(m, f)

fun Float.toFixedPoint(m: UInt, f: UInt) = this.toBigDecimal().toFixedPoint(m, f)

fun BigDecimal.toFixedPoint(m: UInt, f: UInt): FixedPoint {
//    val signed = this < 0
    val value = (this * BigDecimal.fromInt(2).pow(f.toInt()))
        .roundToDigitPositionAfterDecimalPoint(0, RoundingMode.ROUND_HALF_AWAY_FROM_ZERO).ulongValue()

    return FixedPoint(
            value,
            m,
            f
    )
}

fun UInt.pow(power: UInt): UInt {
    var value = this

    repeat((power - 1u).toInt()) {
        value += value
    }

    return value
}

fun String.toBigDecimalOrNull(): BigDecimal? {
    return try {
        this.toBigDecimal()
    } catch (e: ArithmeticException) {
        null
    }
}

fun String.toBigIntegerOrNull(): BigInteger? {
    return try {
        this.toBigInteger()
    } catch (e: ArithmeticException) {
        null
    }
}

val hexMap = mapOf(
    '0' to 0u,
    '1' to 1u,
    '2' to 2u,
    '3' to 3u,
    '4' to 4u,
    '5' to 5u,
    '6' to 6u,
    '7' to 7u,
    '8' to 8u,
    '9' to 9u,
    'a' to 10u,
    'b' to 11u,
    'c' to 12u,
    'd' to 13u,
    'e' to 14u,
    'f' to 15u,
)

fun String.base2int(base: UInt): Long {
    require(this.isNotEmpty())
    val negative = this[0] == '-'

    val s = if (negative) this.substring(1) else this

    val uval = s.base2uint(base).toLong()
    if (negative) {
        return -uval
    }

    return uval
}

fun String.base2uint(base: UInt): ULong {
    require(base <= 16u) {
        "Only bases up to 16 are supported"
    }

    var r = 0uL
    this.toLowerCase().reversed().forEachIndexed { index, c ->
        r += (pow(base.toDouble(), index.toDouble()).toULong() * (hexMap[c] ?: error("invalid base($base) string")))
    }

    return r
}

fun Long.toBCD(packed: Boolean = true): Long {
    var tmp = this
    var r = 0L
    val bits = if (packed) 4 else 8

    for (i in 0 until Long.SIZE_BITS step bits) {

    }

    return r
}

val hex2Map = mapOf(
    0uL to '0',
    1uL to '1',
    2uL to '2',
    3uL to '3',
    4uL to '4',
    5uL to '5',
    6uL to '6',
    7uL to '7',
    8uL to '8',
    9uL to '9',
    10uL to 'a',
    11uL to 'b',
    12uL to 'c',
    13uL to 'd',
    14uL to 'e',
    15uL to 'f',
)

fun Long.toBase(base: UInt, bits: UInt = 0u): String {
    val s = this.absoluteValue.toULong().toBase(base, bits)

    if (this < 0) {
        return "-$s"
    }

    return s
}

fun ULong.toBase(base: UInt, bits: UInt = 0u): String {
    require(base <= 16u) {
        "Only bases up to 16 are supported"
    }
    require(bits <= ULong.SIZE_BITS.toUInt()) {
        "ULong size exceeded ($bits)"
    }
    var t = this
    var i = 0u

    val digitBits = (base - 1u).findMSB() + 1u
    println("digit bits: $digitBits")

    val str = StringBuilder()
    do {
        str.append(hex2Map[t % base])
        t /= base

        i += digitBits
    } while (t != 0uL || (bits > 0u && i < bits))

    return str.reverse().toString()
}

fun toClipboard(lastLine: String?) {
    val len = lastLine!!.length + 1
    val hMem = GlobalAlloc(GMEM_MOVEABLE, len.toULong())
    memcpy(GlobalLock(hMem), lastLine.cstr, len.toULong())
    GlobalUnlock(hMem)
    val hwnd = HWND_TOP
    OpenClipboard(hwnd)
    EmptyClipboard()
    SetClipboardData(CF_TEXT, hMem)
    CloseClipboard()
}

fun pow2(power: Int): Long {
    require(power < Long.SIZE_BITS) {
        "Overflow"
    }
    return 1L shl power
}

fun pow2(power: UInt): ULong {
    require(power < ULong.SIZE_BITS.toUInt()) {
        "Overflow"
    }

    return 1uL shl power
}

fun pow2big(power: UInt) = BigInteger.ONE shl (power.toInt())

fun Boolean.toBit() = if (this) 1uL else 0uL

fun ULong.fromOffset(offset: ULong): Long = this.toLong() - offset.toLong()

fun Long.toOffset(offset: ULong): ULong = this.toULong() + offset

fun UInt.fromOffset(offset: UInt): Int = this.toInt() - offset.toInt()

fun Int.toOffset(offset: UInt): UInt = this.toUInt() + offset