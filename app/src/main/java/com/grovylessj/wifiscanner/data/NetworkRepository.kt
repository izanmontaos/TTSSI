package com.grovylessj.wifiscanner.data

import javax.inject.Inject

class NetworkRepository @Inject constructor(
    private val networkScannerService: NetworkScannerService
) {
    // Exponemos los resultados del escaneo al ViewModel
    val scanResults = networkScannerService.scanResults

    // Inicia el escaneo llamando al servicio
    fun startScan() {
        networkScannerService.startScan()
    }
}
