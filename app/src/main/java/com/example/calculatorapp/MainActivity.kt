package com.example.calculatorapp

import android.content.Context
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.view.Window
import android.view.animation.AnimationUtils
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_main.*
import net.objecthunter.exp4j.ExpressionBuilder
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.math.BigDecimal
import java.math.RoundingMode

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main)

        resultbutton.onClick {
            val function: String = functionText.text.toString()
            val variables = getVariables(varText)
            if (validateFunction(function, variables)){
                try {
                    val result = compute(function, variables)
                    resultTextView.text = getString(R.string.function_result, result)
                } catch (e:Exception){
                    toast(getString(R.string.function_error))
                }
            }
        }

        chartButton.onClick {
            val function: String = functionText.text.toString()
            val range: String = rangeText.text.toString()

            if(validateChart(function, range)){
                startActivity<ChartActivity>(
                    getString(R.string.pl_moras_mainactivity_function) to function,
                    getString(R.string.pl_moras_mainactivity_range) to range
                )
            }
        }
    }

    //utwórz zmienne do równania
    private fun getVariables(vars: EditText): Array<String> {
        return vars.text.toString()
            .replace(" ","")
            .toLowerCase()
            .split(",")
            .toTypedArray()
    }

    private fun compute(function: String, variables: Array<String>): Double {
        val e = ExpressionBuilder(function)
        for (n in variables) {
            e.variable(n[0].toString())
        }
        val expression = e.build()
        for (n in variables) {
            expression!!.setVariable(n[0].toString(), n.substring(2).toDouble())
        }
        return  expression!!.evaluate()
    }


    private fun validateFunction(function: String, variables: Array<String>): Boolean {
        if (function == "" || variables[0] == "") { //SPRAWDZ CZY WSZYSTKIE POLA WYPELNIONE
            vibrate()
            if (function == "") {
                functionTextLayout.startAnimation(
                    AnimationUtils.loadAnimation(
                        this@MainActivity,
                        R.anim.shake
                    )
                )
            } else if (variables[0] == "") {
                varLayout.startAnimation(
                    AnimationUtils.loadAnimation(
                        this@MainActivity,
                        R.anim.shake
                    )
                )
            }
            return false
        }
        return true
    }

    private fun validateChart(function: String, range: String): Boolean {
        if (function == "" || range == "") { //SPRAWDZ CZY WSZYSTKIE POLA WYPELNIONE
            vibrate()
            if (function == "") {
                functionTextLayout?.startAnimation(
                    AnimationUtils.loadAnimation(
                        this@MainActivity,
                        R.anim.shake
                    )
                )
            } else if (range == "") {
                rangeLayout?.startAnimation(
                    AnimationUtils.loadAnimation(
                        this@MainActivity,
                        R.anim.shake
                    )
                )
            }
            return false
        }
        return true
    }

    private fun vibrate(){
        val v = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            v.vibrate(500);
        }
    }

}
