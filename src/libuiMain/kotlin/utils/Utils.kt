package utils

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.RoundingMode
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import kotlinx.cinterop.cstr
import number.BitNumber
import number.FixedPoint
import platform.posix.memcpy
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