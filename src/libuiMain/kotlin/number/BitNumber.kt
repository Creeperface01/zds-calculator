package number

import com.ionspin.kotlin.bignum.integer.BigInteger
import utils.*
import kotlin.math.abs

open class BitNumber : Comparable<BitNumber> {

    val value: ULong
    val size: UInt
    val wordSize: UInt

    val signed: Boolean

    open val negative: Boolean
        get() = signed && value[size - 1u]

    open val signedValue: Long
        get() = if (negative) from2Complement() else this.value.toLong()

    constructor(value: ULong, size: UInt, wordSize: UInt = 16u, signed: Boolean = false) {
        this.wordSize = wordSize
        this.size = size
        this.value = trim(value)
        this.signed = signed
    }

    constructor(value: Long, size: UInt, wordSize: UInt = 16u) {
        this.size = size
        this.wordSize = wordSize
        this.signed = value < 0

        this.value = trim(
            if (signed) {
                abs(value).toULong().to2Complement()
            } else {
                value.toULong()
            }
        )
    }

    open operator fun plus(n: BitNumber) = plus(n, this.size)

    open fun plus(n: BitNumber, size: UInt): BitNumber {
        return BitNumber(this.signedValue + n.signedValue, size)
    }

    open operator fun minus(n: BitNumber) = minus(n, this.size)

    open fun minus(n: BitNumber, size: UInt): BitNumber {
        return BitNumber(this.signedValue - n.signedValue, size)
    }

    private fun trim(value: ULong) = value and (pow2big(wordSize) - BigInteger.ONE).ulongValue()

    fun inv() = this.value.inv()

    fun from2Complement(): Long {
        return -trim((this.value - 1uL).inv()).toLong()
    }

    override fun compareTo(other: BitNumber): Int {
        return this.signedValue.compareTo(other.signedValue)
    }

    override fun toString(): String {
        return "BitNumber(bits=${this.toBinaryString()}, signed=$signedValue, size=$size)"
    }

    fun toBinaryString() = this.value.toBinaryString(this.wordSize)

    fun toHexString() = this.value.toHexString(this.wordSize)
}