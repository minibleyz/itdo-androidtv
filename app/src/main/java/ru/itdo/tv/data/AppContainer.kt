package ru.itdo.tv.data

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import ru.itdo.tv.BuildConfig
import ru.itdo.tv.data.api.ItdoTvApi
import ru.itdo.tv.data.repo.ClipsRepository
import ru.itdo.tv.data.repo.StreamsRepository
import java.util.concurrent.TimeUnit

/**
 * Простой вручной DI-контейнер без Hilt — меньше движущихся частей для первой версии.
 */
class AppContainer {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        coerceInputValues = true
    }

    private val okHttp: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BASIC else HttpLoggingInterceptor.Level.NONE
            }
        )
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.API_BASE_URL)
        .client(okHttp)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    private val api: ItdoTvApi = retrofit.create(ItdoTvApi::class.java)

    val streamsRepository: StreamsRepository by lazy { StreamsRepository(api) }
    val clipsRepository: ClipsRepository by lazy { ClipsRepository(api) }
}
