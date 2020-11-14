package page

import libui.ktx.*
import utils.toBigDecimalOrNull
import utils.toClipboard
import utils.toFixedPoint

fun TabPane.Page.qmf() = vbox {
    val q: TextField
    val f: TextField

    val a: TextField
    val aHex: TextField
    val b: TextField
    val bHex: TextField

    val sumHex: TextField
    val subHex: TextField

    val sumDec: TextField
    val subDec: TextField

    label("Formát")
    hbox {
        label("Q")
        q = textfield()
        label(".")
        f = textfield()
    }

    vbox {
        label("Dekadická čísla")
        hbox {
            label("A")
            a = textfield()
            label("Hex")
            aHex = textfield()
            button("copy") {
                action {
                    toClipboard(aHex.value)
                }
            }
        }
        hbox {
            label("B")
            b = textfield()
            label("Hex")
            bHex = textfield()
            button("copy") {
                action {
                    toClipboard(bHex.value)
                }
            }
        }
    }

    separator()
    label("")
    label("")
    label("Výsledky")

    separator()
    label("")
    label("Hex formát")
    hbox {
        label("A + B    ")
        sumHex = textfield()
        button("copy") {
            action {
                toClipboard(sumHex.value)
            }
        }
        label("A - B    ")
        subHex = textfield()
        button("copy") {
            action {
                toClipboard(subHex.value)
            }
        }
    }

    separator()
    label("")
    label("Dec formát")
    hbox {
        label("A + B    ")
        sumDec = textfield()
        button("copy") {
            action {
                toClipboard(sumDec.value)
            }
        }
        label("A - B    ")
        subDec = textfield()
        button("copy") {
            action {
                toClipboard(subDec.value)
            }
        }
    }

    separator()
    label("")
    button("Vypočítat") {
        action {
            fun missing(name: String): Nothing {
                MsgBox("Chybějící hodnoty", "Je třeba zadat všechny hodnotu '$name'")
                throw RuntimeException()
            }

            try {
                val qVal = q.value.toIntOrNull()?.toUInt() ?: missing("q")
                val fVal = f.value.toIntOrNull()?.toUInt() ?: missing("f")

                val aVal = a.value.toBigDecimalOrNull() ?: missing("A")
                val bVal = b.value.toBigDecimalOrNull() ?: missing("B")

                val aNum = aVal.toFixedPoint(qVal, fVal)
                val bNum = bVal.toFixedPoint(qVal, fVal)

                val sum = aNum + bNum
                val sub = aNum - bNum

                aHex.value = aNum.toHexString()
                bHex.value = bNum.toHexString()

                sumDec.value = sum.toDecimalString(2)
                subDec.value = sub.toDecimalString(2)

                sumHex.value = sum.toHexString()
                subHex.value = sub.toHexString()
            } catch (e: RuntimeException) {

            }
        }
    }
}