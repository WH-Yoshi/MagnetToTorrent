package com.lvca.magnettotorrent

import android.os.Environment
import androidx.compose.runtime.MutableState

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
        downloader.fetchAndSaveMagnet(magnetLink, outputDir, logState).let {
            return it
        }
    } catch (e: Exception) {
        println(e)
        return R.string.error_occurred
    } finally {
        downloader.shutdown()
    }
}