package ru.itdo.tv.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StreamDto(
    val id: Int,
    val username: String = "",
    val name: String = "",
    val avatar: String? = null,
    val title: String = "",
    val description: String? = null,
    @SerialName("is_live") val isLive: Boolean = false,
    @SerialName("hls_url") val hlsUrl: String? = null,
    val viewers: Int = 0,
    @SerialName("started_at") val startedAt: String? = null,
)

@Serializable
data class StreamsResponse(
    val streams: List<StreamDto> = emptyList(),
    val page: Int = 0,
    val limit: Int = 20,
    val total: Int = 0,
    @SerialName("has_more") val hasMore: Boolean = false,
    val query: String? = null,
)

@Serializable
data class StreamResponse(
    val stream: StreamDto,
)
