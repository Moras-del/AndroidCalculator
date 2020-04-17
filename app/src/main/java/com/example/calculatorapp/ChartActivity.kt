package com.example.calculatorapp


import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log


import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder
import java.math.RoundingMode

import android.view.*
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet

import kotlinx.android.synthetic.main.activity_chart.*
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.suspendAtomicCancellableCoroutine
import net.objecthunter.exp4j.function.Function
import net.objecthunter.exp4j.function.Functions
import org.jetbrains.anko.custom.async
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.doAsyncResult
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.toast
import java.util.concurrent.Executors
import kotlin.coroutines.suspendCoroutine


class ChartActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_chart)
        val function = intent.getStringExtra(getString(R.string.pl_moras_mainactivity_function)).toString()
        val range = intent.getStringExtra(getString(R.string.pl_moras_mainactivity_range)).toInt()
        var expression: Expression? = null
        try {
            expression = ExpressionBuilder(function)
                .variable("x")
                .build()
        } catch (e: Exception) {
            toast(getString(R.string.function_error))
            this@ChartActivity.onBackPressed()
        }
        with(chartView) {
            description.isEnabled = false
            data = getChartData(expression!!, range.toDouble(), function)
            marker = MyMarker(this@ChartActivity, R.layout.marker_layout)
            setPinchZoom(true)
        }
    }

    private fun getChartData(expression: Expression, range: Double, function: String): LineData{
        val entryList = mutableListOf<Entry>()
        for (i in -range..range step 0.01){
            entryList.add(Entry(i.toFloat(), expression.setVariable("x", i).evaluate().toFloat()))
        }
        return LineData(LineDataSet(entryList, function))
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
