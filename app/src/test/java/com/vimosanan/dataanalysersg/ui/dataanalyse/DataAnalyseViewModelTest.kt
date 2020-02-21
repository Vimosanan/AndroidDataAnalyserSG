package com.vimosanan.dataanalysersg.ui.dataanalyse

import com.vimosanan.dataanalysersg.persistence.InternalRecordDao
import com.vimosanan.dataanalysersg.repository.models.InternalRecordData
import com.vimosanan.dataanalysersg.repository.models.Record
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
    private lateinit var dataAnalyseViewModel: DataAnalyseViewModel

    @Mock
    private lateinit var apiInterface :ApiInterface

    @Mock
    private lateinit var internalRecordDao: InternalRecordDao

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        dataAnalyseViewModel = DataAnalyseViewModel(apiInterface, internalRecordDao)
    }

    @Test
    fun `get viewData should return computed total and isRegular when we are passing list`() {
        val internalRecordData = InternalRecordData("2000",1.0,2.0,3.0,4.0)

        val input = listOf(internalRecordData)
        val expectedTotalValue = 10.0
        val expectedIsRegularValue = true
        //then
        val actualValue = dataAnalyseViewModel.getViewData(input)
        assertEquals(expectedTotalValue, actualValue[0].total, 0.0)
        assertEquals(expectedIsRegularValue, actualValue[0].isRegular)
    }

    @Test
    fun `get viewData should return computed total and isRegular when we are passing list2`() {
        val internalRecordData = InternalRecordData("2000",1.0,5.0,2.0,4.0)

        val input = listOf(internalRecordData)
        val expectedTotalValue = 12.0
        val expectedIsRegularValue = false
        //then
        val actualValue = dataAnalyseViewModel.getViewData(input)

        assertEquals(expectedTotalValue, actualValue[0].total, 0.0)
        assertEquals(expectedIsRegularValue, actualValue[0].isRegular)
    }

    @Test
    fun `combine for annual data should return computed object value when we are passing list`(){
        val record1 = Record(4.0, "2004-Q1", 4 )
        val record2 = Record(5.0, "2004-Q2", 4 )
        val record3 = Record(1.0, "2004-Q3", 4 )
        val record4 = Record(2.89, "2004-Q4", 4 )

        val input = listOf(record1, record2, record3, record4)
        val expectedValueYear = "2004"
        val expectedValueQ1 = 4.0
        val expectedValueQ4 = 2.89001

        //then
        val actualValue = dataAnalyseViewModel.combineForAnnualData(input)

        assertEquals(expectedValueYear, actualValue[0].year)
        assertEquals(expectedValueQ1, actualValue[0].firstQuarter, 0.0)
        assertEquals(expectedValueQ4, actualValue[0].forthQuarter, 0.09)
    }

    @Test
    fun `combine for annual data should return only size one array when passing list with size below 8`(){

        val record1 = Record(3.89, "2003-Q4", 1 )
        val record2 = Record(4.0, "2004-Q1", 2 )
        val record3 = Record(5.0, "2004-Q2", 3 )
        val record4 = Record(1.0, "2004-Q3", 4 )
        val record5 = Record(2.89, "2004-Q4", 5 )

        val input = listOf(record1, record2, record3, record4, record5)
        val expectedValueSize = 1
        val expectedValueYear = "2004"

        //then
        val actualValue = dataAnalyseViewModel.combineForAnnualData(input)

        assertEquals(expectedValueSize, actualValue.size)
        assertEquals(expectedValueYear, actualValue[0].year)
    }

    @Test
    fun `combine fir annual data should return empty array when the data is irregular`(){
        val record1 = Record(3.89, "2003-Q4", 1 )
        val record2 = Record(4.0, "2004-Q1", 2 )
        val record3 = Record(1.0, "2004-Q3", 4 )
        val record4 = Record(2.89, "2004-Q4", 5 )
        val record5 = Record(5.0, "2005-Q1", 3 )


        val input = listOf(record1, record2, record3, record4, record5)
        val expectedValueSize = 0

        //then
        val actualValue = dataAnalyseViewModel.combineForAnnualData(input)

        assertEquals(expectedValueSize, actualValue.size)
    }


    @After
    fun tearDown() {
    }
}