package com.vimosanan.dataanalysersg.ui.dataanalyse

import com.vimosanan.dataanalysersg.persistence.InternalRecordDao
import com.vimosanan.dataanalysersg.repository.models.InternalRecordData
import com.vimosanan.dataanalysersg.repository.network.ApiInterface
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class DataAnalyseViewModelTest {
    private lateinit var dataAnalyseActivity: DataAnalyseViewModel

    @Mock
    private lateinit var apiInterface :ApiInterface

    @Mock
    private lateinit var internalRecordDao: InternalRecordDao

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        dataAnalyseActivity = DataAnalyseViewModel(apiInterface, internalRecordDao)
    }

    @Test
    fun getViewDataShouldReturnComputedTotalAndIsRegularWhenWeArePassing_list() {
        val internalRecordData = InternalRecordData("2000",1.0,2.0,3.0,4.0)
        val input = listOf(internalRecordData)
        val expectedValue = 10.0
        //then
        val actualValue = dataAnalyseActivity.getViewData(input)
        assertEquals(expectedValue, actualValue[0].total, 0.0)
    }

    @After
    fun tearDown() {
    }
}