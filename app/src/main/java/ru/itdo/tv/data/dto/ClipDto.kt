package ru.itdo.tv.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ClipDto(
    val id: Int,
    @SerialName("channel_id") val channelId: Int? = null,
    @SerialName("channel_name") val channelName: String? = null,
    val username: String = "",
    val name: String = "",
    val avatar: String? = null,
    val title: String = "",
    val description: String? = null,
    @SerialName("video_url") val videoUrl: String,
    val likes: Int = 0,
    @SerialName("created_at") val createdAt: String? = null,
)

@Serializable
data class ClipsResponse(
    val clips: List<ClipDto> = emptyList(),
    val page: Int = 0,
    val limit: Int = 20,
    val total: Int = 0,
    @SerialName("has_more") val hasMore: Boolean = false,
    val query: String? = null,
)
