package com.lvca.magnettotorrent

import android.content.Intent
import android.net.Uri
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
import androidx.core.net.toUri
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lvca.magnettotorrent.motions.materialSharedAxisXIn
import com.lvca.magnettotorrent.motions.materialSharedAxisXOut
import com.lvca.magnettotorrent.motions.toTheLeft
import com.lvca.magnettotorrent.motions.toTheRight
import com.lvca.magnettotorrent.screens.MagnetToTorrentScreen
import com.lvca.magnettotorrent.screens.MenuScreen
import com.lvca.magnettotorrent.screens.Routes
import com.lvca.magnettotorrent.screens.SettingsScreen
import com.lvca.magnettotorrent.screens.TorrentToMagnetScreen
import com.lvca.magnettotorrent.ui.theme.MagnetToTorrentTheme
import com.lvca.magnettotorrent.ui.theme.md_theme_light_tertiary

class MainActivity : ComponentActivity() {
    private val magnetLink = mutableStateOf("")
    private val torrentFile = mutableStateOf<Uri>(Uri.EMPTY)
    private var initialRoute = Routes.MTT

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        handleIntent(intent)
        setContent {
            MagnetToTorrentTheme {
                MagnetToTorrentApp(magnetLink, torrentFile, initialRoute)
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
                        torrentFile.value = dataString.toUri()
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
    torrentFile: MutableState<Uri>,
    initialRoute: String
) {
    val navController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()
    val initialOffset = 0.10f

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = md_theme_light_tertiary
    ) {
        NavHost(navController = navController, startDestination = initialRoute) {
            composable(
                route = Routes.MTT,
                enterTransition = {
                    materialSharedAxisXIn(
                        initialOffsetX = { toTheRight(it, initialOffset) },
                    )
                },
                exitTransition = {
                    materialSharedAxisXOut(
                        targetOffsetX = { toTheLeft(it, initialOffset) },
                    )
                },
            ) {
                MagnetToTorrentScreen(navController, coroutineScope, magnetLink!!)
            }
            composable(
                route = Routes.SETTINGS,
                enterTransition = {
                    materialSharedAxisXIn(
                        initialOffsetX = { toTheRight(it, initialOffset) },
                    )
                },
                exitTransition = {
                    materialSharedAxisXOut(
                        targetOffsetX = { toTheLeft(it, initialOffset) },
                    )
                },
            ) {
                SettingsScreen(navController, coroutineScope)
            }
            composable(
                route = Routes.MENU,
                enterTransition = {
                    materialSharedAxisXIn(
                        initialOffsetX = { toTheRight(it, initialOffset) },
                    )
                },
                exitTransition = {
                    materialSharedAxisXOut(
                        targetOffsetX = { toTheLeft(it, initialOffset) },
                    )
                },
            ) {
                MenuScreen(navController, coroutineScope)
            }
            composable(
                route = Routes.TTM,
                enterTransition = {
                    materialSharedAxisXIn(
                        initialOffsetX = { toTheRight(it, initialOffset) },
                    )
                },
                exitTransition = {
                    materialSharedAxisXOut(
                        targetOffsetX = { toTheLeft(it, initialOffset) },
                    )
                },
            ) {
                TorrentToMagnetScreen(navController, coroutineScope, torrentFile)
            }
        }
    }
}