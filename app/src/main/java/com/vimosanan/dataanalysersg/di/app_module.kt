package com.vimosanan.dataanalysersg.di

import androidx.room.Room
import com.vimosanan.dataanalysersg.app.Constants
import com.vimosanan.dataanalysersg.persistence.InternalRecordDatabase
import com.vimosanan.dataanalysersg.repository.network.ApiInterface
import com.vimosanan.dataanalysersg.ui.dataanalyse.DataAnalyseViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {

    viewModel {
        DataAnalyseViewModel(get(named("apiInterface")), get(named("dao")))
    }

    factory(named("apiInterface")) {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)
    }

    factory (named("dao")) {
        Room
            .databaseBuilder(androidContext(), InternalRecordDatabase::class.java, Constants.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
            .internalDataDao()
    }
}

