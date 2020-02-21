package com.vimosanan.dataanalysersg.ui.dataanalyse

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.vimosanan.dataanalysersg.R
import com.vimosanan.dataanalysersg.adapters.YearDataAdapter
import com.vimosanan.dataanalysersg.repository.models.RecordViewData
import com.vimosanan.dataanalysersg.util.NetworkStatus
import com.vimosanan.dataanalysersg.util.PaginationScrollListener
import com.vimosanan.dataanalysersg.util.Status
import com.vimosanan.dataanalysersg.viewmodels.ViewModelProviderFactory
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class DataAnalyseActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory

    private lateinit var viewModel: DataAnalyseViewModel

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

        viewModel = ViewModelProviders
            .of(this, viewModelProviderFactory)
            .get(DataAnalyseViewModel::class.java)

        adapter = YearDataAdapter(yearDataList)

        val layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        //NO NEED TO CHECK NETWORK CONNECTIVITY -> RETROFIT WILL HANDLE THOSE USING INTERCEPTOR AND USE CACHE ACCORDINGLY
        loadData()

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
                loadData()
            }
        })

        initObservers() //init observers for the view-model object


        txtTryAgain.setOnClickListener{
            loadData()
            txtInfo.setTextColor(ContextCompat.getColor(this, R.color.colorGreyDark))
            txtTryAgain.visibility = View.GONE
        }
    }

    private fun initObservers(){
        viewModel.recordViewDataList.observe(this, Observer {
            if(it != null && it.isNotEmpty()){
                yearDataList.addAll(it)
                adapter.setAdapter(yearDataList)
                isLoading = false
            }
        })

        viewModel.infoStr.observe(this, Observer {
            it?.let {
                txtInfo.text = it
            }
        })

        viewModel.loading.observe(this, Observer {
            if(it){
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.INVISIBLE
            }
        })

        viewModel.status.observe(this, Observer {
            if(it == Status.ERROR){
                txtInfo.setTextColor(ContextCompat.getColor(this, R.color.colorRed))
                txtTryAgain.visibility = View.VISIBLE
            }
        })
    }


    private fun loadData(){
        viewModel.loadData(offset, limit).observe(this, Observer { networkResource ->
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

                                    viewModel.processData(it)
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
                    viewModel.connectToLocalDatabaseWhenInError()
                }
            }
        })
    }


    companion object{
        //val TAG: String get() = "DataAnalyseActivity"
    }
}
