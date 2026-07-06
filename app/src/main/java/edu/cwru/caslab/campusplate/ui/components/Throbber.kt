package edu.cwru.caslab.campusplate.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun Throbber( //TODO: Credits
    modifier: Modifier = Modifier.size(100.dp),
    strokeWidth: Float = 8f,
    trackColor: Color = Color.Transparent,
    color: Color = MaterialTheme.colorScheme.primary,
) {

    val transition = rememberInfiniteTransition()

    val startAngle by transition.animateValue(
        initialValue = 0f,
        targetValue = 360f,
        typeConverter = Float.VectorConverter,
        animationSpec = infiniteRepeatable(
            tween (
                1000,
                0,
                LinearEasing
            ),
            RepeatMode.Restart
        )
    )

    val sweepAngle by transition.animateValue(
        initialValue = 360f,
        targetValue = 0f,
        typeConverter = Float.VectorConverter,
        animationSpec = infiniteRepeatable(
            tween(
                1500,
                0,
                LinearEasing
            ),
            RepeatMode.Reverse
        )
    )

    Canvas(
        modifier
    ) {

        drawCircle(
            trackColor,
            0.4f * size.minDimension,
            style = Stroke(width = strokeWidth)
        )

        drawArc(
            color,
            startAngle,
            sweepAngle,
            false,
            Offset(
                0.1f * (size.minDimension),
                0.1f * (size.minDimension)
            ),
            Size(
                0.8f * size.minDimension,
                0.8f * size.minDimension
            ),
            1f,
            Stroke(
                strokeWidth,
                cap = StrokeCap.Round
            )
        )

    }

}
