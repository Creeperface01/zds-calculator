package utils

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.RoundingMode
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import kotlinx.cinterop.cstr
import number.BitNumber
import number.FixedPoint
import platform.posix.memcpy
import platform.posix.pow
import platform.windows.*

const val INT_SIZE_BITS = 32u

fun Int.checkNegative(): UInt {
    val value = this.positiveUInt()
    if (this < 0) {
        return value.to2Complement()
    }

    return value
}

fun Int.toBit(size: UInt) = BitNumber(this, size)

fun Int.positiveUInt() = (if (this < 0) (-this) else this).toUInt()

fun UInt.to1Complement() = inv()

fun UInt.to2Complement() = inv() + 1u

fun UInt.toBitNumber() = BitNumber(this, INT_SIZE_BITS)

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
            .roundToDigitPositionAfterDecimalPoint(0, RoundingMode.ROUND_HALF_AWAY_FROM_ZERO).intValue()

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

val hexMap = mapOf(
        '0' to 0,
        '1' to 1,
        '2' to 2,
        '3' to 3,
        '4' to 4,
        '5' to 5,
        '6' to 6,
        '7' to 7,
        '8' to 8,
        '9' to 9,
        'a' to 10,
        'b' to 11,
        'c' to 12,
        'd' to 13,
        'e' to 14,
        'f' to 15,
)

fun String.base2int(base: Int): Long {
    require(base <= 16) {
        "Only bases up to 16 are supported"
    }

    var r = 0L
    this.toLowerCase().reversed().forEachIndexed { index, c ->
        r += (pow(base.toDouble(), index.toDouble()).toLong() * (hexMap[c] ?: error("invalid base($base) string")))
    }

    return r
}

val hex2Map = mapOf(
        0L to '0',
        1L to '1',
        2L to '2',
        3L to '3',
        4L to '4',
        5L to '5',
        6L to '6',
        7L to '7',
        8L to '8',
        9L to '9',
        10L to 'a',
        11L to 'b',
        12L to 'c',
        13L to 'd',
        14L to 'e',
        15L to 'f',
)

fun Long.toBase(base: Int): String {
    require(base <= 16) {
        "Only bases up to 16 are supported"
    }
    var t: Long = this

    val str = StringBuilder()
    while (t != 0L) {
        str.append(hex2Map[t % base])
        t /= base
    }

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