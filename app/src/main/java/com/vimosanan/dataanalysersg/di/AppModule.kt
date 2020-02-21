package com.vimosanan.dataanalysersg.di

import android.app.Application
import android.util.Log
import androidx.room.Room
import com.vimosanan.dataanalysersg.app.Constants
import com.vimosanan.dataanalysersg.persistence.InternalRecordDao
import com.vimosanan.dataanalysersg.persistence.InternalRecordDatabase
import com.vimosanan.dataanalysersg.repository.network.ApiInterface
import com.vimosanan.dataanalysersg.util.NetworkStatus
import dagger.Module
import dagger.Provides
import okhttp3.*
import okhttp3.CacheControl
import okhttp3.logging.HttpLoggingInterceptor
import okio.IOException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
class AppModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(app: Application):OkHttpClient {
        return OkHttpClient.Builder()
            .cache(Cache(app.cacheDir, CACHE_SIZE))
            .addInterceptor(httpLoggingInterceptor())
            .addNetworkInterceptor(networkInterceptor())
            .addInterceptor(offlineInterceptor(app))
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofitInstance(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
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

    companion object {
        const val TAG = "CACHE"
        const val CACHE_SIZE = 5 * 1024 * 1024L
        const val HEADER_CACHE_CONTROL = "Cache-Control"
        const val HEADER_PRAGMA = "Pragma"

        private fun httpLoggingInterceptor(): HttpLoggingInterceptor {
            val httpLoggingInterceptor =
                HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                    override fun log(message: String) {
                        Log.d(TAG, "log: http log: $message")
                    }
                })
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            return httpLoggingInterceptor
        }

        private fun networkInterceptor(): Interceptor {
            return object : Interceptor {
                @Throws(IOException::class)
                override fun intercept(chain: Interceptor.Chain): Response {
                    Log.d(TAG, "network interceptor: called.")
                    val response: Response = chain.proceed(chain.request())
                    val cacheControl = CacheControl.Builder()
                        .maxAge(5, TimeUnit.SECONDS)
                        .build()
                    return response.newBuilder()
                        .removeHeader(HEADER_PRAGMA)
                        .removeHeader(HEADER_CACHE_CONTROL)
                        .header(HEADER_CACHE_CONTROL, cacheControl.toString())
                        .build()
                }
            }
        }

        private fun offlineInterceptor(app: Application): Interceptor {
            return object : Interceptor {
                @Throws(IOException::class)
                override fun intercept(chain: Interceptor.Chain): Response {
                    Log.d(TAG, "offline interceptor: called.")
                    var request: Request = chain.request()
                    // prevent caching when network is on. For that we use the "networkInterceptor"
                    if (!NetworkStatus.isNetworkConnected(app)) {
                        val cacheControl = CacheControl.Builder()
                            .maxStale(7, TimeUnit.DAYS)
                            .build()
                        request = request.newBuilder()
                            .removeHeader(HEADER_PRAGMA)
                            .removeHeader(HEADER_CACHE_CONTROL)
                            .cacheControl(cacheControl)
                            .build()
                    }
                    return chain.proceed(request)
                }
            }
        }
    }
}