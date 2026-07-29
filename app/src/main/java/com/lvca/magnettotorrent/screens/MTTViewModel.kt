package com.lvca.magnettotorrent.screens

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lvca.magnettotorrent.R
import com.lvca.magnettotorrent.convertMagnetToTorrent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MTTViewModel : ViewModel() {
    val magnetLink = mutableStateOf("")

    val logsState = mutableStateListOf<String>()
    
    val logsTitle = mutableStateOf("")

    fun setMagnetLink(link: String) {
        magnetLink.value = link
    }

    fun clearLogs() {
        logsState.clear()
    }

    fun initLogsTitle(context: Context) {
        if (logsTitle.value.isEmpty()) {
            logsTitle.value = context.getString(R.string.logs)
        }
    }

    fun startConversion(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            convertMagnetToTorrent(
                context = context,
                magnetLink = magnetLink.value,
                logState = logsState,
                logsTitle = logsTitle
            )
        }
    }
}
