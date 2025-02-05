package com.lvca.magnettotorrent

import android.content.Context
import android.widget.Toast
import com.frostwire.jlibtorrent.SessionManager
import com.frostwire.jlibtorrent.TorrentInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class TorrentDownloader {

    private val sessionManager = SessionManager()
    private val triedMagnetLinks = mutableSetOf<String>()

    init {
        sessionManager.start()
    }

    suspend fun fetchAndSaveMagnet(
        context: Context,
        magnetUri: String,
        outputDir: File,
        logState: MutableList<String>
    ) {
        val timeout: Int
        val timeoutLog: String

        if (triedMagnetLinks.contains(magnetUri)) {
            timeout = 300
            timeoutLog = context.getString(R.string.with_longer_timeout_of_5mins)
        } else {
            timeout = 60
            timeoutLog = context.getString(R.string.for_a_maximum_of_1min)
        }

        logState.add(0,context.getString(R.string.fetching_magnet_metadata) + " " + timeoutLog)
        val metadata = sessionManager.fetchMagnet(magnetUri, timeout)
        if (metadata == null) {
            triedMagnetLinks.add(magnetUri)
            if (timeout == 60) {
                logState.add(0,context.getString(R.string.unable_to_fetch_torrent_data))
            } else {
                logState.add(context.getString(R.string.change_link))
            }
        }

        logState.add(0,context.getString(R.string.fetching_torrent_metadata))
        val torrentInfo = TorrentInfo(metadata)
        val torrentName = torrentInfo.name() ?: context.getString(R.string.unknown)
        val torrentFile = File(outputDir, torrentName.replace("\n", "") + ".torrent")

        if (torrentFile.exists()) {
            logState.add(0,context.getString(R.string.torrent_file_already_exists))
            withContext(Dispatchers.Main) {
                Toast.makeText(context, R.string.torrent_file_already_exists, Toast.LENGTH_SHORT).show()
            }
            return
        }

        try {
            torrentFile.writeBytes(metadata)
        } catch (e: Exception) {
            logState.add(0, context.getString(R.string.error_saving_file) + e.message)
            withContext(Dispatchers.Main) {
                Toast.makeText(context, R.string.error_saving_file, Toast.LENGTH_SHORT).show()
            }
            return
        }
        logState.add(0,context.getString(R.string.saved_torrent_file_to) + " " + torrentFile.absolutePath)

        withContext(Dispatchers.Main) {
            Toast.makeText(context, R.string.torrent_file_saved, Toast.LENGTH_SHORT).show()
        }
    }

    fun shutdown() {
        sessionManager.stop()
    }
}