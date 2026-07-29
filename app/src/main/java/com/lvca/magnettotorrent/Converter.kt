package com.lvca.magnettotorrent

import android.content.Context
import android.net.Uri
import android.net.http.NetworkException
import android.os.Build
import android.os.Environment
import android.widget.Toast
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.MutableState
import com.frostwire.jlibtorrent.TorrentInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
suspend fun convertMagnetToTorrent(
    context: Context,
    magnetLink: String,
    logState: MutableList<String>,
    logsTitle: MutableState<String>
) {
    val downloadsDir =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
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

suspend fun convertTorrentToMagnet(
    context: Context,
    torrentUri: Uri,
    logState: MutableList<String>,
    logsTitle: MutableState<String>
) {
    try {
        logsTitle.value = context.getString(R.string.loading)
        logState.add(0, context.getString(R.string.reading_torrent_file))

        val inputStream = context.contentResolver.openInputStream(torrentUri)
        if (inputStream == null) {
            logState.add(0, context.getString(R.string.error_reading_file))
            return
        }

        val bytes = inputStream.readBytes()
        withContext(Dispatchers.IO) {
            inputStream.close()
        }

        val torrentInfo = TorrentInfo(bytes)
        val magnetLink = torrentInfo.makeMagnetUri()

        logState.add(0, context.getString(R.string.magnet_link_generated))
        logState.add(0, magnetLink)

        withContext(Dispatchers.Main) {
            val clipboard =
                context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
            val clip = android.content.ClipData.newPlainText("Magnet Link", magnetLink)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(context, R.string.magnet_link_copied_to_clipboard, Toast.LENGTH_SHORT)
                .show()
        }

    } catch (e: Exception) {
        logState.add(0, context.getString(R.string.error_occurred) + ": " + e.message)
        withContext(Dispatchers.Main) {
            Toast.makeText(context, R.string.error_occurred, Toast.LENGTH_SHORT).show()
        }
    } finally {
        logsTitle.value = context.getString(R.string.logs)
    }
}
