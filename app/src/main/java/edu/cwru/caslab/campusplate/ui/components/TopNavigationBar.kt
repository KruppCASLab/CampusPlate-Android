package edu.cwru.caslab.campusplate.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.cwru.caslab.campusplate.ui.icons.arrow_back

@Composable
fun TopNavigationBar(
  modifier: Modifier = Modifier,
  text: String,
  onClick: () -> Unit
) {
    Row(
      modifier = modifier
        .fillMaxWidth()
        .padding(bottom = 8.dp)
        .systemBarsPadding(),
      verticalAlignment = Alignment.CenterVertically
    ) {
      FilledIconButton(
        modifier = Modifier.padding(start = 8.dp),
        onClick = onClick
      ) {
        Icon(
          imageVector = arrow_back,
          contentDescription = "Back Arrow"
        )
      }
      Text(
        modifier = Modifier.padding(start = 8.dp),
        text = text,
        style = MaterialTheme.typography.displaySmall
      )
    }
}
