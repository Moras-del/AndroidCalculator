package com.example.calculatorapp

import android.content.Context
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import kotlinx.android.synthetic.main.marker_layout.view.*

class MyMarker(context: Context?, layoutResource: Int) : MarkerView(context, layoutResource) {

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        markeryvalue.text = context.getString(R.string.marker_y_value, e!!.y)
        markerxvalue.text = context.getString(R.string.marker_x_value, e.x)

    }

}