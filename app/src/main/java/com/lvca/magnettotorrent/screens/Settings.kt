package com.lvca.magnettotorrent.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.lvca.magnettotorrent.MainViewModel
import com.lvca.magnettotorrent.R
import com.lvca.magnettotorrent.components.SettingItem
import com.lvca.magnettotorrent.ui.theme.DarkGreen
import com.lvca.magnettotorrent.ui.theme.UbuntuFontFamily
import com.lvca.magnettotorrent.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: MainViewModel
) {
    LaunchedEffect(Unit) {
        viewModel.navigationEvents.collect { event ->
            when (event) {
                is MainViewModel.NavigationEvent.NavigateTo -> navController.navigate(event.route)
                is MainViewModel.NavigationEvent.PopBackStack -> navController.popBackStack()
                is MainViewModel.NavigationEvent.NavigateToPagerPage -> {  }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.onBackButtonPressed() },
                        modifier = Modifier
                            .padding(end = 12.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_left),
                            contentDescription = stringResource(id = R.string.back_icon_button),
                            tint = White,
                            modifier = Modifier
                        )
                    }
                },
                title = { },
                actions = {  },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = DarkGreen
                )
            )
        },
        containerColor = DarkGreen,
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(DarkGreen),
            color = DarkGreen
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.settings),
                        color = White,
                        fontFamily = UbuntuFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp,
                        modifier = Modifier
                            .padding(bottom = 32.dp)
                    )
                    SettingItem(
                        title = stringResource(id = R.string.notification),
                        description = stringResource(id = R.string.allow_notification),
                        icon = painterResource(id = R.drawable.ic_info),
                        onClick = {
                            viewModel.navigateToPagerPage(viewModel.getPageIndexForRoute(Routes.ABOUT))
                        }
                    )
                    SettingItem(
                        title = stringResource(id = R.string.settings_about_title),
                        description = stringResource(id = R.string.settings_about_description),
                        icon = painterResource(id = R.drawable.ic_info),
                        onClick = {
                            viewModel.navigateToPagerPage(viewModel.getPageIndexForRoute(Routes.ABOUT))
                        }
                    )
                    SettingItem(
                        title = stringResource(id = R.string.settings_about_title),
                        description = stringResource(id = R.string.settings_about_description),
                        icon = painterResource(id = R.drawable.ic_info),
                        onClick = {
                            viewModel.navigateToPagerPage(viewModel.getPageIndexForRoute(Routes.ABOUT))
                        }
                    )
                    SettingItem(
                        title = stringResource(id = R.string.settings_about_title),
                        description = stringResource(id = R.string.settings_about_description),
                        icon = painterResource(id = R.drawable.ic_info),
                        onClick = {
                            viewModel.navigateToPagerPage(viewModel.getPageIndexForRoute(Routes.ABOUT))
                        }
                    )
                }
            }
        }
    }
}