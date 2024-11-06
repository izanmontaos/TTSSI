package com.grovylessj.wifiscanner.data

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.InetAddress
import javax.inject.Inject

data class HostDevice(
    val ipAddress: String,
    val macAddress: String?,
    val hostName: String?
)

class NetworkDiscoveryService @Inject constructor() {

    suspend fun discoverHosts(subnet: String): List<HostDevice> = withContext(Dispatchers.IO) {
        val hosts = mutableListOf<HostDevice>()

        for (i in 1..254) {  // Escaneo de IPs en el rango típico 1-254
            val hostIp = "$subnet.$i"
            try {
                val address = InetAddress.getByName(hostIp)

                // Intentamos obtener el nombre de host como verificación
                val hostName = if (address.canonicalHostName != hostIp) address.canonicalHostName else null
                if (hostName != null) {
                    hosts.add(HostDevice(ipAddress = hostIp, macAddress = null, hostName = hostName))
                    Log.d("NetworkDiscoveryService", "Host encontrado: $hostIp - Nombre: $hostName")
                }
            } catch (e: Exception) {
                Log.e("NetworkDiscoveryService", "Error al escanear $hostIp", e)
            }
        }
        hosts
    }
}
