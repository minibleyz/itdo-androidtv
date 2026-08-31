package ru.itdo.tv.ui.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.itdo.tv.data.repo.ClipsRepository
import ru.itdo.tv.data.repo.StreamsRepository
import ru.itdo.tv.domain.model.Clip
import ru.itdo.tv.domain.model.Stream

data class SearchUiState(
    val query: String = "",
    val loading: Boolean = false,
    val streams: List<Stream> = emptyList(),
    val clips: List<Clip> = emptyList(),
    val searched: Boolean = false,
)

class SearchViewModel(
    private val streamsRepository: StreamsRepository,
    private val clipsRepository: ClipsRepository,
) : ViewModel() {

    var state: SearchUiState by mutableStateOf(SearchUiState())
        private set

    private var debounceJob: Job? = null

    fun onQueryChange(q: String) {
        state = state.copy(query = q)
        debounceJob?.cancel()
        if (q.isBlank()) {
            state = state.copy(streams = emptyList(), clips = emptyList(), searched = false, loading = false)
            return
        }
        debounceJob = viewModelScope.launch {
            delay(400)
            runSearch(q)
        }
    }

    private suspend fun runSearch(q: String) {
        state = state.copy(loading = true)
        try {
            val streams = streamsRepository.list(limit = 20, onlyLive = false, query = q)
            val clips = clipsRepository.list(limit = 20, query = q)
            state = state.copy(streams = streams.items, clips = clips.items, loading = false, searched = true)
        } catch (e: Exception) {
            state = state.copy(loading = false, searched = true)
        }
    }
}
