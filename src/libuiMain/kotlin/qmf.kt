import libui.ktx.*
import utils.toBigDecimalOrNull
import utils.toClipboard
import utils.toFixedPoint

fun main() = appWindow(
        title = "Qmf",
        width = 50,
        height = 240
) {

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


    form {
        vbox {
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

            label("")
            label("")
            label("Výsledky")

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

                        println("aVal: ${aVal.toStringExpanded()}")
                        println("bVal: ${bVal.toStringExpanded()}")

                        val aNum = aVal.toFixedPoint(qVal, fVal)
                        val bNum = bVal.toFixedPoint(qVal, fVal)

                        val sum = aNum + bNum
                        val sub = aNum - bNum

                        println("A: $aNum")
                        println("B: $bNum")

                        println("sum: $sum")
                        println("sub: $sub")

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
    }

//    println("test "+(BigDecimal.fromInt(0, DecimalMode(100)) + (BigDecimal.fromInt(1, DecimalMode(100)) / BigDecimal.fromInt(2, DecimalMode(100)).pow(11))).toStringExpanded())

//    val a = (-5).toBit(4u)
//    val b = 2.toBit(4u)
//
//    val c = a + b
//
//    println("a: $a")
//    println("b: $b")
//    println("sum: $c")
//
//    val fixedA = (-0.25).toFixedPoint(4u, 3u)
//    val fixedB = 2.5.toFixedPoint(4u, 3u)
//
//    val fixedC = fixedA + fixedB
//
//    println("fixedA: $fixedA")
//    println("fixedB: $fixedB")
//    println("fixedC: $fixedC")
//
//    val testA = (-214.66).toFixedPoint(10u, 4u)
//    val testB = (-235.33).toFixedPoint(10u, 4u)
//
//    println("testA: $testA")
//    println("testB: $testB")
//    println("test: "+(testA - testB))
}
