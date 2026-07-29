package com.lvca.magnettotorrent

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.lvca.magnettotorrent.motions.materialSharedAxisXIn
import com.lvca.magnettotorrent.motions.materialSharedAxisXOut
import com.lvca.magnettotorrent.motions.toTheLeft
import com.lvca.magnettotorrent.motions.toTheRight
import com.lvca.magnettotorrent.screens.AboutScreen
import com.lvca.magnettotorrent.screens.Routes
import com.lvca.magnettotorrent.screens.SettingsScreen

@Composable
fun NavScreen(
    navController: NavHostController,
    viewModel: MainViewModel,
    initialOffset: Float,
) {
    val actualStartDestination = when (viewModel.initialRoute.value) {
        Routes.MTT, Routes.MENU -> Routes.MAIN_PAGER_CONTAINER
        else -> viewModel.initialRoute.value
    }

    NavHost(
        navController = navController,
        startDestination = actualStartDestination
    ) {
        animatedComposable(Routes.MAIN_PAGER_CONTAINER, initialOffset) {
            PagerScreen(navController, viewModel)
        }

        animatedComposable(Routes.SETTINGS, initialOffset) {
            SettingsScreen(navController, viewModel)
        }

        animatedComposable(Routes.ABOUT, initialOffset) {
            AboutScreen(navController, viewModel)
        }
    }
}

private fun NavGraphBuilder.animatedComposable(
    route: String,
    initialOffset: Float,
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) {
    composable(
        route = route,
        enterTransition = {
            materialSharedAxisXIn(initialOffsetX = { toTheLeft(it, initialOffset) })
        },
        exitTransition = {
            materialSharedAxisXOut(targetOffsetX = { toTheRight(it, initialOffset) })
        },
        popEnterTransition = {
            materialSharedAxisXIn(initialOffsetX = { toTheRight(it, initialOffset) })
        },
        popExitTransition = {
            materialSharedAxisXOut(targetOffsetX = { toTheLeft(it, initialOffset) })
        },
        content = content
    )
}
