package com.grovylessj.wifiscanner.data

import android.content.Context
import android.net.wifi.WifiManager
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.Socket
import javax.inject.Inject

data class HostDevice(
    val ipAddress: String,
    val macAddress: String?,
    val hostName: String?
)

class NetworkDiscoveryService @Inject constructor(
    private val context: Context
) {

    // Obtiene el prefijo de subred actual basado en la dirección IP del dispositivo
    private fun getSubnetPrefix(): String? {
        val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val ipAddress = wifiManager.connectionInfo.ipAddress
        val ipBytes = byteArrayOf(
            (ipAddress and 0xff).toByte(),
            (ipAddress shr 8 and 0xff).toByte(),
            (ipAddress shr 16 and 0xff).toByte(),
            (ipAddress shr 24 and 0xff).toByte()
        )
        val inetAddress = InetAddress.getByAddress(ipBytes)
        val ipString = inetAddress.hostAddress
        val segments = ipString.split(".")
        return if (segments.size >= 3) "${segments[0]}.${segments[1]}.${segments[2]}" else null
    }

    // Descubre los hosts en la subred actual
    suspend fun discoverHosts(): List<HostDevice> = withContext(Dispatchers.IO) {
        val hosts = mutableListOf<HostDevice>()
        val subnetPrefix = getSubnetPrefix()

        if (subnetPrefix == null) {
            Log.e("NetworkDiscoveryService", "No se pudo determinar la subred")
            return@withContext hosts
        }

        for (i in 1..254) {  // Escaneo de IPs en el rango típico 1-254
            val hostIp = "$subnetPrefix.$i"
            try {
                val address = InetAddress.getByName(hostIp)

                // Intentamos abrir un socket en el puerto 80 como verificación
                try {
                    Socket(hostIp, 80).use { socket ->
                        val hostName = address.hostName.takeIf { it != hostIp }
                        hosts.add(HostDevice(ipAddress = hostIp, macAddress = null, hostName = hostName))
                        Log.d("NetworkDiscoveryService", "Host encontrado: $hostIp - Nombre: $hostName")
                    }
                } catch (e: Exception) {
                    // No se pudo conectar al puerto 80, omitimos este host
                }
            } catch (e: Exception) {
                Log.e("NetworkDiscoveryService", "Error al escanear $hostIp", e)
            }
        }
        hosts
    }
}
