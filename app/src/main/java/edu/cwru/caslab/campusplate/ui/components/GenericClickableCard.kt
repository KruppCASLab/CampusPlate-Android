package edu.cwru.caslab.campusplate.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun GenericClickableCard(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Card (
      modifier = modifier.fillMaxWidth()
        .padding(bottom = 8.dp),
      onClick = onClick
    ) { 
      Row (
        modifier = Modifier
          .fillMaxWidth()
          .height(IntrinsicSize.Min)
      ) {
        VerticalDivider(
          color = color,
          thickness = 6.dp
        )
        Column (
          modifier = Modifier
            .padding(8.dp)
        ){ 
          content()
        }
      }
    }
}
