package com.vimosanan.dataanalysersg

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.vimosanan.dataanalysersg.ui.dataanalyse.DataAnalyseActivity

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
class ExampleInstrumentedTest {

    @Rule
    @JvmField
    val rule: ActivityTestRule<DataAnalyseActivity> = ActivityTestRule(DataAnalyseActivity::class.java)

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.vimosanan.dataanalysersg", appContext.packageName)
    }


    @Test
    fun `check_title_matches_as_expected`(){
        Espresso.onView(ViewMatchers.withId(R.id.txtTitle)).check(matches(withText("Mobile Data Usage\nSINGAPORE")))
    }
}
