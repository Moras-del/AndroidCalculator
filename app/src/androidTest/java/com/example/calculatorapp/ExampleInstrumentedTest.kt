package com.example.calculatorapp

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.espresso.intent.matcher.IntentMatchers.toPackage
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Matchers.allOf


import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class ExampleInstrumentedTest {

    @get:Rule
    val intentsTestRule: IntentsTestRule<MainActivity> = IntentsTestRule(MainActivity::class.java)

    @Test
    fun should_calculate_expression(){
        val expression: String = "((4*x)+y)/5"
        val variable: String = "X=10, y=5"
        onView(withId(R.id.functionEditText)).perform(typeText(expression), closeSoftKeyboard())
        onView(withId(R.id.varEditText)).perform(typeText(variable), closeSoftKeyboard())
        onView(withId(R.id.resultbutton)).perform(click())
        onView(withId(R.id.resultTextView)).check(matches(withText("Wynik: 9.00")))
    }

    @Test
    fun should_intent_to_chart(){
        val expression: String = "sin(x)"
        val range: String = "10"
        onView(withId(R.id.functionEditText)).perform(typeText(expression), closeSoftKeyboard())
        onView(withId(R.id.rangeEditText)).perform(typeText(range), closeSoftKeyboard())
        onView(withId(R.id.makeChartButton)).perform(click())
        intended(
            allOf(
                hasExtra(intentsTestRule.activity.getString(R.string.function_name), expression),
                hasExtra(intentsTestRule.activity.getString(R.string.range_name), range),
                toPackage("com.example.calculatorapp")))
    }
}
