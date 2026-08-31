package ru.itdo.tv.ui.common

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ru.itdo.tv.ui.theme.ItdoColors
import androidx.tv.material3.Text
import androidx.tv.material3.MaterialTheme
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource

/**
 * Карточка в духе transitions.dev: анимация только через graphicsLayer
 * (scale/translation) — композитится на GPU, никакого пересчёта layout.
 * Никакого Material-риппла — вместо него тонкая акцентная рамка и лёгкий подъём.
 */
@Composable
fun FocusCard(
    title: String,
    subtitle: String,
    imageUrl: String?,
    modifier: Modifier = Modifier,
    isLive: Boolean = false,
    aspectRatioWidth: Float = 16f,
    aspectRatioHeight: Float = 9f,
    onClick: () -> Unit,
) {
    var focused by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (focused) 1.07f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium),
        label = "cardScale",
    )
    val borderWidth by animateDpAsState(
        targetValue = if (focused) 2.5.dp else 0.dp,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "cardBorder",
    )

    Column(
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                transformOrigin = TransformOrigin(0.5f, 0.5f)
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(aspectRatioWidth / aspectRatioHeight)
                .clip(RoundedCornerShape(10.dp))
                .background(ItdoColors.BgSecondary)
                .border(borderWidth, Brush.linearGradient(ItdoColors.AccentGradient), RoundedCornerShape(10.dp))
                .onFocusChanged { focused = it.isFocused || it.hasFocus }
                .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null, onClick = onClick)
        ) {
            if (imageUrl != null) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = title,
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Crop,
                )
            }
            if (isLive) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(ItdoColors.LiveRed)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text("В ЭФИРЕ", color = Color.White, style = MaterialTheme.typography.labelSmall)
                }
            }
        }
        Text(
            text = title,
            color = ItdoColors.TextPrimary,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            modifier = Modifier.padding(top = 6.dp),
        )
        Text(
            text = subtitle,
            color = ItdoColors.TextSecondary,
            style = MaterialTheme.typography.labelSmall,
            maxLines = 1,
        )
    }
}
