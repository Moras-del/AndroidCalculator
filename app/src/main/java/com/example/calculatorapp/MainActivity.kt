package com.example.calculatorapp

import android.content.Context
import android.content.Intent
import android.os.*
import android.text.Editable
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.Window
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Cartesian
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder
import java.io.Serializable
import java.math.RoundingMode

class MainActivity : AppCompatActivity() {
    var buttonCompute: Button? = null
    var buttonChart: Button? = null
    var resultTextView: TextView? = null
    var functionEditText: EditText? = null
    var varEditText: EditText? = null
    var varTextView: TextView? = null
    var rangeEditText: EditText? = null
    var expression: Expression? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main)
        buttonCompute =     findViewById(R.id.button)
        buttonChart =       findViewById(R.id.makeChartButton)
        resultTextView =    findViewById(R.id.resultTextView)
        rangeEditText =     findViewById(R.id.rangeEditText)
        functionEditText =  findViewById(R.id.functionEditText)
        varEditText =       findViewById(R.id.varEditText)
        varTextView =       findViewById(R.id.varTextView)

        val v:Vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        buttonCompute!!.setOnClickListener(){
                val function: String = functionEditText?.text.toString().toLowerCase()
                val variable = varEditText?.text.toString()
                                                .replace(" ","")
                                                .toLowerCase()
                                                .split(",")
                                                .toTypedArray() //utwórz zmienne do równania
                if (function.equals("") || variable.get(0).equals("")) { //SPRAWDZ CZY WSZYSTKIE POLA WYPELNIONE
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        v.vibrate(500);
                    }
                    if (function.equals("")) {
                        functionEditText?.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake))
                    } else if (variable.get(0).equals("")) {
                        varEditText?.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake))
                    }
                } else {
                    try {
                        val e = ExpressionBuilder(function)
                        for (n in variable) {
                            e.variable(n[0].toString())
                        }
                        expression = e.build()
                        for (n in variable) {
                            expression!!.setVariable(n[0].toString(), n.substring(2).toDouble())
                        }
                        val result: Double = expression!!.evaluate()
                        resultTextView?.text = "Wynik: " + result.toBigDecimal().setScale(
                            2,
                            RoundingMode.HALF_EVEN
                        ).toString()
                    } catch (e:Exception){
                    Toast.makeText(
                        this@MainActivity,
                        "Błąd. Sprawdź poprawność funkcji oraz zmiennych",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        buttonChart!!.setOnClickListener(){
            val function: String = functionEditText?.text.toString()
            val range: String = rangeEditText?.text.toString()
            if (function.equals("") || range.equals("")) { //SPRAWDZ CZY WSZYSTKIE POLA WYPELNIONE
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    v.vibrate(500);
                }
                if (function.equals("")) {
                    functionEditText?.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake))
                } else if (range.equals("")) {
                    rangeEditText?.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake))
                }
            } else {
                val intent = Intent(this@MainActivity, ChartActivity::class.java) //INTENT DO WYKRESU
                intent.putExtra("FUNCTION", function)
                intent.putExtra("RANGE", range)
                startActivity(intent)
            }
        }
    }


}
