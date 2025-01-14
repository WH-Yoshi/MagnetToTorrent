package com.lvca.magnettotorrent

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.frostwire.jlibtorrent.SessionManager
import com.lvca.magnettotorrent.ui.theme.MagnetToTorrentTheme
import com.lvca.magnettotorrent.ui.theme.md_theme_light_onTertiary
import com.lvca.magnettotorrent.ui.theme.md_theme_light_tertiary
import com.lvca.magnettotorrent.ui.theme.md_theme_light_tertiaryContainer
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MagnetToTorrentTheme {
                MagnetToTorrentApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MagnetToTorrentApp() {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Magnet to Torrent",
                        fontWeight = FontWeight.Bold,
                        color = md_theme_light_onTertiary,
                    )
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = md_theme_light_tertiary
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(md_theme_light_tertiary),
            color = md_theme_light_tertiary
        ) {
            val magnetLink = remember { mutableStateOf("") }
            val imeInsets = WindowInsets.ime

            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 32.dp)
                ) {
                    OutlinedTextField(
                        value = magnetLink.value,
                        onValueChange = { newValue ->
                            magnetLink.value = newValue
                        },
                        label = { Text("Enter magnet link") },
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
                    Text(
                        text = "Magnet Link: ${magnetLink.value}",
                        modifier = Modifier.padding(top = 16.dp),
                        color = md_theme_light_onTertiary,
                        maxLines = 10,
                    )
                }

                ExtendedFloatingActionButton(
                    onClick = {
                        convertMagnetToTorrent(magnetLink.value)
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = "Converting...",
                                duration = SnackbarDuration.Short
                            )
                        }
                    },
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(
                            bottom = 16.dp + imeInsets.asPaddingValues().calculateBottomPadding(),
                            end = 16.dp
                        )
                        .align(Alignment.BottomEnd),
                    containerColor = md_theme_light_tertiaryContainer,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_sync),
                        contentDescription = "Convert",
                        tint = md_theme_light_tertiary
                    )
                    Text(
                        text = "Convert",
                        color = md_theme_light_tertiary,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }
}

fun convertMagnetToTorrent(magnetLink: String) {
    if (magnetLink.isEmpty()) {
        println("Error: Magnet link is empty")
        return
    }
    val sessionManager = SessionManager()

    try {
        sessionManager.start()

        val data = sessionManager.fetchMagnet(magnetLink, 30)
        if (data == null) {
            println("Error: Unable to fetch metadata")
            return
        }

        val file = File("/storage/emulated/0/Download/file.torrent")
        FileOutputStream(file).use { fos ->
            fos.write(data)
        }

    } catch (e: Exception) {
        println("Error: ${e.message}")
    } finally {
        sessionManager.stop()
    }
}

@Preview(showBackground = true)
@Composable
fun MagnetToTorrentPreview() {
    MagnetToTorrentTheme {
        MagnetToTorrentApp()
    }
}