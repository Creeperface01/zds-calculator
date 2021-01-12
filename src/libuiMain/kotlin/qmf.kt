import libui.ktx.appWindow
import libui.ktx.page
import libui.ktx.tabpane
import page.*

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

        page("Číselné soustavy") {
            systems()
        }

        page("Záporné čísla") {
            negativeNumbers()
        }

        page("Floating point") {
            floatingPoint()
        }
//
//        page("Binární sčítačka") {
//            adder()
//        }
    }
}


