package com.lvca.magnettotorrent

import androidx.compose.runtime.Composable
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

@Composable
fun NavScreen(
    navController: NavHostController,
    viewModel: MainViewModel,
    initialOffset: Float,
) {
    val actualStartDestination = when (viewModel.initialRoute.value) {
        Routes.MTT, Routes.MENU, Routes.TTM -> Routes.MAIN_PAGER_CONTAINER
        else -> viewModel.initialRoute.value
    }

    NavHost(
        navController = navController,
        startDestination = actualStartDestination
    ) {
        composable(
            route = Routes.MAIN_PAGER_CONTAINER,
            enterTransition = {
                materialSharedAxisXIn(
                    initialOffsetX = { toTheLeft(it, initialOffset) },
                )
            },
            exitTransition = {
                materialSharedAxisXOut(
                    targetOffsetX = { toTheLeft(it, initialOffset) },
                )
            },
        ) {
            PagerScreen(navController, viewModel)
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
                    targetOffsetX = { toTheRight(it, initialOffset) },
                )
            },
        ) {
            SettingsScreen(navController, viewModel)
        }
    }
}
