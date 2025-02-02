package com.lvca.magnettotorrent.screens

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.lvca.magnettotorrent.R
import com.lvca.magnettotorrent.ui.theme.UbuntuFontFamily
import com.lvca.magnettotorrent.ui.theme.md_theme_light_onTertiary
import com.lvca.magnettotorrent.ui.theme.md_theme_light_tertiary
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    coroutineScope: CoroutineScope
) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_menu),
                        contentDescription = "Menu Icon",
                        tint = md_theme_light_onTertiary,
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .clickable {
                                coroutineScope.launch {
                                    navController.navigate(Routes.MENU)
                                }
                            }
                    )
                },
                title = {  },
                actions = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_down),
                        contentDescription = "Settings Icon",
                        tint = md_theme_light_onTertiary,
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .clickable {
                                coroutineScope.launch {
                                    navController.popBackStack()
                                }
                            }
                    )
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = md_theme_light_tertiary
                )
            )
        },
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(md_theme_light_tertiary),
            color = md_theme_light_tertiary
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                ) {
                    Text(
                        text = "Settings",
                        color = md_theme_light_onTertiary,
                        fontFamily = UbuntuFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 25.sp,
                        modifier = Modifier
                            .padding(top = 16.dp, bottom = 32.dp)
                    )
                    Text(
                        text = "Coming soon...",
                        color = md_theme_light_onTertiary,
                        fontFamily = UbuntuFontFamily,
                        fontWeight = FontWeight.Light,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Preview
@Composable
fun MagnetToTorrentScreenPreview() {
    SettingsScreen(
        navController = NavController(context = LocalContext.current),
        coroutineScope = CoroutineScope(Dispatchers.IO),
    )
}