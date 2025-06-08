package com.lvca.magnettotorrent.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.lvca.magnettotorrent.MainViewModel
import com.lvca.magnettotorrent.R
import com.lvca.magnettotorrent.components.MenuItem
import com.lvca.magnettotorrent.ui.theme.UbuntuFontFamily
import com.lvca.magnettotorrent.ui.theme.White
import com.lvca.magnettotorrent.ui.theme.DarkGreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    navController: NavController,
    viewModel: MainViewModel
) {
    LaunchedEffect(Unit) {
        viewModel.navigationEvents.collect { event ->
            when (event) {
                is MainViewModel.NavigationEvent.NavigateTo -> navController.navigate(event.route)
                MainViewModel.NavigationEvent.PopBackStack -> navController.popBackStack()
                is MainViewModel.NavigationEvent.NavigateToPagerPage -> {  }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {  },
                title = {
                    Text(
                        text = stringResource(R.string.menu),
                        color = White,
                        fontFamily = UbuntuFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 27.sp
                    )
                },
                actions = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_settings),
                        contentDescription = stringResource(R.string.settings_icon_button),
                        tint = White,
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .clickable {
                                viewModel.navigate(Routes.SETTINGS)
                            }
                    )
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = DarkGreen
                )
            )
        },
        containerColor = DarkGreen,
    ) {
        LazyColumn(
            modifier = Modifier.padding(it)
        ) {
            item {
                MenuItem(
                    title = stringResource(R.string.magnet_to_torrent),
                    description = stringResource(R.string.magnet_to_torrent_description),
                    direction = "left"
                ) {
                    viewModel.navigateToPagerPage(viewModel.getPageIndexForRoute(Routes.MTT))
                }
            }
            item {
                MenuItem(
                    title = stringResource(R.string.torrent_to_magnet),
                    description = stringResource(R.string.torrent_to_magnet_description),
                    direction = "right"
                ) {
                    viewModel.navigateToPagerPage(viewModel.getPageIndexForRoute(Routes.TTM))
                }
            }
        }
    }
}