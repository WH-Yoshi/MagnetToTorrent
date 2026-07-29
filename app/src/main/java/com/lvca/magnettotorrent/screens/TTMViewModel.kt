package com.lvca.magnettotorrent.screens

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lvca.magnettotorrent.R
import com.lvca.magnettotorrent.convertTorrentToMagnet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TTMViewModel : ViewModel() {
    val torrentFileUri = mutableStateOf<Uri?>(null)

    val logsState = mutableStateListOf<String>()

    val logsTitle = mutableStateOf("")

    fun setTorrentFileUri(uri: Uri) {
        torrentFileUri.value = uri
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
        val uri = torrentFileUri.value ?: return
        viewModelScope.launch(Dispatchers.IO) {
            convertTorrentToMagnet(
                context = context,
                torrentUri = uri,
                logState = logsState,
                logsTitle = logsTitle,
            )
        }
    }
}
