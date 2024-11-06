package com.grovylessj.wifiscanner.data

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

data class NetworkDetails(
    val ssid: String,
    val ipAddress: String,
    val signalStrength: Int
)

class NetworkAnalysisService @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val wifiManager: WifiManager =
        context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

    fun getNetworkDetails(): NetworkDetails? {
        val wifiInfo = wifiManager.connectionInfo
        if (wifiInfo != null && wifiInfo.ssid != "<unknown ssid>") {
            val ssid = wifiInfo.ssid.removePrefix("\"").removeSuffix("\"")
            val ipAddress = intToIp(wifiInfo.ipAddress)
            val signalStrength = WifiManager.calculateSignalLevel(wifiInfo.rssi, 100)

            Log.d("NetworkAnalysisService", "SSID: $ssid, IP: $ipAddress, Signal: $signalStrength")

            return NetworkDetails(ssid, ipAddress, signalStrength)
        }
        return null
    }

    private fun intToIp(ip: Int): String {
        return "${ip and 0xFF}.${ip shr 8 and 0xFF}.${ip shr 16 and 0xFF}.${ip shr 24 and 0xFF}"
    }
}
