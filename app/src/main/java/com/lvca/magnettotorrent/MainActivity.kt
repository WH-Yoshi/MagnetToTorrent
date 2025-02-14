package com.lvca.magnettotorrent

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.lvca.magnettotorrent.screens.Routes
import com.lvca.magnettotorrent.ui.theme.MagnetToTorrentTheme
import com.lvca.magnettotorrent.ui.theme.md_theme_light_tertiary

class MainActivity : ComponentActivity() {
    private val magnetLink = mutableStateOf("")
    private val torrentFileName = mutableStateOf("")
    private var initialRoute = Routes.MTT

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        handleIntent(intent)
        setContent {
            MagnetToTorrentTheme {
                MagnetToTorrentApp(magnetLink, torrentFileName, initialRoute)
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        if (intent.action == Intent.ACTION_VIEW) {
            val dataString = intent.dataString
            if (dataString != null) {
                when {
                    dataString.startsWith("magnet:") -> {
                        magnetLink.value = dataString
                        initialRoute = Routes.MTT
                    }
                    dataString.endsWith(".torrent") -> {
                        torrentFileName.value = dataString
                        initialRoute = Routes.TTM
                    }
                }
            }
        }
    }
}

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun MagnetToTorrentApp(
    magnetLink: MutableState<String>?,
    torrentFileName: MutableState<String>?,
    initialRoute: String
) {
    val navController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()
    val initialOffset = 0.10f

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = md_theme_light_tertiary
    ) {
        NavScreen(
            navController = navController,
            coroutineScope = coroutineScope,
            magnetLink = magnetLink,
            torrentFileName = torrentFileName,
            initialRoute = initialRoute,
            initialOffset = initialOffset
        )
    }
}