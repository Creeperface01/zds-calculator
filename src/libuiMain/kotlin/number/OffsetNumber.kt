package number

class OffsetNumber(
    value: ULong,
    size: UInt,
    val offset: Int
) : BitNumber(value, size, signed = true) {

    override val negative: Boolean
        get() = signedValue < 0

    override val signedValue: Long
        get() = value.toLong() - offset
}