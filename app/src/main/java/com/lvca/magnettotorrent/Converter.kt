package com.lvca.magnettotorrent

import android.content.Context
import android.net.http.NetworkException
import android.os.Build
import android.os.Environment
import android.widget.Toast
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.lvca.magnettotorrent.FileUtil.getExternalDownloadDirectory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
suspend fun convertMagnetToTorrent(
    context: Context,
    magnetLink: String,
    logState: MutableList<String>,
    logsTitle: MutableState<String>
) {
    val outputDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    val downloader = TorrentDownloader()

    try {
        logsTitle.value = context.getString(R.string.loading)
        downloader.fetchAndSaveMagnet(context, magnetLink, outputDir, logState)
    } catch (e: IOException) {
        logState.add(0, context.getString(R.string.file_operation_problem) + e.message)
        CoroutineScope(Dispatchers.Main).launch {
            Toast.makeText(context, R.string.io_exception_occurred, Toast.LENGTH_SHORT).show()
        }
    } catch (e: NetworkException) {
        logState.add(0,context.getString(R.string.network_issue) + e.message)
        CoroutineScope(Dispatchers.Main).launch {
            Toast.makeText(context, R.string.network_exception_occurred, Toast.LENGTH_SHORT).show()
        }
    } catch (e: Exception) {
        logState.add(0,context.getString(R.string.unknown_problem_occurred) + e.message)
        CoroutineScope(Dispatchers.Main).launch {
            Toast.makeText(context, R.string.error_occurred, Toast.LENGTH_SHORT).show()
        }
    } finally {
        downloader.shutdown()
        logsTitle.value = context.getString(R.string.logs)
    }
}