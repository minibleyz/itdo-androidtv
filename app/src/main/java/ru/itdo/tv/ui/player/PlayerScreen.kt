package ru.itdo.tv.ui.player

import android.view.KeyEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import ru.itdo.tv.ui.theme.ItdoColors
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text

/**
 * Полноэкранный плеер на Media3/ExoPlayer. Работает как с HLS-потоками стримов,
 * так и с mp4/hls-ссылками клипов — ExoPlayer сам определяет формат по URL.
 */
@Composable
fun PlayerScreen(
    title: String,
    subtitle: String,
    mediaUrl: String?,
    onBack: () -> Unit,
) {
    val context = LocalContext.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            if (mediaUrl != null) {
                setMediaItem(MediaItem.fromUri(mediaUrl))
                prepare()
                playWhenReady = true
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose { exoPlayer.release() }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ItdoColors.ChatWindow)
            .onKeyEvent { event ->
                when (event.nativeKeyEvent.keyCode) {
                    KeyEvent.KEYCODE_BACK, KeyEvent.KEYCODE_ESCAPE -> {
                        onBack(); true
                    }
                    KeyEvent.KEYCODE_DPAD_CENTER, KeyEvent.KEYCODE_ENTER -> {
                        exoPlayer.playWhenReady = !exoPlayer.playWhenReady; true
                    }
                    else -> false
                }
            }
    ) {
        if (mediaUrl != null) {
            AndroidView(
                factory = {
                    PlayerView(context).apply {
                        player = exoPlayer
                        useController = true
                        controllerShowTimeoutMs = 3500
                    }
                },
                modifier = Modifier.fillMaxSize(),
            )
        } else {
            Column(modifier = Modifier.align(Alignment.Center).padding(24.dp)) {
                Text("Трансляция недоступна", color = ItdoColors.TextPrimary, style = MaterialTheme.typography.headlineMedium)
                Text("Стрим сейчас не в эфире", color = ItdoColors.TextSecondary, style = MaterialTheme.typography.bodyMedium)
            }
        }

        Column(modifier = Modifier.align(Alignment.TopStart).padding(24.dp)) {
            Text(title, color = ItdoColors.TextPrimary, style = MaterialTheme.typography.titleMedium)
            Text(subtitle, color = ItdoColors.TextSecondary, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
