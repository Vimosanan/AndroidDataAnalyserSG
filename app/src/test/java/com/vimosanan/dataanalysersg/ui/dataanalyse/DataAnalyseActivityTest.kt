package com.vimosanan.dataanalysersg.ui.dataanalyse

import android.view.View
import androidx.test.rule.ActivityTestRule
import com.vimosanan.dataanalysersg.R
import junit.framework.TestCase.assertNotNull
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config


@RunWith(RobolectricTestRunner::class)
@Config(manifest= Config.DEFAULT_MANIFEST_NAME)
class DataAnalyseActivityTest {

    @get:Rule
    val rule: ActivityTestRule<DataAnalyseActivity> = ActivityTestRule(DataAnalyseActivity::class.java)

    private var activity: DataAnalyseActivity? =  null

    @Before
    fun setUp() {
        activity = rule.activity
    }

    @Test
    fun testLaunch(){
        var view = activity?.findViewById<View>(R.id.txtInfo)

        assertNotNull(view)
    }

    @After
    fun tearDown() {
        activity = null
    }
}