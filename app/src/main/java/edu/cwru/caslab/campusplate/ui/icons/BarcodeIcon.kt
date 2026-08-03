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
public val barcode_scanner: ImageVector
  get() {
    if (_barcode_scanner != null) {
      return _barcode_scanner!!
    }
    _barcode_scanner =
      ImageVector.Builder(
          name = "barcode_scanner",
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
            moveTo(1f, 21f)
            verticalLineTo(16f)
            horizontalLineTo(3f)
            verticalLineToRelative(3f)
            horizontalLineTo(6f)
            verticalLineToRelative(2f)
            horizontalLineTo(1f)
            close()
            moveToRelative(17f, 0f)
            verticalLineTo(19f)
            horizontalLineToRelative(3f)
            verticalLineTo(16f)
            horizontalLineToRelative(2f)
            verticalLineToRelative(5f)
            horizontalLineTo(18f)
            close()
            moveTo(4f, 18f)
            verticalLineTo(6f)
            horizontalLineTo(6f)
            verticalLineTo(18f)
            horizontalLineTo(4f)
            close()
            moveToRelative(3f, 0f)
            verticalLineTo(6f)
            horizontalLineTo(8f)
            verticalLineTo(18f)
            horizontalLineTo(7f)
            close()
            moveToRelative(3f, 0f)
            verticalLineTo(6f)
            horizontalLineToRelative(2f)
            verticalLineTo(18f)
            horizontalLineTo(10f)
            close()
            moveToRelative(3f, 0f)
            verticalLineTo(6f)
            horizontalLineToRelative(3f)
            verticalLineTo(18f)
            horizontalLineTo(13f)
            close()
            moveToRelative(4f, 0f)
            verticalLineTo(6f)
            horizontalLineToRelative(1f)
            verticalLineTo(18f)
            horizontalLineTo(17f)
            close()
            moveToRelative(2f, 0f)
            verticalLineTo(6f)
            horizontalLineToRelative(1f)
            verticalLineTo(18f)
            horizontalLineTo(19f)
            close()
            moveTo(1f, 8f)
            verticalLineTo(3f)
            horizontalLineTo(6f)
            verticalLineTo(5f)
            horizontalLineTo(3f)
            verticalLineTo(8f)
            horizontalLineTo(1f)
            close()
            moveTo(21f, 8f)
            verticalLineTo(5f)
            horizontalLineTo(18f)
            verticalLineTo(3f)
            horizontalLineToRelative(5f)
            verticalLineTo(8f)
            horizontalLineTo(21f)
            close()
          }
        }
        .build()
    return _barcode_scanner!!
  }

private var _barcode_scanner: ImageVector? = null

