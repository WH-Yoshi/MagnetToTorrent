package com.lvca.magnettotorrent

import android.app.DownloadManager
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.lvca.magnettotorrent.ui.theme.MagnetToTorrentTheme
import com.lvca.magnettotorrent.ui.theme.UbuntuFontFamily
import com.lvca.magnettotorrent.ui.theme.md_theme_light_onTertiary
import com.lvca.magnettotorrent.ui.theme.md_theme_light_onTertiaryContainer
import com.lvca.magnettotorrent.ui.theme.md_theme_light_tertiary
import com.lvca.magnettotorrent.ui.theme.md_theme_light_tertiaryContainer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    private val magnetLink = mutableStateOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MagnetToTorrentTheme {
                MagnetToTorrentApp(magnetLink)
            }
        }

        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        if (intent.action == Intent.ACTION_VIEW) {
            val magnetLinkValue = intent.dataString
            if (magnetLinkValue != null && magnetLinkValue.startsWith("magnet:")) {
                magnetLink.value = magnetLinkValue
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MagnetToTorrentApp(magnetLink: MutableState<String>) {
    val snackBarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val torrentLogs = remember { mutableStateOf("") }
    val imeInsets = WindowInsets.ime
    val scrollState = rememberScrollState()

    LaunchedEffect(torrentLogs.value) {
        scrollState.scrollTo(scrollState.maxValue)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Magnet to Torrent",
                        fontWeight = FontWeight.Bold,
                        fontFamily = UbuntuFontFamily,
                        color = md_theme_light_onTertiary,
                    )
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = md_theme_light_tertiary
                )
            )
        },
        snackbarHost = { SnackbarHost(snackBarHostState) },
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
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 32.dp)
                ) {
                    OutlinedTextField(
                        value = magnetLink.value,
                        onValueChange = { newValue ->
                            magnetLink.value = newValue
                        },
                        label = { Text("Enter magnet link", fontFamily = UbuntuFontFamily) },
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
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(top = 16.dp)
                                .height(300.dp)
                        ) {
                            Text(
                                text = "Magnet Link:",
                                color = md_theme_light_onTertiary,
                                fontFamily = UbuntuFontFamily
                            )
                            Text(
                                text = magnetLink.value,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .verticalScroll(scrollState),
                                color = md_theme_light_onTertiary,
                                fontFamily = UbuntuFontFamily
                            )
                        }
                        Column(
                            modifier = Modifier
                                .padding(top = 16.dp, bottom = 56.dp)
                                .height(400.dp)
                        ) {
                            Text(
                                text = "Logs:",
                                color = md_theme_light_onTertiary,
                                fontFamily = UbuntuFontFamily
                            )
                            Text(
                                text = torrentLogs.value,
                                modifier = Modifier
                                    .width(304.dp)
                                    .verticalScroll(scrollState),
                                color = md_theme_light_onTertiaryContainer,
                                fontFamily = UbuntuFontFamily
                            )
                        }
                    }
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
                    AnimatedVisibility(
                        visible = magnetLink.value.isNotEmpty(),
                        enter = fadeIn(),
                        exit = fadeOut(),
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(bottom = 16.dp)
                            .wrapContentSize()
                    ) {
                        FloatingActionButton(
                            onClick = {
                                magnetLink.value = ""
                                torrentLogs.value = ""
                            },
                            containerColor = md_theme_light_tertiaryContainer,
                            content = {
                                Icon(
                                    painter = painterResource(id = R.drawable.cancel),
                                    contentDescription = "Clear Text",
                                    tint = md_theme_light_tertiary
                                )
                            }
                        )
                    }
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
                                painter = painterResource(id = R.drawable.paste),
                                contentDescription = "Copy",
                                tint = md_theme_light_tertiary
                            )
                        }
                    )
                    FloatingActionButton(
                        onClick = {
                            CoroutineScope(Dispatchers.IO).launch {
                                val result = convertMagnetToTorrent(magnetLink.value, torrentLogs)
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(context, context.getString(result), Toast.LENGTH_SHORT).show()
                                }
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
                                contentDescription = "Convert",
                                tint = md_theme_light_tertiary
                            )
                        }
                    )
                    ExtendedFloatingActionButton(
                        onClick = {
                            try {
                                context.startActivity(Intent(DownloadManager.ACTION_VIEW_DOWNLOADS))
                            } catch (e: Exception) {
                                Toast.makeText(context, "No application found to open the folder", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier
                            .wrapContentSize(),
                        containerColor = md_theme_light_tertiaryContainer,
                        icon = {
                            Icon(
                                painter = painterResource(id = R.drawable.folder),
                                contentDescription = "Open downloads folder",
                                tint = md_theme_light_tertiary
                            )
                        },
                        text = {
                            Text(
                                text = "Downloads",
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