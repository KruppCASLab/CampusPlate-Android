package edu.cwru.caslab.campusplate.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

@Suppress("CheckReturnValue")
public val no_food: ImageVector
  get() {
    if (_no_food != null) {
      return _no_food!!
    }
    _no_food =
      ImageVector.Builder(
          name = "no_food",
          defaultWidth = 24.dp,
          defaultHeight = 24.dp,
          viewportWidth = 24f,
          viewportHeight = 24f,
        )
        .apply {
          path(
            fill = SolidColor(Color.Black),
            fillAlpha = 1f,
            stroke = null,
            strokeAlpha = 1f,
            strokeLineWidth = 1f,
            strokeLineCap = StrokeCap.Butt,
            strokeLineJoin = StrokeJoin.Bevel,
            strokeLineMiter = 1f,
            pathFillType = PathFillType.Companion.NonZero,
          ) {
            moveTo(21.63f, 18.75f)
            lineTo(19.8f, 16.93f)
            lineTo(20.8f, 7f)
            horizontalLineTo(11.25f)
            lineTo(11f, 5f)
            horizontalLineToRelative(5f)
            verticalLineTo(1f)
            horizontalLineToRelative(2f)
            verticalLineTo(5f)
            horizontalLineToRelative(5f)
            lineTo(21.63f, 18.75f)
            close()
            moveToRelative(-6f, -5.98f)
            close()
            moveTo(20.48f, 23.3f)
            lineTo(0.68f, 3.5f)
            lineTo(2.1f, 2.07f)
            lineToRelative(19.8f, 19.8f)
            lineTo(20.48f, 23.3f)
            close()
            moveTo(1f, 19f)
            verticalLineTo(17f)
            horizontalLineTo(16f)
            verticalLineToRelative(2f)
            horizontalLineTo(1f)
            close()
            moveToRelative(1f, 4f)
            quadTo(1.58f, 23f, 1.29f, 22.71f)
            quadTo(1f, 22.43f, 1f, 22f)
            verticalLineTo(21f)
            horizontalLineTo(16f)
            verticalLineToRelative(1f)
            quadToRelative(0f, 0.43f, -0.29f, 0.71f)
            reflectiveQuadTo(15f, 23f)
            horizontalLineTo(2f)
            close()
            moveTo(9.05f, 9.02f)
            verticalLineToRelative(2f)
            quadToRelative(-0.13f, 0f, -0.28f, -0.01f)
            reflectiveQuadTo(8.5f, 11f)
            quadTo(7.03f, 11f, 5.71f, 11.5f)
            reflectiveQuadTo(3.68f, 13f)
            horizontalLineToRelative(9.35f)
            lineToRelative(2f, 2f)
            horizontalLineTo(1f)
            quadTo(1f, 11.98f, 3.34f, 10.49f)
            reflectiveQuadTo(8.5f, 9f)
            quadTo(8.63f, 9f, 8.78f, 9.01f)
            reflectiveQuadTo(9.05f, 9.02f)
            close()
            moveTo(8.5f, 13f)
            close()
          }
        }
        .build()
    return _no_food!!
  }

private var _no_food: ImageVector? = null

