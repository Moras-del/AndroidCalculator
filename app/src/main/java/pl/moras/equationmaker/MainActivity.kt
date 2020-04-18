package pl.moras.equationmaker

import android.content.Context
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.view.Window
import android.view.animation.AnimationUtils
import android.widget.EditText
import com.google.android.gms.ads.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_main.*
import net.objecthunter.exp4j.ExpressionBuilder
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

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
                .setItems(resources.getStringArray(R.array.functions_help)){_,_->}
                .setTitle(getString(R.string.functions_help_title))
                .setPositiveButton(getString(R.string.ok)){_,_->}
                .show()

        }
    }
    //make variables for function
    fun getVariables(vars: EditText): Array<String> {
        return vars.text.toString()
            .replace(" ","")
            .toLowerCase()
            .split(",")
            .toTypedArray()
    }

    fun compute(function: String, variables: Array<String>): Double {
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
        if (function == "" || variables[0] == "") { //check if all edittexts are valid
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
        if (function == "" || range == ""|| !function.contains("x", true)) { //check if all edittexts are valid
            vibrate()
            if (function == "" || !function.contains("x", true)) {
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

    private fun adListener() =object: AdListener(){
        override fun onAdClosed() {
            intersitialAd.loadAd(AdRequest.Builder().build())
        }
    }

}
