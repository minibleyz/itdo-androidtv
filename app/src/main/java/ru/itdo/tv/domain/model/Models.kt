package ru.itdo.tv.domain.model

data class Stream(
    val id: Int,
    val username: String,
    val name: String,
    val avatar: String?,
    val title: String,
    val description: String?,
    val isLive: Boolean,
    val hlsUrl: String?,
    val viewers: Int,
    val startedAt: String?,
)

data class Clip(
    val id: Int,
    val channelId: Int?,
    val channelName: String?,
    val username: String,
    val name: String,
    val avatar: String?,
    val title: String,
    val description: String?,
    val videoUrl: String,
    val likes: Int,
    val createdAt: String?,
)

/** Общий элемент для рядов/карточек — чтобы UI не знал о разнице стрима/клипа. */
sealed interface FeedItem {
    val id: String
    val title: String
    val subtitle: String
    val thumbnailUrl: String?

    data class StreamItem(val stream: Stream) : FeedItem {
        override val id get() = "stream-${stream.id}"
        override val title get() = stream.title.ifBlank { stream.name }
        override val subtitle get() = if (stream.isLive) "В эфире • ${stream.viewers} зрителей" else stream.name
        override val thumbnailUrl get() = stream.avatar
    }

    data class ClipItem(val clip: Clip) : FeedItem {
        override val id get() = "clip-${clip.id}"
        override val title get() = clip.title.ifBlank { clip.name }
        override val subtitle get() = clip.channelName ?: clip.name
        override val thumbnailUrl get() = clip.avatar
    }
}
