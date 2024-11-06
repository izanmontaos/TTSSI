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

    val availableNetworks: LiveData<List<String>> = networkRepository.scanResults

    fun startNetworkScan() {
        viewModelScope.launch {
            networkRepository.startScan()
        }
    }

    fun unregisterReceiver() {
        networkRepository.unregisterReceiver()
    }
}
