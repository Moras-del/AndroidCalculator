package com.example.calculatorapp

import androidx.test.InstrumentationRegistry
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4


import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
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
    var activityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @Test
    fun should_calculate_expression(){
        val expression: String = "((4*x)+y)/5"
        val variable: String = "X=10, y=5"
        onView(withId(R.id.functionEditText)).perform(typeText(expression), closeSoftKeyboard())
        onView(withId(R.id.varEditText)).perform(typeText(variable), closeSoftKeyboard())
        onView(withId(R.id.button)).perform(click())
        onView(withId(R.id.resultTextView)).check(matches(withText("Wynik: 9.00")))
    }
}
