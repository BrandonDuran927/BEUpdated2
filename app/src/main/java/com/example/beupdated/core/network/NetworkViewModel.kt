package com.example.beupdated.core.network

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class NetworkViewModel @Inject constructor(
    private val connectivityObserver: ConnectivityObserver,
    application: Application
) : AndroidViewModel(application) {
    private val _networkStatus = MutableStateFlow<NetworkStatus>(NetworkStatus.Unavailable)
    val networkStatus = _networkStatus.asStateFlow()

    init {
        viewModelScope.launch {
            delay(1000L)
            connectivityObserver.observeNetworkState()
                .debounce(750L)
                .collect { status ->
                println("Network status: $status")
                _networkStatus.value = status
            }
        }
    }
}