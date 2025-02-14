package com.lvca.magnettotorrent.screens

import android.app.DownloadManager
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresExtension
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.lvca.magnettotorrent.R
import com.lvca.magnettotorrent.components.LogsScreen
import com.lvca.magnettotorrent.convertMagnetToTorrent
import com.lvca.magnettotorrent.insertLastCopiedMagnetLink
import com.lvca.magnettotorrent.ui.theme.UbuntuFontFamily
import com.lvca.magnettotorrent.ui.theme.md_theme_light_onTertiary
import com.lvca.magnettotorrent.ui.theme.md_theme_light_tertiary
import com.lvca.magnettotorrent.ui.theme.md_theme_light_tertiaryContainer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MagnetToTorrentScreen(
    navController: NavController,
    coroutineScope: CoroutineScope,
    magnetLink: MutableState<String>
) {
    val context = LocalContext.current
    val imeInsets = WindowInsets.ime
    val logsState = remember { mutableStateListOf<String>() }
    val logsTitle = remember { mutableStateOf(context.getString(R.string.logs)) }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_menu),
                        contentDescription = stringResource(R.string.menu_icon),
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
                        text = stringResource(R.string.magnet_to_torrent),
                        color = md_theme_light_onTertiary,
                        fontFamily = UbuntuFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 25.sp,
                        modifier = Modifier
                            .padding(top = 16.dp, bottom = 32.dp)
                    )
                    Text(
                        text = stringResource(R.string.enter_or_paste_a_magnet_link_to_create_a_torrent_file),
                        color = md_theme_light_onTertiary,
                        fontFamily = UbuntuFontFamily,
                        fontWeight = FontWeight.Light,
                        fontSize = 14.sp
                    )
                    OutlinedTextField(
                        value = magnetLink.value,
                        onValueChange = { newValue ->
                            magnetLink.value = newValue
                        },
                        label = { Text(stringResource(R.string.magnet_link), fontFamily = UbuntuFontFamily) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = md_theme_light_tertiary,
                            unfocusedContainerColor = md_theme_light_tertiary,
                            focusedTextColor = md_theme_light_onTertiary,
                            unfocusedTextColor = md_theme_light_onTertiary,
                            focusedLabelColor = md_theme_light_onTertiary,
                            unfocusedLabelColor = md_theme_light_onTertiary,
                            focusedIndicatorColor = md_theme_light_onTertiary,
                            unfocusedIndicatorColor = md_theme_light_onTertiary,
                        ),
                        singleLine = true,
                        trailingIcon = {
                            AnimatedVisibility(
                                visible = magnetLink.value.isNotEmpty(),
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
                                    tint = md_theme_light_onTertiary,
                                    modifier = Modifier
                                        .clickable {
                                            magnetLink.value = ""
                                            logsState.clear()
                                        }
                                )
                            }
                        },
                    )
                    HorizontalDivider(Modifier.padding(top = 16.dp), color = Color.Gray)
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
                            insertLastCopiedMagnetLink(context = context, magnetLink = magnetLink)
                        },
                        containerColor = md_theme_light_tertiaryContainer,
                        modifier = Modifier
                            .align(Alignment.End)
                            .wrapContentSize()
                            .padding(bottom = 16.dp),
                        content = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_paste),
                                contentDescription = stringResource(R.string.copy),
                                tint = md_theme_light_tertiary
                            )
                        }
                    )
                    FloatingActionButton(
                        onClick = {
                            if (magnetLink.value.isEmpty()) {
                                Toast.makeText(context, R.string.magnet_link_is_empty, Toast.LENGTH_SHORT).show()
                                return@FloatingActionButton
                            }
                            CoroutineScope(Dispatchers.IO).launch {
                                convertMagnetToTorrent(context, magnetLink.value, logsState, logsTitle)
                            }
                        },
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(bottom = 16.dp)
                            .wrapContentSize(),
                        containerColor = md_theme_light_tertiaryContainer,
                        content = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_sync),
                                contentDescription = stringResource(R.string.convert),
                                tint = md_theme_light_tertiary
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
                        containerColor = md_theme_light_tertiaryContainer,
                        icon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_folder),
                                contentDescription = stringResource(R.string.open_folder),
                                tint = md_theme_light_tertiary
                            )
                        },
                        text = {
                            Text(
                                text = stringResource(R.string.download_folder),
                                color = md_theme_light_tertiary,
                                fontFamily = UbuntuFontFamily
                            )
                        }
                    )
                }
            }
        }
    }
}

@Preview
@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun MagnetToTorrentScreenPreview() {
    MagnetToTorrentScreen(
        navController = NavController(context = LocalContext.current),
        magnetLink = mutableStateOf(""),
        coroutineScope = CoroutineScope(Dispatchers.IO)
    )
}