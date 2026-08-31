package ru.itdo.tv.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.itdo.tv.data.repo.ClipsRepository
import ru.itdo.tv.data.repo.StreamsRepository
import ru.itdo.tv.domain.model.Clip
import ru.itdo.tv.domain.model.Stream

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Error(val message: String) : HomeUiState
    data class Content(
        val streams: List<Stream>,
        val clips: List<Clip>,
    ) : HomeUiState
}

class HomeViewModel(
    private val streamsRepository: StreamsRepository,
    private val clipsRepository: ClipsRepository,
) : ViewModel() {

    var state: HomeUiState by mutableStateOf(HomeUiState.Loading)
        private set

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            state = HomeUiState.Loading
            try {
                // Лайв-стримы + последние клипы — два независимых запроса параллельно не нужны —
                // для первой версии достаточно последовательно.
                val streams = streamsRepository.list(onlyLive = true, limit = 20)
                val clips = clipsRepository.list(limit = 20)
                state = HomeUiState.Content(streams.items, clips.items)
            } catch (e: Exception) {
                state = HomeUiState.Error(e.message ?: "Не удалось загрузить данные")
            }
        }
    }
}
