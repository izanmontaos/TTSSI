package com.grovylessj.wifiscanner.data

import javax.inject.Inject

class NetworkDiscoveryRepository @Inject constructor(
    private val discoveryService: NetworkDiscoveryService
) {
    suspend fun discoverHosts(): List<HostDevice> {
        return discoveryService.discoverHosts()
    }
}
