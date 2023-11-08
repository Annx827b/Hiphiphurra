package com.example.hiphiphurra
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @get:Rule
    val activityRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun loginAndCheckRecyclerView() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.hiphiphurra", appContext.packageName)
        onView(withId(R.id.edit_email)).perform(typeText("test1234@hotmail.com"), closeSoftKeyboard(), pressImeActionButton())
        onView(withId(R.id.edit_password)).perform(typeText("1Frankrig3"), closeSoftKeyboard(), pressImeActionButton())
        onView(withId(R.id.login_button)).perform(click())
        Thread.sleep(1000)
    }



}