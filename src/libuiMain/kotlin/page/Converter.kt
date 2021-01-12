package page

import libui.ktx.*
import utils.base2uint
import utils.toBase
import utils.toClipboard

fun TabPane.Page.negativeNumbers() = vbox {
    val input: TextField
    val fromBase: TextField
    val toBase: TextField
    val result: TextField
    val bits: TextField
    val complement: Combobox

    hbox {
        label("A")
        input = textfield()

        separator()
        label("Soustava")
        fromBase = textfield {
            value = "10"
        }

        separator()
        label("počet bitů")
        bits = textfield {
            value = "8"
        }

        separator()
        label("Záporný formát")
        complement = combobox {
            item("Přímý kód")
            item("Jednotkový doplněk")
            item("Dvojkový doplněk")

            value = 0
//            item("9")
//            item("10")
        }
    }
    separator()

    hbox {
        label("Cílová soustava")
        toBase = textfield {
            value = "2"
        }
    }
    hbox {
        label("Výsledek")
        result = textfield(true)
        button("Kopírovat") {
            action {
                toClipboard(result.value)
            }
        }
    }
    button("Vypočítat") {
        action {
            val bitsNum = bits.value.toIntOrNull() ?: return@action
            val fromBaseNum = fromBase.value.toUIntOrNull() ?: return@action
            val toBaseNum = toBase.value.toUIntOrNull() ?: return@action
            val num = input.value.base2uint(fromBaseNum)

            var resultNum = when (complement.value) {
                0 -> num or (1uL shl (bitsNum - 1))
                1 -> num.inv()
                2 -> num.inv() + 1u
                else -> return@action
            }

            resultNum = resultNum and ((1uL shl bitsNum) - 1u)
            result.value = resultNum.toBase(toBaseNum)
        }
    }
}