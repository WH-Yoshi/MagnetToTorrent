package com.lvca.magnettotorrent

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.lvca.magnettotorrent.motions.materialSharedAxisXIn
import com.lvca.magnettotorrent.motions.materialSharedAxisXOut
import com.lvca.magnettotorrent.motions.toTheLeft
import com.lvca.magnettotorrent.motions.toTheRight
import com.lvca.magnettotorrent.screens.MagnetToTorrentScreen
import com.lvca.magnettotorrent.screens.MenuScreen
import com.lvca.magnettotorrent.screens.Routes
import com.lvca.magnettotorrent.screens.SettingsScreen
import com.lvca.magnettotorrent.screens.TorrentToMagnetScreen
import kotlinx.coroutines.CoroutineScope

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun NavScreen(
    navController: NavHostController,
    coroutineScope: CoroutineScope,
    magnetLink: MutableState<String>?,
    torrentFileName: MutableState<String>?,
    initialRoute: String,
    initialOffset: Float,
) {
    NavHost(
        navController = navController,
        startDestination = initialRoute
    ) {
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
            TorrentToMagnetScreen(navController, coroutineScope, torrentFileName!!)
        }
    }
}
