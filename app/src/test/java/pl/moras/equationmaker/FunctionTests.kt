package pl.moras.equationmaker

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.EditText
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.BeforeClass
import org.junit.internal.runners.JUnit38ClassRunner
import org.junit.runner.RunWith

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

class FunctionTests {

    @MockK
    lateinit var editText: EditText

    var activity = MainActivity()

    @Before
    fun setup() = MockKAnnotations.init(this)

    @Test
    fun should_get_variables_from_string(){
        every { editText.text.toString() } returns "X=2, y=3"

        val result = activity.getVariables(editText)

        assertArrayEquals(arrayOf("x=2", "y=3"), result)
    }

    @Test
    fun should_compute_function(){
        val function = "x*x"
        val variables = arrayOf("x=3")

        val result = activity.compute(function, variables)

        assertEquals(9f, result.toFloat())
    }

}
