package com.grovylessj.wifiscanner.data

import javax.inject.Inject

class NetworkAnalysisRepository @Inject constructor(
    private val networkAnalysisService: NetworkAnalysisService
) {
    fun getNetworkDetails() = networkAnalysisService.getNetworkDetails()
}
