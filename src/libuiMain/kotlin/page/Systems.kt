package page

import libui.ktx.*
import utils.base2int
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
        from = textfield()
    }
    hbox {
        label("Výsledek")
        result = textfield(true)
        label("Cílová soustava")
        to = textfield()
    }

    hbox {
        button("Vypočítat") {
            action {
                val fromBase = from.value.toIntOrNull() ?: return@action
                val toBase = to.value.toIntOrNull() ?: return@action
                val num = input.value.base2int(fromBase)

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