package ru.itdo.tv.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.itdo.tv.ui.common.FocusCard
import ru.itdo.tv.ui.common.HeroBanner
import ru.itdo.tv.ui.theme.ItdoColors
import ru.itdo.tv.domain.model.Clip
import ru.itdo.tv.domain.model.Stream
import androidx.tv.material3.Button
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text

@Composable
fun HomeScreen(
    state: HomeUiState,
    onRefresh: () -> Unit,
    onOpenSearch: () -> Unit,
    onOpenStream: (Stream) -> Unit,
    onOpenClip: (Clip) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize().background()) {
        when (state) {
            is HomeUiState.Loading -> LoadingState()
            is HomeUiState.Error -> ErrorState(state.message, onRefresh)
            is HomeUiState.Content -> ContentState(state, onRefresh, onOpenSearch, onOpenStream, onOpenClip)
        }
    }
}

@Composable
private fun ContentState(
    state: HomeUiState.Content,
    onRefresh: () -> Unit,
    onOpenSearch: () -> Unit,
    onOpenStream: (Stream) -> Unit,
    onOpenClip: (Clip) -> Unit,
) {
    val heroStream = state.streams.firstOrNull()

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            if (heroStream != null) {
                HeroBanner(
                    title = heroStream.title.ifBlank { heroStream.name },
                    subtitle = "В эфире • ${heroStream.name} • ${heroStream.viewers} зрителей",
                    imageUrl = heroStream.avatar,
                    modifier = Modifier.fillMaxWidth().height(420.dp),
                )
            } else {
                Box(modifier = Modifier.fillMaxWidth().height(160.dp))
            }
        }

        item {
            RowHeader(title = "Стримы", onRefresh = onRefresh, onSearch = onOpenSearch)
        }
        item {
            if (state.streams.isEmpty()) {
                EmptyRowHint("Сейчас никто не в эфире")
            } else {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 48.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                ) {
                    items(state.streams) { s ->
                        FocusCard(
                            title = s.title.ifBlank { s.name },
                            subtitle = "${s.name} • ${s.viewers} зрителей",
                            imageUrl = s.avatar,
                            isLive = s.isLive,
                            modifier = Modifier.width(280.dp),
                            onClick = { onOpenStream(s) },
                        )
                    }
                }
            }
        }

        item {
            RowHeader(title = "Клипы", onRefresh = onRefresh, onSearch = onOpenSearch)
        }
        item {
            if (state.clips.isEmpty()) {
                EmptyRowHint("Клипов пока нет")
            } else {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 48.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                ) {
                    items(state.clips) { c ->
                        FocusCard(
                            title = c.title.ifBlank { c.name },
                            subtitle = c.channelName ?: c.name,
                            imageUrl = c.avatar,
                            aspectRatioWidth = 9f,
                            aspectRatioHeight = 16f,
                            modifier = Modifier.width(160.dp),
                            onClick = { onOpenClip(c) },
                        )
                    }
                }
            }
        }

        item { Box(modifier = Modifier.height(40.dp)) }
    }
}

@Composable
private fun RowHeader(title: String, onRefresh: () -> Unit, onSearch: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(start = 48.dp, end = 48.dp, top = 28.dp, bottom = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = title, style = MaterialTheme.typography.headlineMedium, color = ItdoColors.TextPrimary)
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = onSearch) { Text("Поиск") }
            Button(onClick = onRefresh) { Text("Обновить") }
        }
    }
}

@Composable
private fun EmptyRowHint(text: String) {
    Text(
        text = text,
        color = ItdoColors.TextSecondary,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(start = 48.dp),
    )
}

@Composable
private fun LoadingState() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Загрузка…", color = ItdoColors.TextSecondary, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
private fun ErrorState(message: String, onRetry: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(48.dp), verticalArrangement = Arrangement.Center) {
        Text("Не удалось загрузить", color = ItdoColors.TextPrimary, style = MaterialTheme.typography.headlineMedium)
        Text(message, color = ItdoColors.TextSecondary, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(top = 8.dp, bottom = 20.dp))
        Button(onClick = onRetry) { Text("Повторить") }
    }
}

private fun Modifier.background() = this.then(Modifier.background(ItdoColors.BgPrimary))
