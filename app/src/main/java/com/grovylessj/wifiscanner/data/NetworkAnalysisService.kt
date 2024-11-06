package com.grovylessj.wifiscanner.data

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

data class NetworkDetails(
    val ssid: String,
    val ipAddress: String,
    val signalStrength: Int,
    val networkType: String,
    val linkSpeed: Int?,
    val frequency: Int?,
    val macAddress: String?
)

class NetworkAnalysisService @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val wifiManager: WifiManager =
        context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

    private val connectivityManager =
        context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    fun getNetworkDetails(): NetworkDetails? {
        val wifiInfo = wifiManager.connectionInfo
        val networkType = getNetworkType()
        val ssid = wifiInfo.ssid.removePrefix("\"").removeSuffix("\"")
        val ipAddress = intToIp(wifiInfo.ipAddress)
        val signalStrength = WifiManager.calculateSignalLevel(wifiInfo.rssi, 100)
        val linkSpeed = wifiInfo.linkSpeed // Mbps
        val frequency = wifiInfo.frequency // MHz
        val macAddress = wifiInfo.macAddress

        return NetworkDetails(
            ssid = ssid,
            ipAddress = ipAddress,
            signalStrength = signalStrength,
            networkType = networkType,
            linkSpeed = if (linkSpeed > 0) linkSpeed else null,
            frequency = if (frequency > 0) frequency else null,
            macAddress = macAddress.takeIf { it.isNotEmpty() }
        )
    }

    private fun getNetworkType(): String {
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return when {
            capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true -> "Wi-Fi"
            capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true -> "Mobile Data"
            else -> "Unknown"
        }
    }

    private fun intToIp(ip: Int): String {
        return "${ip and 0xFF}.${ip shr 8 and 0xFF}.${ip shr 16 and 0xFF}.${ip shr 24 and 0xFF}"
    }
}
