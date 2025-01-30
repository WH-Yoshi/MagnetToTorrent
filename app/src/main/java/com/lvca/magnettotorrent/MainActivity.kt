package com.lvca.magnettotorrent

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresExtension
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lvca.magnettotorrent.motions.*
import com.lvca.magnettotorrent.screens.*
import com.lvca.magnettotorrent.ui.theme.MagnetToTorrentTheme

class MainActivity : ComponentActivity() {
    private val magnetLink = mutableStateOf("")

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MagnetToTorrentTheme {
                MagnetToTorrentApp(magnetLink)
            }
        }

        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        if (intent.action == Intent.ACTION_VIEW) {
            val magnetLinkValue = intent.dataString
            if (magnetLinkValue != null && magnetLinkValue.startsWith("magnet:")) {
                magnetLink.value = magnetLinkValue
            }
        }
    }
}


@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun MagnetToTorrentApp(magnetLink: MutableState<String>) {
    val navController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()
    val initialOffset = 0.10f

    NavHost(navController = navController, startDestination = "main") {
        composable(
            route = "main",
            enterTransition = {
                materialSharedAxisXIn(initialOffsetX = { -(it * initialOffset).toInt() })
            },
            exitTransition = {
                materialSharedAxisXOut(targetOffsetX = { (it * initialOffset).toInt() })
            },
        ) {
            MagnetToTorrentScreen(navController, magnetLink, coroutineScope)
        }
        composable(
            route = "second",
            enterTransition = {
                materialSharedAxisXIn(initialOffsetX = { (it * initialOffset).toInt() })
            },
            exitTransition = {
                materialSharedAxisXOut(targetOffsetX = { -(it * initialOffset).toInt() })
            },
        ) {
            SecondScreen()
        }
    }
}