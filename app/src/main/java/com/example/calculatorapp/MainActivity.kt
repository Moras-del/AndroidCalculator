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
import org.w3c.dom.Text
import java.io.Serializable
import java.math.RoundingMode

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main)
        val buttonCompute: Button       = findViewById(R.id.button)
        val buttonChart: Button         =       findViewById(R.id.makeChartButton)
        val resultTextView: TextView    =    findViewById(R.id.resultTextView)
        val rangeEditText: EditText     =     findViewById(R.id.rangeEditText)
        val functionEditText: EditText  =  findViewById(R.id.functionEditText)
        val varEditText: EditText       =       findViewById(R.id.varEditText)

        val v:Vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        buttonCompute!!.setOnClickListener(){
                val function: String = functionEditText?.text.toString().toLowerCase()
                val variable = varEditText?.text.toString()
                                                .replace(" ","")
                                                .toLowerCase()
                                                .split(",")
                                                .toTypedArray() //utwórz zmienne do równania
                if (function == "" || variable[0] == "") { //SPRAWDZ CZY WSZYSTKIE POLA WYPELNIONE
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        v.vibrate(500);
                    }
                    if (function.equals("")) {
                        functionEditText?.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake))
                    } else if (variable[0] == "") {
                        varEditText?.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake))
                    }
                } else {
                    try {
                        val e = ExpressionBuilder(function)
                        for (n in variable) {
                            e.variable(n[0].toString())
                        }
                        val expression = e.build()
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
            if (function == "" || range == "") { //SPRAWDZ CZY WSZYSTKIE POLA WYPELNIONE
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    v.vibrate(500);
                }
                if (function == "") {
                    functionEditText?.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake))
                } else if (range == "") {
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
