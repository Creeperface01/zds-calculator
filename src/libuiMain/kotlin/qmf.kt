import libui.ktx.appWindow
import libui.ktx.page
import libui.ktx.tabpane
import page.adder
import page.characters
import page.negativeNumbers
import page.qmf

fun main() = appWindow(
        title = "Qmf",
        width = 50,
        height = 240
) {

    tabpane {
        page("Qmf") {
            qmf()
        }

        page("Kódování znaků") {
            characters()
        }

        page("Záporné čísla") {
            negativeNumbers()
        }

        page("Binární sčítačka") {
            adder()
        }
    }

}


