package com.vimosanan.dataanalysersg.di

import com.vimosanan.dataanalysersg.di.dataanalyse.DataAnalyserViewModelModule
import com.vimosanan.dataanalysersg.ui.dataanalyse.DataAnalyseActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class ActivityBuilderModule {

    @ContributesAndroidInjector(modules = [
                    DataAnalyserViewModelModule::class])
    abstract fun contributeDataAnalyseActivity(): DataAnalyseActivity

}