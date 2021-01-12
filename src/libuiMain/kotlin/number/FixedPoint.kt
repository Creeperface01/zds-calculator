package number

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.RoundingMode
import utils.pow
import utils.until
import kotlin.math.abs

class FixedPoint : BitNumber {

    val m: UInt
    val f: UInt

    constructor(value: ULong, m: UInt, f: UInt, signed: Boolean = false) : super(value, m + f, signed = signed) {
        this.m = m
        this.f = f
    }

    constructor(value: Long, m: UInt, f: UInt) : super(value, m + f) {
        this.m = m
        this.f = f
    }

    constructor(n: BitNumber, m: UInt, f: UInt) : this(n.value, m, f, n.signed)

    operator fun plus(n: FixedPoint) = FixedPoint(super.plus(n), m, f)

    operator fun minus(n: FixedPoint) = FixedPoint(super.minus(n), m, f)

    fun toDecimalString(precision: Int = -1): String {
        val absValue = abs(this.signedValue).toUInt()
        var value = BigDecimal.fromUInt((absValue shr this.f.toInt()))
        val decimalPart = absValue and (2u.pow(this.f) - 1u)

        for (i in (0 until this.f)) {
            val weight = this.f.toInt() - i
            val bit = BigDecimal.fromUInt((decimalPart shr i) and 1u)
            val decimalValue = (BigDecimal.fromInt(1) / BigDecimal.fromInt(2).pow(weight))

            if (value > 0) { //wtf?
                value += bit * decimalValue
            } else {
                value = bit * decimalValue
            }
        }

        if (precision >= 0) {
            value = value.roundToDigitPositionAfterDecimalPoint(precision.toLong(), RoundingMode.ROUND_HALF_AWAY_FROM_ZERO)
        }

        if (this.negative) {
            value = -value
        }

        return value.toStringExpanded().trimEnd('.')
    }

    override fun toString(): String {
        return "BitNumber(bits=${this.toBinaryString()}, signed=${toDecimalString()}, m=$m, f=$f)"
    }
}