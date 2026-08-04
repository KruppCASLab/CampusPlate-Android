package edu.cwru.caslab.campusplate.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.cwru.caslab.campusplate.ui.icons.no_food

@Composable
fun EmptyPlaceholder(
  modifier: Modifier = Modifier,
  text: String
) {
    Column(
      modifier = modifier
        .fillMaxWidth(),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {

      Icon(
        modifier = modifier
          .size(48.dp),
        imageVector = no_food,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.outline
      )

      Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.outline
      )
    }
}
