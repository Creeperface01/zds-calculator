@file:Suppress("NAME_SHADOWING")

package utils

const val HEX_VALUES = "0123456789ABCDEF"

operator fun UInt.get(index: UInt) = this[index.toInt()]

operator fun UInt.get(index: Int): Boolean {
    return ((this shr index) and 1u) == 1u
}

fun UInt.setBit(index: UInt, value: Boolean) = this.setBit(index.toInt(), value)

fun UInt.setBit(index: Int, value: Boolean): UInt {
    return if (value) {
        this or (1u shl index)
    } else {
        this and (1u shl index).inv()
    }
}

fun UInt.toBinaryString(size: UInt): String {
    val builder = StringBuilder()

    for (i in (0 until size).reversed()) {
        builder.append(if (this[i]) 1 else 0)
    }

    return builder.toString()
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