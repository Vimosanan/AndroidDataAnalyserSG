package com.vimosanan.dataanalysersg.di

import android.app.Application
import androidx.room.Room
import com.vimosanan.dataanalysersg.app.Constants
import com.vimosanan.dataanalysersg.persistence.InternalRecordDao
import com.vimosanan.dataanalysersg.persistence.InternalRecordDatabase
import com.vimosanan.dataanalysersg.repository.network.ApiInterface
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
class AppModule {

    @Singleton
    @Provides
    fun provideRetrofitInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideNetworkService(retrofit: Retrofit): ApiInterface {
        return retrofit
            .create(ApiInterface::class.java)
    }

    @Singleton
    @Provides
    fun provideDb(app: Application): InternalRecordDatabase {
        return Room
            .databaseBuilder(app, InternalRecordDatabase::class.java, Constants.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideInternalDataDao(db: InternalRecordDatabase): InternalRecordDao {
        return db.internalDataDao()
    }
}