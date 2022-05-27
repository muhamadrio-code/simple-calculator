package com.seratus.simplecalculator

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.math.pow
import kotlin.math.sqrt

class MainViewModel : ViewModel() {

    private var switch = false
    private var isFinish = false
    private val stringBuilder = StringBuilder(12)
    private var value1 = 0.0
    private var value2 = ""
    private var operatorFun :() -> Double = {0.0}
    private var operatorSign = ""
        set(value) {
            field = value
            this.stringBuilder.clear()
        }
    private var result = 0.0
        set(value) {
            field = value
            switch = false
            stringBuilder.clear()
            stringBuilder.append(value)
            setTextValue()
        }
    val textResult by lazy { MutableStateFlow("0") }
    val operatorDisplayText by lazy { MutableStateFlow("") }

    private fun setTextValue(){
        textResult.value = if (stringBuilder.isEmpty()){
            "0"
        } else {
            stringBuilder.toString()
        }
    }

    fun onButtonNumberClicked(string: CharSequence){
        if (isFinish){
            onOperatorClicked(Operators.CLEAR_ALL)
        }
        stringBuilder.append(string)
        setTextValue()
    }

    fun setValueToCalculate(value: CharSequence){
        if (!switch){
            value1 = value.toString().toDouble()
            textResult.value = value.toString()
            stringBuilder.clear()
        } else {
            value2 = value.toString()
        }
    }

    private fun createText() : String {
        switch = true
        return "$value1 $operatorSign"
    }

    fun onOperatorClicked(operatorName:String){
        when(operatorName){
            Operators.CLEAR -> {
                stringBuilder.clear()
                setTextValue()
            }
            Operators.CLEAR_ALL -> {
                stringBuilder.clear()
                operatorDisplayText.value = ""
                value1 = 0.0
                value2 = ""
                isFinish = false
                switch = false
                setTextValue()
            }
            Operators.DELETE -> {
                isFinish = false
                switch = false
                if (operatorDisplayText.value.contains('=')){
                    operatorDisplayText.value = ""
                } else {
                    if (stringBuilder.isNotEmpty()){
                        stringBuilder.deleteAt(stringBuilder.lastIndex)
                    }
                    setTextValue()
                }
            }
            Operators.DIVIDE -> {
                if (isFinish) return
                if (stringBuilder.isNotEmpty()){
                    stringBuilder.clear()
                }
                operatorSign = "/"
                operatorFun = { value1.div(value2.toDouble()) }
                operatorDisplayText.value = createText()
            }
            Operators.MINUS -> {
                if (isFinish) return
                if (stringBuilder.isNotEmpty()){
                    stringBuilder.clear()
                }
                operatorSign = "-"
                operatorFun = { value1.minus(value2.toDouble()) }
                operatorDisplayText.value = createText()
            }
            Operators.MULTIPLICATION -> {
                if (isFinish) return
                if (stringBuilder.isNotEmpty()){
                    stringBuilder.clear()
                }
                operatorSign = "*"
                operatorFun = { value1.times(value2.toDouble()) }
                operatorDisplayText.value = createText()
            }
            Operators.PLUS -> {
                if (isFinish) return
                if (stringBuilder.isNotEmpty()){
                    stringBuilder.clear()
                }
                operatorSign = "+"
                operatorFun = { value1.plus(value2.toDouble()) }
                operatorDisplayText.value = createText()
            }
            Operators.MODULO -> {
                if (isFinish) return
                if (stringBuilder.isNotEmpty()){
                    stringBuilder.clear()
                }
                operatorSign = "%"
                operatorFun = { value1.mod(value2.toDouble()) }
                operatorDisplayText.value = createText()
            }
            Operators.SQRT -> {
                result = sqrt(value1)
                operatorDisplayText.value = "sqrt($value1)"
            }
            Operators.SQR -> {
                result = value1.times(value1)
                operatorDisplayText.value = "sqr($value1)"
            }
            Operators.POWER -> {
                if (isFinish) return
                if (stringBuilder.isNotEmpty()){
                    stringBuilder.clear()
                }
                operatorSign = "^"
                operatorFun = { value1.pow(value2.toDouble()) }
                operatorDisplayText.value = createText()
            }
            Operators.COMA -> {
                if (isFinish) return
                if (!stringBuilder.contains('.')){
                    if (stringBuilder.isEmpty()){
                        stringBuilder.append('0')
                    }
                    stringBuilder.append('.')
                    setTextValue()
                }
            }
            Operators.NEGATIVE -> {
                if (isFinish) return
                if (!stringBuilder.contains('-')){
                    stringBuilder.insert(0,'-')
                } else {
                    stringBuilder.deleteAt(0)
                }
                setTextValue()
            }
            Operators.RESULT -> {
                result = operatorFun()
                isFinish = true
                operatorDisplayText.value = "$value1 $operatorSign $value2 = "
            }
        }
    }

}