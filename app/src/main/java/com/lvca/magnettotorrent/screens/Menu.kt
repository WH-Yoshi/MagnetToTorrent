package com.lvca.magnettotorrent.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.lvca.magnettotorrent.R
import com.lvca.magnettotorrent.components.MenuItem
import com.lvca.magnettotorrent.ui.theme.UbuntuFontFamily
import com.lvca.magnettotorrent.ui.theme.md_theme_light_onTertiary
import com.lvca.magnettotorrent.ui.theme.md_theme_light_tertiary
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    navController: NavController,
    coroutineScope: CoroutineScope,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = stringResource(R.string.back_icon_button),
                        tint = md_theme_light_onTertiary,
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .clickable {
                                coroutineScope.launch {
                                    navController.popBackStack()
                                }
                            }
                    )
                },
                title = {
                    Text(
                        text = stringResource(R.string.menu),
                        color = md_theme_light_onTertiary,
                        fontFamily = UbuntuFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 23.sp,
                        modifier = Modifier
                            .padding(start = 16.dp),

                    )
                },
                actions = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_settings),
                        contentDescription = stringResource(R.string.settings_icon_button),
                        tint = md_theme_light_onTertiary,
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .clickable {
                                coroutineScope.launch {
                                    navController.navigate(Routes.SETTINGS)
                                }
                            }
                    )
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = md_theme_light_tertiary
                )
            )
        },
        containerColor = md_theme_light_tertiary,
    ) {
        LazyColumn(
            modifier = Modifier.padding(it)
        ) {
            item {
                MenuItem(
                    title = stringResource(R.string.magnet_to_torrent),
                    description = stringResource(R.string.magnet_to_torrent_description),
                ) {
                    navController.navigate(Routes.MTT)
                }
            }
            item {
                MenuItem(
                    title = stringResource(R.string.torrent_to_magnet),
                    description = stringResource(R.string.torrent_to_magnet_description),
                ) {
                    navController.navigate(Routes.TTM)
                }
            }
            item {
                MenuItem(
                    title = "Soon to come...",
                    description = "Patience is a virtue",
                ) {
                    navController.navigate(Routes.MENU)
                }
            }
        }
    }
}

@Preview
@Composable
fun MenuScreenPreview() {
    MenuScreen(
        navController = NavController(context = LocalContext.current),
        coroutineScope = CoroutineScope(Dispatchers.IO),
    )
}