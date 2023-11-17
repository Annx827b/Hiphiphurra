package com.example.hiphiphurra

import android.view.View
import android.widget.Spinner
import org.hamcrest.Matcher
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun loginAndAddFriend() {
        onView(withId(R.id.edit_email)).perform(
            typeText("testperson1@hotmail.com"),
            closeSoftKeyboard()
        )
        onView(withId(R.id.edit_password)).perform(typeText("test1234"), closeSoftKeyboard())
        onView(withId(R.id.login_button)).perform(click())


        Thread.sleep(2000)

        onView(withId(R.id.add_button)).perform(click())

        Thread.sleep(2000)


        onView(withId(R.id.editText_name)).perform(typeText("Marie"), closeSoftKeyboard())
        onView(withId(R.id.editText_year)).perform(typeText("2001"), closeSoftKeyboard())
        onView(withId(R.id.day_spinner)).perform(setSpinnerSelection(9))
        onView(withId(R.id.month_spinner)).perform(setSpinnerSelection(4))

        onView(withId(R.id.add)).perform(click())

        Thread.sleep(3000)

    }

    private fun setSpinnerSelection(position: Int): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isAssignableFrom(Spinner::class.java)
            }

            override fun getDescription(): String {
                return "Set spinner selection"
            }

            override fun perform(uiController: UiController, view: View) {
                (view as Spinner).setSelection(position)
            }
        }
    }

}
