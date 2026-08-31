package ru.itdo.tv.data.repo

import ru.itdo.tv.data.api.ItdoTvApi
import ru.itdo.tv.data.dto.StreamDto
import ru.itdo.tv.domain.model.Stream

data class Page<T>(val items: List<T>, val page: Int, val hasMore: Boolean, val query: String?)

class StreamsRepository(private val api: ItdoTvApi) {

    suspend fun list(page: Int = 0, limit: Int = 20, onlyLive: Boolean? = null, query: String? = null): Page<Stream> {
        val res = api.getStreams(
            page = page,
            limit = limit,
            onlyLive = onlyLive?.let { if (it) 1 else 0 },
            query = query?.takeIf { it.isNotBlank() },
        )
        return Page(res.streams.map { it.toDomain() }, res.page, res.hasMore, res.query)
    }

    suspend fun get(id: Int): Stream = api.getStream(id).stream.toDomain()
}

private fun StreamDto.toDomain() = Stream(
    id = id,
    username = username,
    name = name,
    avatar = avatar,
    title = title,
    description = description,
    isLive = isLive,
    hlsUrl = hlsUrl,
    viewers = viewers,
    startedAt = startedAt,
)
