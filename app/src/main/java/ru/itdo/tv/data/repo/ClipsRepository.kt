package ru.itdo.tv.data.repo

import ru.itdo.tv.data.api.ItdoTvApi
import ru.itdo.tv.data.dto.ClipDto
import ru.itdo.tv.domain.model.Clip

class ClipsRepository(private val api: ItdoTvApi) {

    suspend fun list(page: Int = 0, limit: Int = 20, channelId: Int? = null, query: String? = null): Page<Clip> {
        val res = api.getClips(
            page = page,
            limit = limit,
            channelId = channelId,
            query = query?.takeIf { it.isNotBlank() },
        )
        return Page(res.clips.map { it.toDomain() }, res.page, res.hasMore, res.query)
    }
}

private fun ClipDto.toDomain() = Clip(
    id = id,
    channelId = channelId,
    channelName = channelName,
    username = username,
    name = name,
    avatar = avatar,
    title = title,
    description = description,
    videoUrl = videoUrl,
    likes = likes,
    createdAt = createdAt,
)
