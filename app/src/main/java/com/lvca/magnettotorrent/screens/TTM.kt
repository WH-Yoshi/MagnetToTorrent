package com.lvca.magnettotorrent.screens

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import com.lvca.magnettotorrent.ui.theme.DarkGreen
import com.lvca.magnettotorrent.ui.theme.LightGreen
import com.lvca.magnettotorrent.ui.theme.UbuntuFontFamily
import com.lvca.magnettotorrent.ui.theme.White

@SuppressLint("LocalContextGetResourceValueCall")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TorrentToMagnetScreen(
    navController: NavController,
    mainViewModel: MainViewModel,
    ttmViewModel: TTMViewModel
) {
    val context = LocalContext.current
    val imeInsets = WindowInsets.ime

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            ttmViewModel.setTorrentFileUri(it)
            ttmViewModel.clearLogs()
        }
    }

    LaunchedEffect(Unit) {
        ttmViewModel.initLogsTitle(context)
        mainViewModel.navigationEvents.collect { event ->
            when (event) {
                is MainViewModel.NavigationEvent.NavigateTo -> navController.navigate(event.route)
                MainViewModel.NavigationEvent.PopBackStack -> navController.popBackStack()
                is MainViewModel.NavigationEvent.NavigateToPagerPage -> {}
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = {
                            mainViewModel.navigateToPagerPage(
                                mainViewModel.getPageIndexForRoute(
                                    Routes.MENU
                                )
                            )
                        },
                        modifier = Modifier
                            .padding(start = 12.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_left),
                            contentDescription = stringResource(R.string.arrow_left_icon),
                            tint = White
                        )
                    }
                },
                title = { },
                actions = { },
                colors = TopAppBarDefaults.topAppBarColors(
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
                        text = stringResource(R.string.torrent_to_magnet),
                        color = White,
                        fontFamily = UbuntuFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp,
                        modifier = Modifier
                            .padding(bottom = 32.dp)
                    )
                    Text(
                        text = stringResource(R.string.upload_torrent_file),
                        color = White,
                        fontFamily = UbuntuFontFamily,
                        fontWeight = FontWeight.Light,
                        fontSize = 17.sp
                    )

                    OutlinedTextField(
                        value = ttmViewModel.torrentFileUri.value?.let { getFileName(context, it) } ?: "",
                        onValueChange = { },
                        readOnly = true,
                        label = {
                            Text(
                                stringResource(R.string.files),
                                fontFamily = UbuntuFontFamily
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                            .clickable { launcher.launch("application/x-bittorrent") },
                        enabled = false, 
                        colors = TextFieldDefaults.colors(
                            disabledContainerColor = DarkGreen,
                            disabledTextColor = White,
                            disabledLabelColor = White,
                            disabledIndicatorColor = White,
                            focusedContainerColor = DarkGreen,
                            unfocusedContainerColor = DarkGreen,
                            focusedTextColor = White,
                            unfocusedTextColor = White,
                            focusedLabelColor = White,
                            unfocusedLabelColor = White,
                            focusedIndicatorColor = White,
                            unfocusedIndicatorColor = White,
                        ),
                        trailingIcon = {
                            AnimatedVisibility(
                                visible = ttmViewModel.torrentFileUri.value != null,
                                enter = fadeIn(),
                                exit = fadeOut(),
                                modifier = Modifier
                                    .padding(end = 12.dp)
                                    .wrapContentSize()
                            )
                            {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_backspace),
                                    contentDescription = stringResource(R.string.full_backspace),
                                    tint = White,
                                    modifier = Modifier
                                        .clickable {
                                            ttmViewModel.torrentFileUri.value = null
                                            ttmViewModel.clearLogs()
                                        }
                                )
                            }
                        },
                    )

                    HorizontalDivider(
                        Modifier.padding(top = 16.dp, bottom = 16.dp),
                        color = Color.Gray
                    )
                    LogsScreen(
                        logsState = ttmViewModel.logsState,
                        logsTitle = ttmViewModel.logsTitle.value
                    )
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
                            launcher.launch("application/x-bittorrent")
                        },
                        containerColor = LightGreen,
                        modifier = Modifier
                            .align(Alignment.End)
                            .wrapContentSize()
                            .padding(bottom = 16.dp),
                        content = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_upload_file),
                                contentDescription = stringResource(R.string.upload_icon_button),
                                tint = DarkGreen
                            )
                        }
                    )
                    FloatingActionButton(
                        onClick = {
                            if (ttmViewModel.torrentFileUri.value == null) {
                                Toast.makeText(
                                    context,
                                    R.string.torrent_file_is_empty,
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@FloatingActionButton
                            }

                            ttmViewModel.startConversion(context)
                        },
                        modifier = Modifier
                            .align(Alignment.End)
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
                }
            }
        }
    }
}

@SuppressLint("Range")
private fun getFileName(context: Context, uri: Uri): String {
    var result: String? = null
    if (uri.scheme == "content") {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        try {
            if (cursor != null && cursor.moveToFirst()) {
                result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
            }
        } finally {
            cursor?.close()
        }
    }
    if (result == null) {
        result = uri.path
        val cut = result?.lastIndexOf('/')
        if (cut != -1) {
            if (result != null) {
                if (cut != null) {
                    result = result.substring(cut + 1)
                }
            }
        }
    }
    return result ?: "Unknown"
}
