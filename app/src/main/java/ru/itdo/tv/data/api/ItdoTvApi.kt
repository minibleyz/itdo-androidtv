package ru.itdo.tv.data.api

import ru.itdo.tv.data.dto.ClipsResponse
import ru.itdo.tv.data.dto.StreamResponse
import ru.itdo.tv.data.dto.StreamsResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Публичный API /api/tv/* на сервере ITDO — без авторизации, только чтение.
 */
interface ItdoTvApi {

    @GET("streams.php")
    suspend fun getStreams(
        @Query("page") page: Int = 0,
        @Query("limit") limit: Int = 20,
        @Query("only_live") onlyLive: Int? = null,
        @Query("q") query: String? = null,
    ): StreamsResponse

    @GET("stream.php")
    suspend fun getStream(@Query("id") id: Int): StreamResponse

    @GET("clips.php")
    suspend fun getClips(
        @Query("page") page: Int = 0,
        @Query("limit") limit: Int = 20,
        @Query("channel_id") channelId: Int? = null,
        @Query("q") query: String? = null,
    ): ClipsResponse
}
