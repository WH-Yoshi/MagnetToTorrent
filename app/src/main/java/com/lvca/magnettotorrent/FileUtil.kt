package com.lvca.magnettotorrent

import android.os.Environment
import java.io.File

object FileUtil {
    internal fun getExternalDownloadDirectory() = File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
        "MagnetToTorrent"
    ).also { it.mkdir() }
}