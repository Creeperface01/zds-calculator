package page

import libui.ktx.*

fun TabPane.Page.negativeNumbers() = vbox {
    hbox {
        label("A")
        textfield {
            action {
                println("action")
            }
        }

        separator()
        label("Soustava")
        textfield {
            value = "10"

            action {

            }
        }

        separator()
        label("Doplněk")
        val c = combobox {
            item("1")
            item("2")
            item("9")
            item("10")
        }
    }
    separator()

    hbox {
        label("Cílová soustava")
        textfield {
            action {
                println("action")
            }
        }
    }
}