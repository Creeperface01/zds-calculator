package page

import com.ionspin.kotlin.bignum.integer.toBigInteger
import libui.ktx.*
import number.FloatingPointNumber
import utils.*

fun TabPane.Page.floatingPoint() = vbox {
    var format = DECIMAL_FORMAT_BASE_BINARY_16

    val int: TextField
    val bin: TextField

    val binBase: TextField

    val s: TextField
    val exp: TextField
    val sig: TextField

    val expBase: TextField
    val sigBase: TextField

    val expType: Combobox

    val calcInt: Button
    val calcForm: Button
    val calcBin: Button

    hbox {
        label("Formát")
        combobox {
            item("Binary 16")
            item("Binary 32")
            item("Binary 64")
            item("Binary 128")

            value = 0

            action {
                format = when (this.value) {
                    1 -> DECIMAL_FORMAT_BASE_BINARY_32
                    2 -> DECIMAL_FORMAT_BASE_BINARY_64
                    3 -> DECIMAL_FORMAT_BASE_BINARY_128
                    else -> DECIMAL_FORMAT_BASE_BINARY_16
                }
            }
        }
    }
    vbox {
        label("Dekadická hodnota")
        hbox {
            int = textfield {
                value = "0"
            }
            separator()
            button("copy") {
                action {
                    toClipboard(int.value)
                }
            }
        }

        calcInt = button("Vypočítat")
        separator()

        label("Binární tvar")
        hbox {
            vbox.gridpane {
                nextCell()
                nextCell()
                label("Soustava")
                label("Reprezentace")

                row()

                label("s")
                s = textfield {
                    value = "0"
                }
                nextCell()
                nextCell()
                button("copy") {
                    action {
                        toClipboard(s.value)
                    }
                }

                row()

                label("exp")
                exp = textfield {
                    value = "0"
                }
                expBase = textfield {
                    value = "10"
                }
                expType = combobox {
                    item("Přímá")
                    item("Posunutá")

                    value = 0
                }
                button("copy") {
                    action {
                        toClipboard(exp.value)
                    }
                }

                row()

                label("Significand")
                sig = textfield {
                    value = "0"
                }
                sigBase = textfield {
                    value = "2"
                }
                nextCell()
                button("copy") {
                    action {
                        toClipboard(sig.value)
                    }
                }

                row()

                calcForm = button("Vypočítat")
            }
        }

        separator()

        hbox {
            gridpane {
                label("Binární formát")
                label("Soustava")

                row()
                bin = textfield {
                    value = "0"
                }
                binBase = textfield {
                    value = "2"
                }

                button("copy") {
                    action {
                        toClipboard(bin.value)
                    }
                }

                row()

                calcBin = button("Vypočítat")
            }
        }
    }

    fun fillValues(fp: FloatingPointNumber) {
        int.value = fp.toDecimalString()
        s.value = if (fp.negative) "1" else "0"

        val expb = expBase.value.toUIntOrNull() ?: 2u
        exp.value = if (expType.value == 0) {
            fp.exp.fromOffset(fp.format.bias).toLong().toBase(expb, fp.format.expBits)
        } else {
            fp.exp.toLong().toBase(expb, fp.format.expBits)
        }

        sig.value = fp.significand.toBase(sigBase.value.toUIntOrNull() ?: 2u, fp.format.bits)

//        println("value0: "+fp.value)
//        println("value1: "+fp.value.toBase(2u))
//        println("bin value: "+fp.value.toBase(binBase.value.toUIntOrNull() ?: 2u))
//        println("bias: "+fp.format.bias)
//        println("exp2: ${fp.exp.toULong().toBase(2u)}")
//        println("exp2: ${fp.exp}")
//        println("exp2: ${fp.exp.fromOffset(fp.format.bias)}")
//        println("exp2: ${fp.exp.fromOffset(fp.format.bias).toLong()}")
//        println("exp2: ${fp.exp.fromOffset(fp.format.bias).toLong().toBase(2u)}")
//        println("exp2: ${fp.exp.toULong().toBase(2u)}")
        bin.value = fp.value.toBase(binBase.value.toUIntOrNull() ?: 2u)
    }

    calcInt.action {
        val fp = FloatingPointNumber.fromDecimalString(format, int.value) ?: return@action
        fillValues(fp)
    }

    calcForm.action {
        val sigVal = sig.value.base2uint(sigBase.value.toUInt())
        val expVal = exp.value.base2int(expBase.value.toUInt()).toInt()

        val expUVal = if (expType.value == 0) {
            expVal.toOffset(format.bias)
        } else {
            expVal.toUInt()
        }

        val fp = FloatingPointNumber(format, sigVal, expUVal, s.value != "0")
        fillValues(fp)
    }

    calcBin.action {
        val binVal = bin.value.base2uint(binBase.value.toUIntOrNull() ?: 2u)
        val fp = FloatingPointNumber.fromBinary(format, binVal.toBigInteger())
        println("fp value: ${fp.value.toBase(2u)}")
        fillValues(fp)
    }
}