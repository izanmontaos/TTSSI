package com.grovylessj.wifiscanner.di


import android.content.Context
import com.grovylessj.wifiscanner.data.NetworkScannerService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideNetworkScannerService(
        @ApplicationContext context: Context
    ): NetworkScannerService {
        return NetworkScannerService(context)
    }
}
