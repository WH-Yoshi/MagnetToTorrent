package com.lvca.magnettotorrent

import android.content.Context
import android.net.http.NetworkException
import android.os.Build
import android.os.Environment
import android.widget.Toast
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.MutableState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException

suspend fun convertMagnetToTorrent(
    context: Context,
    magnetLink: String,
    logState: MutableList<String>,
    logsTitle: MutableState<String>
) {
    val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    val torrentsDir = File(downloadsDir, "Torrents")

    if (!torrentsDir.exists()) {
        torrentsDir.mkdirs()
    }

    val downloader = TorrentDownloader()

    try {
        logsTitle.value = context.getString(R.string.loading)
        downloader.fetchAndSaveMagnet(context, magnetLink, torrentsDir, logState)
    } catch (e: IOException) {
        logState.add(0, context.getString(R.string.file_operation_problem) + e.message)
        withContext(Dispatchers.Main) {
            Toast.makeText(context, R.string.io_exception_occurred, Toast.LENGTH_SHORT).show()
        }
    } catch (e: NetworkException) {
        logState.add(0, context.getString(R.string.network_issue) + e.message)
        withContext(Dispatchers.Main) {
            Toast.makeText(context, R.string.network_exception_occurred, Toast.LENGTH_SHORT).show()
        }
    } catch (e: Exception) {
        logState.add(0, context.getString(R.string.unknown_problem_occurred) + e.message)
        withContext(Dispatchers.Main) {
            Toast.makeText(context, R.string.error_occurred, Toast.LENGTH_SHORT).show()
        }
    } finally {
        downloader.shutdown()
        logsTitle.value = context.getString(R.string.logs)
    }
}
