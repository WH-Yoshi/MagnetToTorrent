package com.lvca.magnettotorrent

import android.net.http.NetworkException
import android.os.Build
import android.os.Environment
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.MutableState
import java.io.IOException

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
fun convertMagnetToTorrent(magnetLink: String, logState: MutableState<String>): Int {
    if (magnetLink.isEmpty()) {
        return R.string.magnet_link_is_empty
    }

    val outputDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    if (!outputDir.exists()) {
        outputDir.mkdirs()
    }

    val downloader = TorrentDownloader()

    try {
        return downloader.fetchAndSaveMagnet(magnetLink, outputDir, logState)
    } catch (e: IOException) {
        logState.value += "File operation problem: ${e.message}\n"
        return R.string.io_exception_occurred
    } catch (e: NetworkException) {
        logState.value += "Network issue: ${e.message}\n"
        return R.string.network_exception_occurred
    } catch (e: Exception) {
        logState.value += "Unknown problem occurred: ${e.message}\n"
        return R.string.error_occurred
    } finally {
        downloader.shutdown()
    }
}