package com.lvca.magnettotorrent

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.compose.rememberNavController
import com.lvca.magnettotorrent.screens.Routes
import com.lvca.magnettotorrent.ui.theme.DarkGreen
import com.lvca.magnettotorrent.ui.theme.MagnetToTorrentTheme
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    val magnetLink = mutableStateOf("")
    val torrentFileName = mutableStateOf("")
    var initialRoute = mutableStateOf(Routes.MENU)

    fun handleIntent(intent: Intent) {
        if (intent.action == Intent.ACTION_VIEW) {
            val dataString = intent.dataString
            if (dataString != null) {
                when {
                    dataString.startsWith("magnet:") -> {
                        magnetLink.value = dataString
                        initialRoute.value = Routes.MTT
                    }
                    dataString.endsWith(".torrent") -> {
                        torrentFileName.value = dataString
                        initialRoute.value = Routes.TTM
                    }
                }
            }
        }
    }

    private val _navigationEvents = Channel<NavigationEvent>(Channel.BUFFERED)
    val navigationEvents = _navigationEvents.receiveAsFlow()

    sealed class NavigationEvent {
        data class NavigateTo(val route: String) : NavigationEvent()
        data object PopBackStack : NavigationEvent()
        data class NavigateToPagerPage(val pageIndex: Int) : NavigationEvent()
    }

    fun navigate(route : String) {
        viewModelScope.launch {
            _navigationEvents.send(NavigationEvent.NavigateTo(route))
        }
    }

    fun onBackButtonPressed() {
        viewModelScope.launch {
            _navigationEvents.send(NavigationEvent.PopBackStack)
        }
    }

    private val _currentPage = mutableIntStateOf(1)
    val currentPage: State<Int> = _currentPage

    fun setPagerPage(pageIndex: Int) {
        _currentPage.intValue = pageIndex
    }

    fun navigateToPagerPage(pageIndex: Int) {
        viewModelScope.launch {
            _navigationEvents.send(NavigationEvent.NavigateToPagerPage(pageIndex))
        }
    }

    fun getPageIndexForRoute(route: String): Int {
        return when (route) {
            Routes.MTT -> 0
            Routes.MENU -> 1
            Routes.TTM -> 2
            else -> 1
        }
    }
}

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        viewModel.handleIntent(intent)
        setContent {
            MagnetToTorrentTheme {
                MagnetToTorrentApp(viewModel)
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        viewModel.handleIntent(intent)
    }
}

@Composable
fun MagnetToTorrentApp(
    viewModel: MainViewModel
) {
    val navController = rememberNavController()
    val initialOffset = 0.10f

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = DarkGreen
    ) {
        NavScreen(
            navController = navController,
            viewModel = viewModel,
            initialOffset = initialOffset
        )
    }
}