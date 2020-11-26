import libui.ktx.appWindow
import libui.ktx.page
import libui.ktx.tabpane
import page.characters
import page.qmf
import page.systems

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

//        page("Záporné čísla") {
//            negativeNumbers()
//        }
//
//        page("Binární sčítačka") {
//            adder()
//        }
    }

}


