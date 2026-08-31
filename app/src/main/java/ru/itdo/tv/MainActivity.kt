package ru.itdo.tv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.itdo.tv.di.SimpleViewModelFactory
import ru.itdo.tv.domain.model.Clip
import ru.itdo.tv.domain.model.Stream
import ru.itdo.tv.ui.home.HomeScreen
import ru.itdo.tv.ui.home.HomeViewModel
import ru.itdo.tv.ui.player.PlayerScreen
import ru.itdo.tv.ui.search.SearchScreen
import ru.itdo.tv.ui.search.SearchViewModel
import ru.itdo.tv.ui.theme.ItdoTvTheme

private sealed interface Screen {
    data object Home : Screen
    data object Search : Screen
    data class Player(val title: String, val subtitle: String, val mediaUrl: String?) : Screen
}

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val container = (application as ItdoTvApp).container

        setContent {
            ItdoTvTheme {
                var screen by remember { mutableStateOf<Screen>(Screen.Home) }

                val homeViewModel: HomeViewModel = viewModel(
                    factory = SimpleViewModelFactory {
                        HomeViewModel(container.streamsRepository, container.clipsRepository)
                    }
                )
                val searchViewModel: SearchViewModel = viewModel(
                    factory = SimpleViewModelFactory {
                        SearchViewModel(container.streamsRepository, container.clipsRepository)
                    }
                )

                // Обновляем ленту каждый раз, когда мы возвращаемся на Home или приложение
                // возобновляется (onResume) пока мы на Home — без фонового поллинга.
                val isHome = screen is Screen.Home
                LaunchedEffect(isHome) {
                    if (isHome) homeViewModel.refresh()
                }
                val lifecycleOwner = LocalLifecycleOwner.current
                DisposableEffect(lifecycleOwner, isHome) {
                    val observer = LifecycleEventObserver { _, event ->
                        if (isHome && event == Lifecycle.Event.ON_RESUME) homeViewModel.refresh()
                    }
                    lifecycleOwner.lifecycle.addObserver(observer)
                    onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
                }

                when (val s = screen) {
                    is Screen.Home -> HomeScreen(
                        state = homeViewModel.state,
                        onRefresh = { homeViewModel.refresh() },
                        onOpenSearch = { screen = Screen.Search },
                        onOpenStream = { stream: Stream ->
                            screen = Screen.Player(
                                title = stream.title.ifBlank { stream.name },
                                subtitle = "${stream.name} • ${stream.viewers} зрителей",
                                mediaUrl = stream.hlsUrl,
                            )
                        },
                        onOpenClip = { clip: Clip ->
                            screen = Screen.Player(
                                title = clip.title.ifBlank { clip.name },
                                subtitle = clip.channelName ?: clip.name,
                                mediaUrl = clip.videoUrl,
                            )
                        },
                    )
                    is Screen.Search -> SearchScreen(
                        state = searchViewModel.state,
                        onQueryChange = { searchViewModel.onQueryChange(it) },
                        onOpenStream = { stream: Stream ->
                            screen = Screen.Player(
                                title = stream.title.ifBlank { stream.name },
                                subtitle = "${stream.name} • ${stream.viewers} зрителей",
                                mediaUrl = stream.hlsUrl,
                            )
                        },
                        onOpenClip = { clip: Clip ->
                            screen = Screen.Player(
                                title = clip.title.ifBlank { clip.name },
                                subtitle = clip.channelName ?: clip.name,
                                mediaUrl = clip.videoUrl,
                            )
                        },
                    )
                    is Screen.Player -> PlayerScreen(
                        title = s.title,
                        subtitle = s.subtitle,
                        mediaUrl = s.mediaUrl,
                        onBack = { screen = Screen.Home },
                    )
                }
            }
        }
    }
}
