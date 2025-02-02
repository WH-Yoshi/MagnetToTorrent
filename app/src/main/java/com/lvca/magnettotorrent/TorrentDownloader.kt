package com.lvca.magnettotorrent

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.MutableState
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
        logState: MutableState<String>?
    ) {
        val timeout: Int
        val timeoutLog: String

        if (triedMagnetLinks.contains(magnetUri)) {
            timeout = 300
            timeoutLog = "with longer timeout of 5mins...\n"
        } else {
            timeout = 60
            timeoutLog = "for a maximum of 1min...\n"
        }

        logState!!.value += "Fetching magnet metadata $timeoutLog"
        val metadata = sessionManager.fetchMagnet(magnetUri, timeout)
        if (metadata == null) {
            triedMagnetLinks.add(magnetUri)
            if (timeout == 60) {
                logState!!.value += R.string.unable_to_fetch_torrent_data
            } else {
                logState!!.value += R.string.change_link
            }
        }

        logState!!.value += "Fetching torrent metadata...\n"
        val torrentInfo = TorrentInfo(metadata)
        val torrentName = torrentInfo.name() ?: "unknown"
        val torrentFile = File(outputDir, "$torrentName.torrent")

        if (torrentFile.exists()) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, R.string.torrent_file_already_exists, Toast.LENGTH_SHORT).show()
            }
            return
        }

        logState!!.value += "Saving torrent file...\n"
        torrentFile.writeBytes(metadata)
        logState!!.value += "Saved torrent file to ${torrentFile.absolutePath}\n"

        withContext(Dispatchers.Main) {
            Toast.makeText(context, R.string.torrent_file_saved, Toast.LENGTH_SHORT).show()
        }
    }

    fun shutdown() {
        sessionManager.stop()
    }
}