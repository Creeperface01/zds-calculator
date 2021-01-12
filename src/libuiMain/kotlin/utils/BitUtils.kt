@file:Suppress("NAME_SHADOWING")

package utils

import com.ionspin.kotlin.bignum.integer.BigInteger
import kotlin.math.min

const val HEX_VALUES = "0123456789ABCDEF"

operator fun ULong.get(index: UInt) = this[index.toInt()]

operator fun ULong.get(index: Int): Boolean {
    return ((this shr index) and 1uL) == 1uL
}

operator fun UInt.get(index: UInt) = this[index.toInt()]

operator fun UInt.get(index: Int): Boolean {
    return ((this shr index) and 1u) == 1u
}

fun ULong.setBit(index: UInt, value: Boolean) = this.setBit(index.toInt(), value)

fun ULong.setBit(index: Int, value: Boolean): ULong {
    return if (value) {
        this or (1uL shl index)
    } else {
        this and (1uL shl index).inv()
    }
}

fun UInt.setBit(index: UInt, value: Boolean) = this.setBit(index.toInt(), value)

fun UInt.setBit(index: Int, value: Boolean): UInt {
    return if (value) {
        this or (1u shl index)
    } else {
        this and (1u shl index).inv()
    }
}

fun ULong.toBinaryString(size: UInt): String {
    val builder = StringBuilder()

    for (i in (0 until size).reversed()) {
        builder.append(if (this[i]) 1 else 0)
    }

    return builder.toString()
}

fun UInt.toBinaryString(size: UInt): String {
    val builder = StringBuilder()

    for (i in (0 until size).reversed()) {
        builder.append(if (this[i]) 1 else 0)
    }

    return builder.toString()
}

fun ULong.toHexString(size: UInt): String {
    val builder = StringBuilder()

    for (i in 0 until size.roundToNibbles() step 4) {
        val nibble = (this shr i) and 0xfu
        builder.append(HEX_VALUES[nibble.toInt()])
    }

    return builder.reverse().toString()
}

fun UInt.toHexString(size: UInt): String {
    val builder = StringBuilder()

    for (i in 0 until size.roundToNibbles() step 4) {
        val nibble = (this shr i) and 0xfu
        builder.append(HEX_VALUES[nibble.toInt()])
    }

    return builder.reverse().toString()
}

fun UInt.roundToNibbles(): UInt {
    val rem = this and 0x3u

    if (rem == 0u) {
        return this
    }

    val nibbles = this shr 2
    return (nibbles + 1u) shl 2
}

fun UInt.roundToByte(): UInt {
    val rem = this and 0xffu

    if (rem == 0u) {
        return this
    }

    val bytes = this shr 8
    return (bytes + 1u) shl 8
}

fun UInt.maskBits(length: UInt) = this and (pow2(length).toUInt() - 1u)
fun ULong.maskBits(length: UInt) = this and (pow2(length) - 1u)

infix fun UInt.shl(bitCount: UInt) = this shl bitCount.toInt()
infix fun UInt.shr(bitCount: UInt) = this shr bitCount.toInt()

infix fun ULong.shl(bitCount: UInt) = this shl bitCount.toInt()
infix fun ULong.shr(bitCount: UInt) = this shr bitCount.toInt()

fun ULong.findMSB(): UInt {
    for (i in (0u until ULong.SIZE_BITS.toUInt()).reversed()) {
        if (this[i]) {
            return i
        }
    }

    return 0u
}

fun UInt.findMSB(): UInt {
    for (i in (0u until UInt.SIZE_BITS.toUInt()).reversed()) {
        if (this[i]) {
            return i
        }
    }

    return 0u
}

fun BigInteger.findMSB(): UInt {
    for (i in (0u until (this.numberOfWords * ULong.SIZE_BITS).toUInt()).reversed()) {
        if (this.bitAt(i.toLong())) {
            return i
        }
    }

    return 0u
}

fun ULong.findLSB(): UInt {
    for (i in 0u until ULong.SIZE_BITS.toUInt()) {
        if (this[i]) {
            return i
        }
    }

    return 0u
}

fun UInt.findLSB(): UInt {
    for (i in 0u until UInt.SIZE_BITS.toUInt()) {
        if (this[i]) {
            return i
        }
    }

    return 0u
}

fun BigInteger.findLSB(): UInt {
    for (i in 0u until (this.numberOfWords * ULong.SIZE_BITS).toUInt()) {
        if (this.bitAt(i.toLong())) {
            return i
        }
    }

    return 0u
}

fun ULong.reverseBits(from: Int = 0, to: Int = Int.MAX_VALUE): ULong { //inefficient
    var r = this
    val maxBit = min(ULong.SIZE_BITS - 1, to) - 1
    for (i in from until (maxBit / 2)) {
        val swapIndex = maxBit - i
        val b = r[i]

        println("swapping $i with $swapIndex")

        r = r.setBit(i, r[swapIndex])
        r = r.setBit(swapIndex, b)
    }

    return r
}

fun BigInteger.reverseBits(from: Int = 0, to: Int = Int.MAX_VALUE): BigInteger { //inefficient
    var r = this
    val maxBit = min((this.numberOfWords * ULong.SIZE_BITS) - 1, to) - 1
    for (i in from until (maxBit / 2)) {
        val swapIndex = (maxBit - i).toLong()
        val b = r.bitAt(i.toLong())

        println("swapping $i with $swapIndex - $b, ${r.bitAt(swapIndex)}")

        r = r.setBitAt(i.toLong(), r.bitAt(swapIndex))
        r = r.setBitAt(swapIndex, b)
    }

    return r
}