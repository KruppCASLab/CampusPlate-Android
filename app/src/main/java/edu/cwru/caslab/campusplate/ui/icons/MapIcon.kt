package edu.cwru.caslab.campusplate.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.Dp

public fun getMarkerImage(
  size: Dp,
  color: Color
): ImageVector {
 return ImageVector.Builder(
          name = "location_on",
          defaultWidth = size,
          defaultHeight = size,
          viewportWidth = 48f,
          viewportHeight = 48f,
        )
        .apply {
          path(
            fill = SolidColor(color),
            fillAlpha = 1f,
            stroke = null,
            strokeAlpha = 1f,
            strokeLineWidth = 1f,
            strokeLineCap = StrokeCap.Butt,
            strokeLineJoin = StrokeJoin.Bevel,
            strokeLineMiter = 1f,
            pathFillType = PathFillType.Companion.NonZero,
          ) {
            moveTo(26.83f, 22.83f)
            quadTo(28f, 21.65f, 28f, 20f)
            reflectiveQuadTo(26.83f, 17.17f)
            reflectiveQuadTo(24f, 16f)
            reflectiveQuadToRelative(-2.82f, 1.17f)
            reflectiveQuadTo(20f, 20f)
            reflectiveQuadToRelative(1.18f, 2.82f)
            reflectiveQuadTo(24f, 24f)
            reflectiveQuadToRelative(2.83f, -1.18f)
            close()
            moveTo(24f, 38.7f)
            quadToRelative(6.1f, -5.6f, 9.05f, -10.18f)
            reflectiveQuadTo(36f, 20.4f)
            quadToRelative(0f, -5.45f, -3.48f, -8.92f)
            reflectiveQuadTo(24f, 8f)
            reflectiveQuadToRelative(-8.52f, 3.48f)
            reflectiveQuadTo(12f, 20.4f)
            quadToRelative(0f, 3.55f, 2.95f, 8.13f)
            reflectiveQuadTo(24f, 38.7f)
            close()
            moveTo(24f, 44f)
            quadTo(15.95f, 37.15f, 11.98f, 31.28f)
            reflectiveQuadTo(8f, 20.4f)
            quadTo(8f, 12.9f, 12.83f, 8.45f)
            reflectiveQuadTo(24f, 4f)
            reflectiveQuadTo(35.18f, 8.45f)
            reflectiveQuadTo(40f, 20.4f)
            quadToRelative(0f, 5f, -3.98f, 10.88f)
            reflectiveQuadTo(24f, 44f)
            close()
            moveTo(24f, 20f)
            close()
          }
        }
        .build()
}

