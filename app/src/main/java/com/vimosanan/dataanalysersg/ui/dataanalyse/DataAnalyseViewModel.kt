package com.vimosanan.dataanalysersg.ui.dataanalyse

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.vimosanan.dataanalysersg.app.Constants
import com.vimosanan.dataanalysersg.persistence.InternalRecordDao
import com.vimosanan.dataanalysersg.repository.models.InternalRecordData
import com.vimosanan.dataanalysersg.repository.models.Record
import com.vimosanan.dataanalysersg.repository.models.RecordViewData
import com.vimosanan.dataanalysersg.repository.network.ApiInterface
import com.vimosanan.dataanalysersg.util.Resource
import com.vimosanan.dataanalysersg.util.Status
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

class DataAnalyseViewModel @Inject constructor(private var apiInterface: ApiInterface?, var dao: InternalRecordDao): ViewModel(){
    //mutable live data
    var recordViewDataList = MutableLiveData<List<RecordViewData>> ()

    //mutable live data observing the text value for text_view_info
    var infoStr = MutableLiveData<String> ()

    //live data observing loader value
    var loading = MutableLiveData<Boolean> ()

    var status = MutableLiveData<Status> ()

    fun loadData(offset: Int, limit: Int) = liveData(IO){
        emit(Resource.loading(null))

        try{
            delay(2_000)
            val response = apiInterface?.getResults(offset, Constants.RESOURCE_ID,limit )

            if(response!!.isSuccessful) {
                emit(Resource.success(response.body()))

            }
        }catch (e: Exception){
            infoStr.postValue(NO_CACHE_DATA)
            loading.postValue(false)
            status.postValue(Status.ERROR)
        }
    }

    fun processData(recordList: List<Record>){
        CoroutineScope(IO).launch {
            infoStr.postValue(PROCESS_DATA_STR)
            val internalDataList = combineForAnnualData(recordList)

            infoStr.postValue(CACHING_IN_LOCAL_STR)
            saveToLocalDatabase(internalDataList)

            infoStr.postValue(READY_FOR_DISPLAY_STR)
            val viewDataList = getViewData(internalDataList)

            recordViewDataList.postValue(viewDataList)
            infoStr.postValue(SHOWING_RESULT_STR)
            loading.postValue(false)
        }
    }

    fun getViewData(dataList: List<InternalRecordData>): MutableList<RecordViewData> {
        val viewDataList: MutableList<RecordViewData> = mutableListOf()

        for(item in dataList){
            val isRegular =
                !(item.firstQuarter> item.secondQuarter || item.secondQuarter> item.thirdQuarter || item.thirdQuarter>item.forthQuarter)

            val total = item.firstQuarter + item.secondQuarter + item.thirdQuarter + item.forthQuarter

            viewDataList.add(RecordViewData(isRegular, total, item))
        }
        return viewDataList
    }


    private fun saveToLocalDatabase(dataList: List<InternalRecordData>){
        CoroutineScope(IO).launch {
            dao.insertDataToLocalDatabase(dataList)
        }
    }

    fun connectToLocalDatabaseWhenInError(){
        CoroutineScope(IO).launch {
            infoStr.postValue(DATABASE_CONN_STR)

            getDataFromLocalDatabase()
        }
    }


    fun getDataFromLocalDatabase(){
        infoStr.postValue(DATABASE_CONN_STR)

        CoroutineScope(IO).launch {
            delay(2000)
            val internalRecordDataList = dao.getAllData()

            infoStr.postValue(READY_FOR_DISPLAY_STR)
            val viewDataList = getViewData(internalRecordDataList)

            delay(2000)
            infoStr.postValue(SHOWING_RESULT_STR)

            recordViewDataList.postValue(viewDataList)
        }
    }

    fun combineForAnnualData(dataList: List<Record>): List<InternalRecordData> {
        var quarter1 = 0.0
        var quarter2 = 0.0
        var quarter3 = 0.0
        var quarter4 = 0.0

        var year: String
        val recordList: MutableList<InternalRecordData> = mutableListOf()

        for(item in dataList){

            val list = item.quarter?.split("-")

            year = list?.get(0)!!

            list[1].let {
                when (it) {
                    "Q1" -> {
                        //reassign value to 0.0 again for next year
                        quarter1 = 0.0
                        quarter2 = 0.0
                        quarter3 = 0.0
                        quarter4 = 0.0

                        quarter1 = item.volume!!
                    }
                    "Q2" -> {
                        quarter2 = item.volume!!
                    }
                    "Q3" -> {
                        quarter3 = item.volume!!
                    }
                    "Q4" -> {
                        quarter4 = item.volume!!
                    }
                }
            }

            if(quarter1 != 0.0 && quarter2 != 0.0 && quarter3 != 0.0 && quarter4 != 0.0){
                val data = InternalRecordData(year, quarter1, quarter2, quarter3, quarter4)
                recordList.add(data)

                //reassign value to 0.0 again for next year
                quarter1 = 0.0
                quarter2 = 0.0
                quarter3 = 0.0
                quarter4 = 0.0
            }
        }
        return recordList.toList()
    }

    companion object {
        val DATABASE_CONN_STR get() = "Connecting to local Database...."
        val SHOWING_RESULT_STR get() = "Showing results - Singapore Data Usage per year in PB"
        val PROCESS_DATA_STR get() = "Processing Data...."
        val CACHING_IN_LOCAL_STR get() = "Caching Data into local database...."
        val READY_FOR_DISPLAY_STR get() = "Getting Ready for the display...."
        val NO_CACHE_DATA get() = "OOPS, you are OFFLINE and NO CACHE available, please turn ON WIFI and try again!"
    }
}