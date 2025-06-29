package com.lvca.magnettotorrent.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavController
import com.lvca.magnettotorrent.MainViewModel
import com.lvca.magnettotorrent.R
import com.lvca.magnettotorrent.ui.theme.DarkGreen
import com.lvca.magnettotorrent.ui.theme.LightGreen
import com.lvca.magnettotorrent.ui.theme.UbuntuFontFamily
import com.lvca.magnettotorrent.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TorrentToMagnetScreen(
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

    val context = LocalContext.current
    val imeInsets = WindowInsets.ime

    val filePicker = remember {
        Intent(Intent.ACTION_GET_CONTENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/x-bittorrent"
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = {
                            viewModel.navigateToPagerPage(
                                viewModel.getPageIndexForRoute(
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
                            tint = White,
                        )
                    }
                },
                title = {  },
                actions = {  },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = DarkGreen
                )
            )
        },
        containerColor = DarkGreen,
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .background(DarkGreen),
            color = DarkGreen
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.torrent_to_magnet),
                        color = White,
                        fontFamily = UbuntuFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 25.sp,
                        modifier = Modifier
                            .padding(top = 16.dp, bottom = 32.dp)
                    )
                    Text(
                        text = stringResource(R.string.upload_torrent_file),
                        color = White,
                        fontFamily = UbuntuFontFamily,
                        fontWeight = FontWeight.Light,
                        fontSize = 14.sp
                    )
                    Text(
                        text = stringResource(R.string.files),
                        color = White,
                        fontFamily = UbuntuFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    Text(
                        text = getFileName(context, viewModel.torrentFileName.value.toUri()),  // Use the KTX extension function String.toUri instead?
                        color = White,
                        fontFamily = UbuntuFontFamily,
                        fontWeight = FontWeight.Light,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 8.dp)
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
                            try {
                                context.startActivity(filePicker)
                            } catch (e: Exception) {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.no_application_found_to_open_the_folder),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
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
                            print(viewModel.torrentFileName.value)
                            if (viewModel.torrentFileName.value.isEmpty()) {
                                Toast.makeText(context, R.string.torrent_file_is_empty, Toast.LENGTH_SHORT).show()
                                return@FloatingActionButton
                            }
//                            CoroutineScope(Dispatchers.IO).launch {
//                                convertTorrentToMagnet(context, torrentFile.value, torrentLogs)
//                            }
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

@Composable
fun getFileName(context: Context, uri: Uri): String {
    val cursor = context.contentResolver.query(uri, null, null, null, null)
    val nameIndex = cursor?.getColumnIndex(OpenableColumns.DISPLAY_NAME)
    cursor?.moveToFirst()
    val fileName = nameIndex?.let { cursor.getString(it) } ?: stringResource(R.string.no_file_imported)
    cursor?.close()
    return fileName
}