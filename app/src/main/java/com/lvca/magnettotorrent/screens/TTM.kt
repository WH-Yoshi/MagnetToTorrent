package com.lvca.magnettotorrent.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.annotation.RequiresExtension
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
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
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
fun TorrentToMagnetScreen(
    navController: NavController,
    coroutineScope: CoroutineScope,
    torrentFileName: MutableState<String>
) {
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
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
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
                        text = stringResource(R.string.torrent_to_magnet),
                        color = md_theme_light_onTertiary,
                        fontFamily = UbuntuFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 25.sp,
                        modifier = Modifier
                            .padding(top = 16.dp, bottom = 32.dp)
                    )
                    Text(
                        text = stringResource(R.string.upload_torrent_file),
                        color = md_theme_light_onTertiary,
                        fontFamily = UbuntuFontFamily,
                        fontWeight = FontWeight.Light,
                        fontSize = 14.sp
                    )
                    Text(
                        text = stringResource(R.string.files),
                        color = md_theme_light_onTertiary,
                        fontFamily = UbuntuFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    Text(
                        text = getFileName(context, Uri.parse(torrentFileName.value)),
                        color = md_theme_light_onTertiary,
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
                        containerColor = md_theme_light_tertiaryContainer,
                        modifier = Modifier
                            .align(Alignment.End)
                            .wrapContentSize()
                            .padding(bottom = 16.dp),
                        content = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_upload),
                                contentDescription = stringResource(R.string.upload_icon_button),
                                tint = md_theme_light_tertiary
                            )
                        }
                    )
                    FloatingActionButton(
                        onClick = {
                            print(torrentFileName.value)
                            if (torrentFileName.value.isEmpty()) {
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
                        containerColor = md_theme_light_tertiaryContainer,
                        content = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_sync),
                                contentDescription = stringResource(R.string.convert),
                                tint = md_theme_light_tertiary
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

@Preview
@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun TorrentToMagnetScreenPreview() {
    TorrentToMagnetScreen(
        navController = NavController(context = LocalContext.current),
        torrentFileName = mutableStateOf(""),
        coroutineScope = CoroutineScope(Dispatchers.IO)
    )
}