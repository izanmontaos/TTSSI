package com.grovylessj.wifiscanner.ui.Analisis

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grovylessj.wifiscanner.data.NetworkAnalysisRepository
import com.grovylessj.wifiscanner.data.NetworkDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnalisisViewModel @Inject constructor(
    private val repository: NetworkAnalysisRepository
) : ViewModel() {

    private val _networkDetails = MutableLiveData<NetworkDetails?>()
    val networkDetails: LiveData<NetworkDetails?> get() = _networkDetails

    fun loadNetworkDetails() {
        viewModelScope.launch(Dispatchers.IO) {
            _networkDetails.postValue(repository.getNetworkDetails())
        }
    }
}
