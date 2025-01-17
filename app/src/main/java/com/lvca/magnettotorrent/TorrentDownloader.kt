package com.lvca.magnettotorrent

import androidx.compose.runtime.MutableState
import com.frostwire.jlibtorrent.SessionManager
import com.frostwire.jlibtorrent.TorrentInfo
import java.io.File

class TorrentDownloader {

    private val sessionManager = SessionManager()

    init {
        sessionManager.start()
    }

    fun fetchAndSaveMagnet(magnetUri: String, outputDir: File, logState: MutableState<String>): Int {

        logState.value += "Fetching magnet metadata...\n"
        val metadata = sessionManager.fetchMagnet(magnetUri, 30)
        if (metadata == null) {
            logState.value += R.string.unable_to_fetch_torrent_data
            return R.string.unable_to_fetch_torrent_data
        }

        logState.value += "Fetching torrent metadata...\n"
        val torrentInfo = TorrentInfo(metadata)
        val torrentName = torrentInfo.name() ?: "unknown"
        val torrentFile = File(outputDir, "$torrentName.torrent")

        if (torrentFile.exists()) {
            return R.string.torrent_file_already_exists
        }

        logState.value += "Saving torrent file...\n"
        torrentFile.writeBytes(metadata)
        logState.value += "Saved torrent file to ${torrentFile.absolutePath}\n"

        return R.string.torrent_file_saved
    }

    fun shutdown() {
        sessionManager.stop()
    }
}