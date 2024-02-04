package com.nb.findfalcone.ui.activities

import android.os.Looper
import android.os.MessageQueue.IdleHandler
import android.view.View
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.base.IdlingResourceRegistry
import androidx.test.espresso.matcher.RootMatchers.isPlatformPopup
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import com.nb.findfalcone.R
import kotlinx.coroutines.delay
import okhttp3.internal.wait
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.containsString
import org.hamcrest.Matchers.hasEntry
import org.hamcrest.Matchers.instanceOf
import org.hamcrest.Matchers.`is`
import kotlin.concurrent.thread

@RunWith(JUnit4::class)
class MainActivityTest {

    lateinit var idleHandler: IdlingResourceRegistry

    @Before
    fun launches_the_application_successfully() {
        ActivityScenario.launch(MainActivity::class.java)
        idleHandler = IdlingResourceRegistry(Looper.myLooper())
    }

    @Test
    fun select_the_space_vehicle_and_planet() {
        onView(withId(R.id.vehicleSpinner)).perform(click())

        onData(
           allOf(`is`(instanceOf(String::class.java)), `is`("Space pod"))
        ).perform(click())

       // onView(withId(R.id.tvItem)).check(matches(withText("Space pod")))

    }



}

