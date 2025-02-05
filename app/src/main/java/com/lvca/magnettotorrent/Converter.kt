package com.lvca.magnettotorrent

import android.content.Context
import android.net.http.NetworkException
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.MutableState
import com.lvca.magnettotorrent.FileUtil.getExternalDownloadDirectory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
suspend fun convertMagnetToTorrent(context: Context, magnetLink: String, logState: MutableState<String>) {
    val outputDir = getExternalDownloadDirectory()
    val downloader = TorrentDownloader()

    try {
        downloader.fetchAndSaveMagnet(context, magnetLink, outputDir!!, logState)
    } catch (e: IOException) {
        logState.value += "File operation problem: ${e.message}\n"
        CoroutineScope(Dispatchers.Main).launch {
            Toast.makeText(context, R.string.io_exception_occurred, Toast.LENGTH_SHORT).show()
        }
    } catch (e: NetworkException) {
        logState.value += "Network issue: ${e.message}\n"
        CoroutineScope(Dispatchers.Main).launch {
            Toast.makeText(context, R.string.network_exception_occurred, Toast.LENGTH_SHORT).show()
        }
    } catch (e: Exception) {
        logState.value += "Unknown problem occurred: ${e.message}\n"
        CoroutineScope(Dispatchers.Main).launch {
            Toast.makeText(context, R.string.error_occurred, Toast.LENGTH_SHORT).show()
        }
    } finally {
        downloader.shutdown()
    }
}