package com.lvca.magnettotorrent

import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.MutableState

fun insertLastCopiedMagnetLink(context: Context, magnetLink: MutableState<String>) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clipData = clipboard.primaryClip
    if (clipData != null && clipData.itemCount > 0) {
        val copiedText = clipData.getItemAt(0).text
        if (copiedText != null && copiedText.startsWith("magnet:")) {
            magnetLink.value = copiedText.toString()
        } else {
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