package pl.moras.equationmaker

import android.content.Context
import android.os.*
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.view.animation.AnimationUtils
import android.widget.EditText
import com.google.android.gms.ads.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_main.*
import net.objecthunter.exp4j.ExpressionBuilder
import org.jetbrains.anko.longToast
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var intersitialAd: InterstitialAd

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MobileAds.initialize(this)
        intersitialAd = InterstitialAd(this).apply {
            adUnitId = getString(R.string.ad_intersitial_id)
            loadAd(AdRequest.Builder().build())
            adListener = adListener()
        }
        adView.loadAd(AdRequest.Builder().build())

        resultTextView.text = getString(R.string.function_result, 0f)
        resultbutton.onClick {
            val function: String = functionText.text.toString()
            val variables = getVariables(varText)
            if (
                isFunctionValid(function) { errorCallback(functionTextLayout) } &&
                areVariablesValid(variables) { errorCallback(varLayout) }) {
                try {
                    val result = compute(function, variables)
                    resultTextView.text = getString(R.string.function_result, result)
                } catch (e: Exception) {
                    toast(getString(R.string.function_error))
                }
            }
        }
        chartButton.onClick {
            val function: String = functionText.text.toString()
            val range: String = rangeText.text.toString()

            if (isChartValid(function, range, {errorCallback(functionTextLayout)},{b: Boolean -> rangeErrorCallback(b) })) {
                if (intersitialAd.isLoaded) {
                    intersitialAd.show()
                }
                startActivity<ChartActivity>(
                    getString(R.string.pl_moras_mainactivity_function) to function,
                    getString(R.string.pl_moras_mainactivity_range) to range
                )
            }
        }

        functionTextLayout.setEndIconOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setItems(resources.getStringArray(R.array.functions_help)) { _, _ -> }
                .setTitle(getString(R.string.functions_help_title))
                .setPositiveButton(getString(R.string.ok)) { _, _ -> }
                .show()

        }
    }

    //make variables for function
    fun getVariables(vars: EditText): Array<String> = vars.text.toString()
        .replace(" ", "")
        .toLowerCase(Locale.getDefault())
        .split(",")
        .toTypedArray()


    fun compute(function: String, variables: Array<String>): Double {
        val e = ExpressionBuilder(function)
        for (n in variables) {
            e.variable(n[0].toString())
        }
        val expression = e.build()
        for (n in variables) {
            expression!!.setVariable(n[0].toString(), n.substring(2).toDouble())
        }
        return expression!!.evaluate()
    }

    private val errorCallback: (View)->Unit = {
        vibrate()
        animate(it)
    }

    private val rangeErrorCallback: (boolean: Boolean) -> (Unit) = {
        vibrate()
        animate(rangeLayout)
        if (it)
            longToast("max 100")
    }


    private fun vibrate(){
        val v = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            v.vibrate(500);
        }
    }

    private fun animate(view: View){
        view.startAnimation(
            AnimationUtils.loadAnimation(this, R.anim.shake)
        )
    }

    private fun adListener():AdListener =object: AdListener(){
        override fun onAdClosed() {
            intersitialAd.loadAd(AdRequest.Builder().build())
        }
    }

}
