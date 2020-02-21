package com.vimosanan.dataanalysersg.di.dataanalyse

import androidx.lifecycle.ViewModel
import com.vimosanan.dataanalysersg.di.ViewModelKey
import com.vimosanan.dataanalysersg.ui.dataanalyse.DataAnalyseViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class DataAnalyserViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(DataAnalyseViewModel::class)
    abstract fun bindDataAnalyseViewModel(dataAnalyseViewModel: DataAnalyseViewModel):ViewModel
}