package com.lvca.magnettotorrent

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.lvca.magnettotorrent.screens.MTTViewModel
import com.lvca.magnettotorrent.screens.MagnetToTorrentScreen
import com.lvca.magnettotorrent.screens.MenuScreen
import com.lvca.magnettotorrent.screens.Routes
import com.lvca.magnettotorrent.screens.TTMViewModel
import com.lvca.magnettotorrent.screens.TorrentToMagnetScreen
import com.lvca.magnettotorrent.ui.theme.DarkGreen
import kotlinx.coroutines.launch

@Composable
fun PagerScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    val mttViewModel: MTTViewModel = viewModel()
    val ttmViewModel: TTMViewModel = viewModel()
    
    val pagerPages = listOf(Routes.MTT, Routes.MENU, Routes.TTM)

    val pagerState = rememberPagerState(
        initialPage = mainViewModel.getPageIndexForRoute(mainViewModel.initialRoute.value)
    ) {
        pagerPages.size
    }

    LaunchedEffect(Unit) {
        mainViewModel.navigationEvents.collect { event ->
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

    // Listen for incoming magnet links from MainViewModel
    LaunchedEffect(Unit) {
        mainViewModel.incomingMagnetLink.collect { link ->
            mttViewModel.setMagnetLink(link)
        }
    }

    // Listen for incoming torrent URIs from MainViewModel
    LaunchedEffect(Unit) {
        mainViewModel.incomingTorrentUri.collect { uri ->
            ttmViewModel.setTorrentFileUri(uri)
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
                    MagnetToTorrentScreen(
                        navController = navController, 
                        mainViewModel = mainViewModel,
                        mttViewModel = mttViewModel
                    )
                }
                Routes.MENU -> {
                    MenuScreen(navController = navController, viewModel = mainViewModel)
                }
                Routes.TTM -> {
                    TorrentToMagnetScreen(
                        navController = navController,
                        mainViewModel = mainViewModel,
                        ttmViewModel = ttmViewModel
                    )
                }
            }
        }
    }
}