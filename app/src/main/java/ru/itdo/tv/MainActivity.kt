package ru.itdo.tv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
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
                var screen by rememberSaveable(stateSaver = ScreenSaver) { mutableStateOf<Screen>(Screen.Home) }

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

                // Обновляем ленту при каждом возврате на экран Home (без фонового поллинга).
                val lifecycleOwner = LocalLifecycleOwner.current
                DisposableResumeEffect(screen is Screen.Home) {
                    homeViewModel.refresh()
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

/** Вызывает [onResume] каждый раз, когда условие [active] становится true и экран в фокусе. */
@androidx.compose.runtime.Composable
private fun DisposableResumeEffect(active: Boolean, onResume: () -> Unit) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(active) {
        if (active) onResume()
    }
    androidx.compose.runtime.DisposableEffect(lifecycleOwner, active) {
        val observer = LifecycleEventObserver { _, event ->
            if (active && event == Lifecycle.Event.ON_RESUME) onResume()
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        androidx.compose.runtime.DisposableEffectResult { lifecycleOwner.lifecycle.removeObserver(observer) }
    }
}

private val ScreenSaver = androidx.compose.runtime.saveable.Saver<androidx.compose.runtime.MutableState<Screen>, Nothing>(
    save = { null },
    restore = { null },
)
