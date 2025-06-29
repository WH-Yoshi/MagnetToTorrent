package com.lvca.magnettotorrent.screens

import android.app.DownloadManager
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.lvca.magnettotorrent.MainViewModel
import com.lvca.magnettotorrent.R
import com.lvca.magnettotorrent.components.LogsScreen
import com.lvca.magnettotorrent.convertMagnetToTorrent
import com.lvca.magnettotorrent.insertLastCopiedMagnetLink
import com.lvca.magnettotorrent.ui.theme.DarkGreen
import com.lvca.magnettotorrent.ui.theme.LightGreen
import com.lvca.magnettotorrent.ui.theme.UbuntuFontFamily
import com.lvca.magnettotorrent.ui.theme.White
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MagnetToTorrentScreen(
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

    val context = LocalContext.current
    val imeInsets = WindowInsets.ime
    val logsState = remember { mutableStateListOf<String>() }
    val logsTitle = remember { mutableStateOf(context.getString(R.string.logs)) }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {  },
                title = {  },
                actions = {
                    IconButton(
                        onClick = {
                            viewModel.navigateToPagerPage(
                                viewModel.getPageIndexForRoute(
                                    Routes.MENU
                                )
                            )
                        },
                        modifier = Modifier
                            .padding(end = 12.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_right),
                            contentDescription = stringResource(R.string.arrow_right_icon),
                            tint = White,
                            modifier = Modifier
                        )
                    }
                },
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
                .padding(innerPadding),
            color = DarkGreen
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.magnet_to_torrent),
                        color = White,
                        fontFamily = UbuntuFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp,
                        modifier = Modifier
                            .padding(bottom = 32.dp)
                    )
                    Text(
                        text = stringResource(R.string.enter_or_paste_a_magnet_link),
                        color = White,
                        fontFamily = UbuntuFontFamily,
                        fontWeight = FontWeight.Light,
                        fontSize = 17.sp
                    )
                    OutlinedTextField(
                        value = viewModel.magnetLink.value,
                        onValueChange = { newValue ->
                            viewModel.magnetLink.value = newValue
                        },
                        label = { Text(stringResource(R.string.magnet_link), fontFamily = UbuntuFontFamily) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = DarkGreen,
                            unfocusedContainerColor = DarkGreen,
                            focusedTextColor = White,
                            unfocusedTextColor = White,
                            focusedLabelColor = White,
                            unfocusedLabelColor = White,
                            focusedIndicatorColor = White,
                            unfocusedIndicatorColor = White,
                        ),
                        singleLine = true,
                        trailingIcon = {
                            AnimatedVisibility(
                                visible = viewModel.magnetLink.value.isNotEmpty(),
                                enter = fadeIn(),
                                exit = fadeOut(),
                                modifier = Modifier
                                    .align(Alignment.End)
                                    .padding(end = 12.dp)
                                    .wrapContentSize())
                            {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_backspace),
                                    contentDescription = stringResource(R.string.full_backspace),
                                    tint = White,
                                    modifier = Modifier
                                        .clickable {
                                            viewModel.magnetLink.value = ""
                                            logsState.clear()
                                        }
                                )
                            }
                        },
                    )
                    HorizontalDivider(Modifier.padding(top = 16.dp, bottom = 16.dp), color = Color.Gray)
                    LogsScreen(logsState = logsState, logsTitle = logsTitle.value)
                }
                Column(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(
                            bottom = 16.dp + imeInsets.asPaddingValues().calculateBottomPadding(),
                            end = 16.dp
                        )
                        .align(Alignment.BottomEnd)
                ) {
                    FloatingActionButton(
                        onClick = {
                            insertLastCopiedMagnetLink(context = context, magnetLink = viewModel.magnetLink)
                        },
                        containerColor = LightGreen,
                        modifier = Modifier
                            .align(Alignment.End)
                            .wrapContentSize()
                            .padding(bottom = 16.dp),
                        content = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_paste),
                                contentDescription = stringResource(R.string.paste),
                                tint = DarkGreen
                            )
                        }
                    )
                    FloatingActionButton(
                        onClick = {
                            val magnet = viewModel.magnetLink.value.trim()
                            if (magnet.isEmpty() || !magnet.startsWith("magnet:?xt=urn:btih:")) {
                                Toast.makeText(context, R.string.invalid_magnet_link, Toast.LENGTH_SHORT).show()
                                return@FloatingActionButton
                            }

                            CoroutineScope(Dispatchers.IO).launch {
                                convertMagnetToTorrent(context, viewModel.magnetLink.value, logsState, logsTitle)
                            }
                        },
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(bottom = 16.dp)
                            .wrapContentSize(),
                        containerColor = LightGreen,
                        content = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_sync),
                                contentDescription = stringResource(R.string.convert),
                                tint = DarkGreen
                            )
                        }
                    )
                    ExtendedFloatingActionButton(
                        onClick = {
                            try {
                                context.startActivity(Intent(DownloadManager.ACTION_VIEW_DOWNLOADS))
                            } catch (e: Exception) {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.no_application_found_to_open_the_folder),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                        modifier = Modifier
                            .wrapContentSize(),
                        containerColor = LightGreen,
                        icon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_folder),
                                contentDescription = stringResource(R.string.open_folder),
                                tint = DarkGreen
                            )
                        },
                        text = {
                            Text(
                                text = stringResource(R.string.download_folder),
                                color = DarkGreen,
                                fontFamily = UbuntuFontFamily
                            )
                        }
                    )
                }
            }
        }
    }
}