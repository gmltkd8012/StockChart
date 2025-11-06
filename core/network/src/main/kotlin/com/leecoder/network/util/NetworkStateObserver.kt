package com.leecoder.network.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkStateObserver @Inject constructor(
    @ApplicationContext private val context: Context,
): NetworkCallback() {

    private val cm: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val _isConnectedNetwork = MutableStateFlow(checkInitialNetwork())
    val isConnectedNetwork: StateFlow<Boolean> = _isConnectedNetwork.asStateFlow()

    @Volatile
    private var isRegister = false

    init {
        register()
    }

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        Log.d("[LeeCoder]", "Network is onAvailable")
        _isConnectedNetwork.value = true
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        Log.e("[LeeCoder]", "Network is onLost")
        _isConnectedNetwork.value = false
    }


    private fun checkInitialNetwork(): Boolean {
        val network = cm.activeNetwork ?: return false
        val capabilities = cm.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    @Synchronized
    private fun register() {
        if (!isRegister) {
            cm.registerDefaultNetworkCallback(this)
            isRegister = true
        }
    }
}