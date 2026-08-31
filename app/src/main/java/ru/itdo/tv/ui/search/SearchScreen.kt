package ru.itdo.tv.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import ru.itdo.tv.domain.model.Clip
import ru.itdo.tv.domain.model.Stream
import ru.itdo.tv.ui.common.FocusCard
import ru.itdo.tv.ui.theme.ItdoColors
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.graphics.Color

@Composable
fun SearchScreen(
    state: SearchUiState,
    onQueryChange: (String) -> Unit,
    onOpenStream: (Stream) -> Unit,
    onOpenClip: (Clip) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ItdoColors.BgPrimary)
            .padding(horizontal = 48.dp, vertical = 32.dp)
    ) {
        Text("Поиск", style = MaterialTheme.typography.headlineMedium, color = ItdoColors.TextPrimary)

        OutlinedTextField(
            value = state.query,
            onValueChange = onQueryChange,
            placeholder = { Text("Название стрима, клипа или автор…") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            colors = TextFieldDefaults.colors(
                focusedTextColor = ItdoColors.TextPrimary,
                unfocusedTextColor = ItdoColors.TextPrimary,
                focusedContainerColor = ItdoColors.BgSecondary,
                unfocusedContainerColor = ItdoColors.BgSecondary,
                cursorColor = ItdoColors.AccentPrimary,
                focusedIndicatorColor = ItdoColors.AccentPrimary,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 24.dp),
        )

        when {
            state.loading -> Text("Ищём…", color = ItdoColors.TextSecondary, style = MaterialTheme.typography.bodyMedium)
            !state.searched -> Text("Начните ввод, чтобы найти стримы и клипы", color = ItdoColors.TextSecondary, style = MaterialTheme.typography.bodyMedium)
            state.streams.isEmpty() && state.clips.isEmpty() -> Text(
                "Ничего не найдено по «${state.query}»",
                color = ItdoColors.TextSecondary,
                style = MaterialTheme.typography.bodyMedium,
            )
            else -> {
                if (state.streams.isNotEmpty()) {
                    Text("Стримы", style = MaterialTheme.typography.titleMedium, color = ItdoColors.TextPrimary, modifier = Modifier.padding(bottom = 10.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
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
                if (state.clips.isNotEmpty()) {
                    Text("Клипы", style = MaterialTheme.typography.titleMedium, color = ItdoColors.TextPrimary, modifier = Modifier.padding(top = 24.dp, bottom = 10.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
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
        }
    }
}
