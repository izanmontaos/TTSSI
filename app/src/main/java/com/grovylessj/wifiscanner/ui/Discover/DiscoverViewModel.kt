package com.grovylessj.wifiscanner.ui.Discover

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grovylessj.wifiscanner.data.HostDevice
import com.grovylessj.wifiscanner.data.NetworkDiscoveryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val repository: NetworkDiscoveryRepository
) : ViewModel() {

    private val _hosts = MutableLiveData<List<HostDevice>>()
    val hosts: LiveData<List<HostDevice>> get() = _hosts

    fun discoverHosts(subnet: String) {
        viewModelScope.launch {
            _hosts.postValue(repository.discoverHosts(subnet))
        }
    }
}
