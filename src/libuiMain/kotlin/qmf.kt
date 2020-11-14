import libui.ktx.*
import page.adder
import page.negativeNumbers
import page.qmf
import utils.toBigDecimalOrNull
import utils.toClipboard
import utils.toFixedPoint

fun main() = appWindow(
        title = "Qmf",
        width = 50,
        height = 240
) {

    tabpane {
        page("Qmf") {
            qmf()
        }

        page("Záporné čísla") {
            negativeNumbers()
        }

        page("Binární sčítačka") {
            adder()
        }
    }

}


