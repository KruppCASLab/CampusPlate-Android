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
public val add_a_photo: ImageVector
  get() {
    if (_add_a_photo != null) {
      return _add_a_photo!!
    }
    _add_a_photo =
      ImageVector.Builder(
          name = "add_a_photo",
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
            moveTo(11f, 13f)
            close()
            moveTo(3f, 21f)
            quadTo(2.18f, 21f, 1.59f, 20.41f)
            reflectiveQuadTo(1f, 19f)
            verticalLineTo(7f)
            quadTo(1f, 6.18f, 1.59f, 5.59f)
            reflectiveQuadTo(3f, 5f)
            horizontalLineTo(6.15f)
            lineTo(8f, 3f)
            horizontalLineToRelative(6f)
            verticalLineTo(5f)
            horizontalLineTo(8.88f)
            lineTo(7.05f, 7f)
            horizontalLineTo(3f)
            verticalLineTo(19f)
            horizontalLineTo(19f)
            verticalLineTo(10f)
            horizontalLineToRelative(2f)
            verticalLineToRelative(9f)
            quadToRelative(0f, 0.82f, -0.59f, 1.41f)
            reflectiveQuadTo(19f, 21f)
            horizontalLineTo(3f)
            close()
            moveTo(19f, 7f)
            verticalLineTo(5f)
            horizontalLineTo(17f)
            verticalLineTo(3f)
            horizontalLineToRelative(2f)
            verticalLineTo(1f)
            horizontalLineToRelative(2f)
            verticalLineTo(3f)
            horizontalLineToRelative(2f)
            verticalLineTo(5f)
            horizontalLineTo(21f)
            verticalLineTo(7f)
            horizontalLineTo(19f)
            close()
            moveTo(11f, 17.5f)
            quadToRelative(1.88f, 0f, 3.19f, -1.31f)
            reflectiveQuadTo(15.5f, 13f)
            reflectiveQuadTo(14.19f, 9.81f)
            reflectiveQuadTo(11f, 8.5f)
            reflectiveQuadTo(7.81f, 9.81f)
            reflectiveQuadTo(6.5f, 13f)
            reflectiveQuadToRelative(1.31f, 3.19f)
            reflectiveQuadTo(11f, 17.5f)
            close()
            moveToRelative(0f, -2f)
            quadToRelative(-1.05f, 0f, -1.78f, -0.72f)
            reflectiveQuadTo(8.5f, 13f)
            reflectiveQuadTo(9.23f, 11.23f)
            reflectiveQuadTo(11f, 10.5f)
            reflectiveQuadToRelative(1.78f, 0.72f)
            reflectiveQuadTo(13.5f, 13f)
            reflectiveQuadToRelative(-0.72f, 1.78f)
            reflectiveQuadTo(11f, 15.5f)
            close()
          }
        }
        .build()
    return _add_a_photo!!
  }

private var _add_a_photo: ImageVector? = null

