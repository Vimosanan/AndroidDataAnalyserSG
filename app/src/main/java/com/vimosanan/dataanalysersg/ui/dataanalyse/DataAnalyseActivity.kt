package com.vimosanan.dataanalysersg.ui.dataanalyse

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.vimosanan.dataanalysersg.R
import com.vimosanan.dataanalysersg.adapters.YearDataAdapter
import com.vimosanan.dataanalysersg.repository.models.RecordViewData
import com.vimosanan.dataanalysersg.util.NetworkStatus
import com.vimosanan.dataanalysersg.util.PaginationScrollListener
import com.vimosanan.dataanalysersg.util.Status
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel

class DataAnalyseActivity : AppCompatActivity() {


    private val dataAnalyseViewModel: DataAnalyseViewModel by viewModel()

    private lateinit var adapter: YearDataAdapter
    private var yearDataList: MutableList<RecordViewData> = mutableListOf()

    private var offset = 2 //OFFSET VALUE
    private var limit = 40 // LIMIT OF EACH NETWORK CALL RESPONSE
    private var hasMore = true //WHETHER MORE DATA TO GET FROM REMOTE
    private var isLoading = false

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = YearDataAdapter(yearDataList)

        val layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        //CHECK FOR THE NETWORK CONNECTIVITY
        if(NetworkStatus.isNetworkConnected(this)) {
            isLoading = true
            loadData()
        } else {
            dataAnalyseViewModel.getDataFromLocalDatabase()
        }

        //HANDLE PAGINATION
        recyclerView.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
            override fun isLastPage(): Boolean {
                return !hasMore
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun loadMoreItems() {
                isLoading = true
                if(NetworkStatus.isNetworkConnected(this@DataAnalyseActivity)){
                    loadData()
                }
            }
        })

        dataAnalyseViewModel.recordViewDataList.observe(this, Observer {
            if(it != null && it.isNotEmpty()){
                yearDataList.addAll(it)
                adapter.setAdapter(yearDataList)
                isLoading = false
            }
        })

        dataAnalyseViewModel.infoStr.observe(this, Observer {
            it?.let {
                txtInfo.text = it
            }
        })

        dataAnalyseViewModel.loading.observe(this, Observer {
            if(it){
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.INVISIBLE
            }
        })
    }

    private fun loadData(){
        dataAnalyseViewModel.loadData(offset, limit).observe(this, Observer { networkResource ->
            when (networkResource.status) {
                //LOADING FOR NETWORK RESPONSE
                Status.LOADING -> {
                    txtInfo.text = resources.getString(R.string.txt_label_info_loading_network)
                    progressBar.visibility = View.VISIBLE
                }
                //IF NETWORK RESPONSE SUCCESS
                Status.SUCCESS -> {
                    val entityResponse = networkResource.data

                    if(entityResponse != null){
                        if(entityResponse.result != null){
                            if(entityResponse.result!!.records.isNotEmpty()){
                                entityResponse.result!!.records.let {
                                    offset += it.size

                                    //set there is no more data to check for pagination
                                    if(it.isEmpty() || it.size < limit){
                                        hasMore = false
                                    }
                                    dataAnalyseViewModel.processData(it)
                                }
                            } else {
                                isLoading = false
                            }
                        }
                    }

                }
                //IF NETWORK ERROR
                Status.ERROR -> {
                    progressBar.visibility = View.INVISIBLE
                    txtInfo.text = resources.getString(R.string.txt_label_info_error_loading_network)
                    dataAnalyseViewModel.connectToLocalDatabaseWhenInError()
                }
            }
        })
    }


    companion object{
        //val TAG: String get() = "DataAnalyseActivity"
    }
}
