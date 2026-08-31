package ru.itdo.tv.ui.common

import androidx.compose.animation.core.tween
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ru.itdo.tv.ui.theme.ItdoColors
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text

/**
 * Большой hero-блок сверху экрана — превью выбранной/сфокусированной карточки,
 * с тёплым градиентом в фон вместо чёрного. Переключение — мягкий Crossfade
 * (alpha через graphicsLayer), без дёрганья layout.
 */
@Composable
fun HeroBanner(
    title: String,
    subtitle: String,
    imageUrl: String?,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxWidth()) {
        Crossfade(targetState = imageUrl, animationSpec = tween(280), label = "heroImage") { url ->
            if (url != null) {
                AsyncImage(
                    model = url,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )
            } else {
                Box(modifier = Modifier.fillMaxSize().background(ItdoColors.BgSecondary))
            }
        }

        // Тёплое затухание в фон снизу — как --nav-gradient на сайте ITDO.
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            ItdoColors.BgPrimary.copy(alpha = 0.35f),
                            ItdoColors.BgPrimary,
                        ),
                        startY = 0f,
                    )
                )
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(PaddingValues(start = 48.dp, bottom = 40.dp, end = 400.dp))
        ) {
            Text(
                text = title,
                color = ItdoColors.TextPrimary,
                style = MaterialTheme.typography.displayLarge,
                maxLines = 2,
            )
            Text(
                text = subtitle,
                color = ItdoColors.TextSecondary,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 8.dp),
            )
        }
    }
}
