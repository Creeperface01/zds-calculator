package page

import libui.ktx.*
import utils.base2uint
import utils.toBase
import utils.toClipboard

fun TabPane.Page.systems() = vbox {
    val input: TextField
    val from: TextField

    val result: TextField
    val to: TextField

    hbox {
        label("Číslo")
        input = textfield()
        label("Základní soustava")
        from = textfield {
            value = "10"
        }
    }
    hbox {
        label("Výsledek")
        result = textfield(true)
        label("Cílová soustava")
        to = textfield {
            value = "2"
        }
    }

    hbox {
        button("Vypočítat") {
            action {
                val fromBase = from.value.toUIntOrNull() ?: return@action
                val toBase = to.value.toUIntOrNull() ?: return@action
                val num = input.value.base2uint(fromBase)

                result.value = num.toBase(toBase)
            }
        }
        button("Kopírovat") {
            action {
                toClipboard(result.value)
            }
        }
    }
}

fun TabPane.Page.bcd() {
    val input: TextField
    val negativeFormat: Combobox
    val result: TextField
    val unpacked: Checkbox

    hbox {
        label("Dec číslo")
        input = textfield()
        label("Záporný formát")
        negativeFormat = combobox {
            item("")
        }
        unpacked = checkbox("Rozvinutý formát") {
            value = false
        }
    }
    hbox {
        label("BCD číslo")
        result = textfield()
    }

    hbox {
        button("BCD -> Dec") {
            action {
//                val num = input.value.base2int(fromBase)
//
//                result.value = num.toBase(toBase)
            }
        }
        button("Dec -> BCD") {
            action {
//                val num = input.value.base2int(fromBase)
//
//                result.value = num.toBase(toBase)
            }
        }
        button("Kopírovat") {
            action {
                toClipboard(result.value)
            }
        }
    }
}