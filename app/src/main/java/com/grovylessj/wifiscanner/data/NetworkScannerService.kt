package com.grovylessj.wifiscanner.data

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class NetworkScannerService @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val wifiManager: WifiManager? =
        context.applicationContext.getSystemService(Context.WIFI_SERVICE) as? WifiManager

    private val _scanResults = MutableLiveData<List<String>>()
    val scanResults: LiveData<List<String>> get() = _scanResults

    // BroadcastReceiver para escuchar los resultados del escaneo
    private val scanReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val success = intent?.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false) ?: false
            if (success) {
                handleScanSuccess()
            } else {
                handleScanFailure()
            }
        }
    }

    init {
        // Registra el BroadcastReceiver para escuchar SCAN_RESULTS_AVAILABLE_ACTION
        context.registerReceiver(scanReceiver, IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
    }

    fun startScan() {
        // Verifica si el WifiManager y los permisos de ubicación están disponibles
        if (wifiManager == null) {
            Log.e("NetworkScannerService", "WifiManager no está disponible.")
            return
        }

        if (!hasLocationPermission()) {
            Log.e("NetworkScannerService", "Permisos de ubicación no concedidos.")
            _scanResults.postValue(emptyList())
            return
        }

        // Intenta iniciar el escaneo
        val scanStarted = try {
            wifiManager.startScan()
        } catch (e: SecurityException) {
            Log.e("NetworkScannerService", "SecurityException: Permisos insuficientes para iniciar el escaneo.", e)
            false
        }

        if (!scanStarted) {
            Log.e("NetworkScannerService", "Fallo al iniciar el escaneo de redes.")
        }
    }

    private fun handleScanSuccess() {
        // Obtiene los resultados del escaneo de manera segura
        val results: List<ScanResult> = try {
            wifiManager?.scanResults ?: emptyList()
        } catch (e: SecurityException) {
            Log.e("NetworkScannerService", "SecurityException: No se pudo obtener scanResults.", e)
            emptyList()
        }

        // Filtra los resultados para devolver solo los SSID no vacíos
        _scanResults.postValue(results.mapNotNull { it.SSID.takeIf { ssid -> ssid.isNotEmpty() } })
    }

    private fun handleScanFailure() {
        Log.e("NetworkScannerService", "Error en el escaneo de redes Wi-Fi.")
        _scanResults.postValue(emptyList())
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    fun unregisterReceiver() {
        // Desregistrar el BroadcastReceiver cuando ya no se necesite
        context.unregisterReceiver(scanReceiver)
    }
}
