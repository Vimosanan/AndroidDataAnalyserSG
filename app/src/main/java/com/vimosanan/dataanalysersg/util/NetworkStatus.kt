package com.vimosanan.dataanalysersg.util

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log

object NetworkStatus {
    /**
     * Check device network status
     * @param context
     * @return
     */
    fun isNetworkConnected(context: Context): Boolean {
        try{
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = connectivityManager.activeNetworkInfo

            return activeNetwork != null && activeNetwork.isConnected
        }catch (e: Exception){
            Log.e("NETWORK", e.message)
        }
        return false
    }
}