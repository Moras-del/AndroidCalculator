package pl.moras.equationmaker


import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log


import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder

import android.view.*
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd

import kotlinx.android.synthetic.main.activity_chart.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast
import java.lang.Exception


class ChartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_chart)
        val function = intent.getStringExtra(getString(R.string.pl_moras_mainactivity_function)).toString()
        val range = intent.getStringExtra(getString(R.string.pl_moras_mainactivity_range)).toInt()
        var chartData: LineData? = null
        try {
            val expression = ExpressionBuilder(function)
                .variable("x")
                .build()
            chartData = getChartData(expression, range.toDouble(), function)
        } catch (e:Exception){
            longToast(getString(R.string.function_error))
            onBackPressed()
        }
        with(chartView) {
            axisLeft.textColor = Color.WHITE
            axisRight.textColor = Color.WHITE
            xAxis.textColor = Color.WHITE
            legend.textColor=Color.WHITE
            description.isEnabled = false
            data = chartData
            marker = MyMarker(
                this@ChartActivity,
                R.layout.marker_layout
            )
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

    private infix fun ClosedRange<Double>.step(step: Double): Iterable<Double> {
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
