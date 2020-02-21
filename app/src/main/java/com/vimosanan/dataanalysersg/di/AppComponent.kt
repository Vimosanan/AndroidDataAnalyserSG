package com.vimosanan.dataanalysersg.di

import android.app.Application
import com.vimosanan.dataanalysersg.adapters.YearDataAdapter
import com.vimosanan.dataanalysersg.app.DataAnalyserSGApp
import com.vimosanan.dataanalysersg.repository.network.ApiInterface
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
                AndroidSupportInjectionModule::class,
                ActivityBuilderModule::class,
                AppModule::class,
                ViewModelFactoryModule::class])
interface AppComponent: AndroidInjector<DataAnalyserSGApp> {

    @Component.Builder
    interface Builder{

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun getApiInterface(): ApiInterface
}