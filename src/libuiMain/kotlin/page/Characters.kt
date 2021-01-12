package page

import libui.ktx.*
import utils.base2uint
import utils.toHexString

fun TabPane.Page.characters() = vbox {
    hbox {
        val hexUTF8: TextField
        val hexUTF16: TextField
        val hexUTF32: TextField

        val unicode: TextField

        val utf82unicodeBtn: Button
        val utf162unicodeBtn: Button
        val utf322unicodeBtn: Button
        val unicode2utfBtn: Button

        vbox {
            hbox {
                label("UTF8 Hex")
                hexUTF8 = textfield()
            }
            hbox {
                label("UTF16 Hex")
                hexUTF16 = textfield()
            }
            hbox {
                label("UTF32 Hex")
                hexUTF32 = textfield()
            }
            utf82unicodeBtn = button("UTF8 -> unicode")
            utf162unicodeBtn = button("UTF16 -> unicode")
            utf322unicodeBtn = button("UTF32 -> unicode")
        }

        vbox {
            hbox {
                label("unicode")
                unicode = textfield()
            }
            unicode2utfBtn = button("unicode -> UTF")
        }

        fun unicode2utf() {
            var value = unicode.value.toLowerCase()

            if (value.startsWith("0x") || value.startsWith("u+")) {
                value = value.substring(2)
            }

            val unicodeNumber = value.base2uint(16u)
            val pos = (unicodeNumber shr 0xf) and 0xffu

            fun utf32() {
                hexUTF32.value = "0x" + unicodeNumber.toUInt().toHexString(32u)
            }

            fun utf16() {
                if (unicodeNumber <= 0xffffu) {
                    hexUTF16.value = "0x" + unicodeNumber.toUInt().toHexString(16u)
                    return
                }

                val data = unicodeNumber - 0x01_0000u

                val lead = ((data shr 10) and 0b11_1111_1111u) + 0xD800u
                val trail = (data and 0b11_1111_1111u) + 0xDC00u

                hexUTF16.value = "0x${(lead and 0xffffu).toUInt().toHexString(16u)} 0x${
                    (trail and 0xffffu).toUInt().toHexString(16u)
                }"
            }

            fun utf8() {
                val parts = IntArray(4) {
                    ((unicodeNumber shr (it * 6)) and 0b11_1111u).toInt()
                }

                var zeroByte = 4
                for (i in parts.indices) {
                    if (parts[i] == 0) {
                        zeroByte = i
                        break
                    }
                }

                for (i in 0 until zeroByte) {
                    parts[i] = parts[i] or (1 shl 7)
                }

                val prefix = when (zeroByte) {
                    1 -> 0b0000_0000
                    2 -> 0b1100_0000
                    3 -> 0b1110_0000
                    else -> 0b1111_0000
                }

                parts[zeroByte - 1] = parts[zeroByte - 1] or prefix

                hexUTF8.value = parts.reversed().joinToString(" ") {
                    "0x" + it.toUInt().toHexString(8u)
                }
            }

            utf32()
            utf16()
            utf8()
        }

        fun utf82unicode() {
            val input = hexUTF8.value.toLowerCase().replace("0x", "").replace(" ", "")

            if (input.isBlank()) {
                return
            }

            val number = input.base2uint(16u)
            val bytes = IntArray(4) {
                ((number shr (it * 8)) and 0xffu).toInt()
            }

            var leadIndex = 3

            for (i in bytes.indices) {
                if (bytes[i] == 0) {
                    leadIndex = i - 1
                }
            }

            if (leadIndex !in 0..3) {
                return
            }

            val usedBits = when (leadIndex) {
                0 -> 1
                else -> (1 shl (8 - (leadIndex + 2))) - 1
            }

            var result = 0
            for (i in bytes.indices) {
                val byte = if (i == leadIndex) {
                    bytes[i] and usedBits
                } else {
                    bytes[i] and 0b11_1111
                }

                result = result or ((byte and 0b11_1111) shl (i * 6))
            }

            unicode.value = "U+" + result.toUInt().toHexString(24u)
        }

        fun utf162unicode() {
            val input = hexUTF16.value.toLowerCase().replace("0x", "").replace(" ", "")

            if (input.isBlank()) {
                return
            }

            val number = input.base2uint(16u)
            if (number <= 0xFFFFu) {
                unicode.value = "U+" + number.toUInt().toHexString(16u)
                return
            }

            var lead = (number shr 16) and 0xffffu
            var trail = number and 0xffffu

            lead -= 0xD800u
            trail -= 0xDC00u

            lead = lead and 0b11_1111_1111u
            trail = trail and 0b11_1111_1111u

            val result = (trail or (lead shl 10)) + 0x01_0000u

            unicode.value = "U+" + result.toUInt().toHexString(24u)
        }

        fun utf322unicode() {
            val input = hexUTF32.value.toLowerCase().replace("0x", "").replace(" ", "")

            if (input.isBlank()) {
                return
            }

            val number = input.base2uint(16u)
            unicode.value = "U+" + number.toUInt().toHexString(24u)
        }

        unicode2utfBtn.action {
            unicode2utf()
        }

        utf82unicodeBtn.action {
            utf82unicode()
        }

        utf162unicodeBtn.action {
            utf162unicode()
        }

        utf322unicodeBtn.action {
            utf322unicode()
        }
    }
}