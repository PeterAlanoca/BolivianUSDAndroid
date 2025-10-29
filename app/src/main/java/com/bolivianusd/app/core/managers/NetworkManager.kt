package com.bolivianusd.app.core.managers

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

class NetworkManager(private val context: Context) {

    fun isConnected(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    suspend fun hasInternetAccess(): Boolean = withContext(Dispatchers.IO) {
        try {
            val url = URL(GOOGLE_URL)
            (url.openConnection() as HttpURLConnection).run {
                connectTimeout = 10000
                readTimeout = 10000
                requestMethod = "GET"
                connect()
                responseCode == 204
            }
        } catch (e: Exception) {
            false
        }
    }

    suspend fun isOnline(): Boolean {
        if (!isConnected()) return false
        return hasInternetAccess()
    }

    companion object {
        private const val GOOGLE_URL = "https://clients3.google.com/generate_204"
    }
}
