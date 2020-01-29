package com.example.calculatorapp

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.widget.Toast
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Cartesian
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder
import java.math.RoundingMode
import kotlin.math.round
import android.text.method.Touch.onTouchEvent
import android.util.Log
import com.anychart.scales.Linear
import android.text.method.Touch.onTouchEvent
import androidx.core.view.ViewCompat.*
import android.text.method.Touch.onTouchEvent
import android.util.AttributeSet
import android.util.Xml
import android.view.*
import com.anychart.chart.common.listener.Event
import com.anychart.chart.common.listener.ListenersInterface
import com.anychart.enums.*
import com.anychart.scales.Base
import android.view.MotionEvent
import android.view.View.OnTouchListener
import androidx.annotation.RequiresApi
import com.anychart.data.View
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar


class ChartActivity : AppCompatActivity() {
    var chartView: AnyChartView? = null
    var expression: Expression? = null
    var rangeSeekbar: CrystalRangeSeekbar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_chart)
        val function = intent.getStringExtra("FUNCTION").toString()
        val range: Int = intent.getStringExtra("RANGE").toInt()
        chartView = findViewById(R.id.any_chart_view)
        rangeSeekbar = findViewById(R.id.rangeSeekBar)

        try {
            expression = ExpressionBuilder(function)
                .variable("x")
                .build()
        } catch (e: Exception) {
            Toast.makeText(applicationContext, "Błąd. Sprawdź poprawność funkcji", Toast.LENGTH_LONG).show()
            this@ChartActivity.onBackPressed()
        }

        val LineChart: Cartesian = AnyChart.line()
        var chartRange: MutableList<DataEntry> = ArrayList()
        var index = 0
        for (i in -range.toDouble()..range.toDouble() step 0.01) {
            chartRange.add(
                index,
                ValueDataEntry(
                    i.toBigDecimal().setScale(2, RoundingMode.HALF_EVEN),
                    expression?.setVariable("x", i)?.evaluate()
                )
            )
            index++
        }
        LineChart.data(chartRange)
        LineChart.animation(true)
        LineChart.lineMarker(true)
        LineChart.yGrid(true)
        LineChart.tooltip().titleFormat("function () { return \"X = \" + this.x.toString()}")
        LineChart.tooltip().format( "function() { return \"Y = \" + parseFloat(this.value).toFixed(2).toString();}")
        chartView?.setChart(LineChart)
        rangeSeekbar?.setOnRangeSeekbarChangeListener { minValue: Number, maxValue: Number ->
            if (rangeSeekbar?.isPressed == false) {
                LineChart.xZoom().setTo(minValue, maxValue)
            }
        }
    }
        infix fun ClosedRange<Double>.step(step: Double): Iterable<Double> {
            require(start.isFinite())
            require(endInclusive.isFinite())
            require(step > 0.0) { "Step must be positive, was: $step." }
            val sequence = generateSequence(start) { previous ->
                if (previous == Double.POSITIVE_INFINITY) return@generateSequence null
                val next = previous + step
                if (next > endInclusive) null else next
            }
            return sequence.asIterable()
        }


}
