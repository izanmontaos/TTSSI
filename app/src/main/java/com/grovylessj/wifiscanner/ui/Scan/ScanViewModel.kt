package com.grovylessj.wifiscanner.ui.Scan

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grovylessj.wifiscanner.data.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScanViewModel @Inject constructor(
    private val networkRepository: NetworkRepository
) : ViewModel() {

    // Exponemos los resultados del escaneo para que el Fragment lo observe
    val availableNetworks: LiveData<List<String>> = networkRepository.scanResults

    // Método para iniciar el escaneo
    fun startNetworkScan() {
        viewModelScope.launch {
            networkRepository.startScan()
        }
    }
}
