package number

import platform.posix.pow
import utils.get
import utils.to2Complement
import utils.toBinaryString
import utils.toHexString
import kotlin.math.abs

open class BitNumber : Comparable<BitNumber> {

    val value: UInt
    val size: UInt
    val byteSize = 16u
    val signed: Boolean

    val negative: Boolean
        get() = signed && value[size - 1u]

    val signedValue: Int
        get() = if (negative) from2Complement() else this.value.toInt()

    constructor(value: UInt, size: UInt, signed: Boolean = false) {
        this.size = size
//        this.byteSize = size.roundToNibbles()
        this.value = trim(value)
        this.signed = signed
    }

    constructor(value: Int, size: UInt) {
        this.size = size
//        this.byteSize = size.roundToNibbles()
        this.signed = value < 0

        this.value = trim(
                if (signed) {
                    abs(value).toUInt().to2Complement()
                } else {
                    value.toUInt()
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

    private fun trim(value: UInt) = value and ((pow(2.0, byteSize.toDouble()) - 1).toUInt())

    fun inv() = this.value.inv()

    fun from2Complement(): Int {
        return -trim((this.value - 1u).inv()).toInt()
    }

    override fun compareTo(other: BitNumber): Int {
        return this.signedValue.compareTo(other.signedValue)
    }

    override fun toString(): String {
        return "BitNumber(bits=${this.toBinaryString()}, signed=$signedValue, size=$size)"
    }

    fun toBinaryString() = this.value.toBinaryString(this.byteSize)

    fun toHexString() = this.value.toHexString(this.byteSize)
}