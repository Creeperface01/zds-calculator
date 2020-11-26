package page

import libui.ktx.*

fun TabPane.Page.calculators() = vbox {
    val bits: TextField
    hbox {
        label("Počet bitů")
        bits = textfield {
            value = "8"
        }
    }
}