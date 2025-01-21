package com.lvca.magnettotorrent

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.MutableState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun insertLastCopiedMagnetLink(context: Context, magnetLink: MutableState<String>) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clipData = clipboard.primaryClip
    if (clipData == null || clipData.itemCount == 0) {
        CoroutineScope(Dispatchers.Main).launch {
            Toast.makeText(context, context.getString(R.string.clipboard_is_empty), Toast.LENGTH_SHORT).show()
        }
        return
    }
    getMagnetLinkFromClipboard(context, clipData, magnetLink)
    if (magnetLink.value.isEmpty()) {
        CoroutineScope(Dispatchers.Main).launch {
            Toast.makeText(context, context.getString(R.string.no_valid_magnet_link_found_in_clipboard), Toast.LENGTH_SHORT).show()
        }
    }
}

fun getMagnetLinkFromClipboard(context: Context, clipData: ClipData, magnetLink: MutableState<String>) {
    try {
        val copiedText = clipData.getItemAt(0).text ?: return
        if (copiedText.startsWith("magnet:?xt=urn")) {
            magnetLink.value = copiedText.toString()
        }
    } catch (e: Exception) {
        CoroutineScope(Dispatchers.Main).launch {
            Toast.makeText(context, context.getString(R.string.no_valid_magnet_link_found_in_clipboard), Toast.LENGTH_SHORT).show()
        }
    }
}