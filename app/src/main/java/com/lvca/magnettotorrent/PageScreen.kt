package com.lvca.magnettotorrent

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.lvca.magnettotorrent.screens.MagnetToTorrentScreen
import com.lvca.magnettotorrent.screens.MenuScreen
import com.lvca.magnettotorrent.screens.Routes
import com.lvca.magnettotorrent.screens.TorrentToMagnetScreen
import com.lvca.magnettotorrent.ui.theme.DarkGreen
import kotlinx.coroutines.launch

@Composable
fun PagerScreen(
    navController: NavHostController,
    viewModel: MainViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    val pagerPages = listOf(Routes.MTT, Routes.MENU, Routes.TTM)

    val pagerState = rememberPagerState(
        initialPage = viewModel.getPageIndexForRoute(viewModel.initialRoute.value)
    ) {
        pagerPages.size
    }

    LaunchedEffect(Unit) {
        viewModel.navigationEvents.collect { event ->
            when (event) {
                is MainViewModel.NavigationEvent.NavigateToPagerPage -> {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(event.pageIndex)
                    }
                }
                is MainViewModel.NavigationEvent.NavigateTo -> navController.navigate(event.route)
                MainViewModel.NavigationEvent.PopBackStack -> navController.popBackStack()
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = DarkGreen
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            userScrollEnabled = true
        ) { pageIndex ->
            when (pagerPages[pageIndex]) {
                Routes.MTT -> {
                    MagnetToTorrentScreen(navController = navController, viewModel = viewModel)
                }
                Routes.MENU -> {
                    MenuScreen(navController = navController, viewModel = viewModel)
                }
                Routes.TTM -> {
                    TorrentToMagnetScreen(navController = navController, viewModel = viewModel)
                }
            }
        }
    }
}