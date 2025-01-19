package com.lvca.magnettotorrent

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.MutableState

fun insertLastCopiedMagnetLink(context: Context, magnetLink: MutableState<String>) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clipData = clipboard.primaryClip
    if (clipData != null && clipData.itemCount > 0) {
        getMagnetLinkFromClipboard(clipData, magnetLink)
        if (magnetLink.value.isEmpty()) {
            Toast.makeText(
                context,
                context.getString(R.string.no_valid_magnet_link_found_in_clipboard),
                Toast.LENGTH_SHORT
            ).show()
        }
    } else {
        Toast.makeText(
            context,
            context.getString(R.string.clipboard_is_empty),
            Toast.LENGTH_SHORT
        ).show()
    }
}

fun getMagnetLinkFromClipboard(clipData: ClipData, magnetLink: MutableState<String>) {
    for (i in 0 until 2) {
        val copiedText = clipData.getItemAt(i).text
        if (copiedText != null && copiedText.startsWith("magnet:")) {
            magnetLink.value = copiedText.toString()
            return
        }
    }
}