package edu.cwru.caslab.campusplate.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import edu.cwru.caslab.campusplate.model.FoodStop
import edu.cwru.caslab.campusplate.ui.components.TopNavigationBar


@Composable
fun FoodStopInfoScreen(
  foodStop: FoodStop?,
  modifier: Modifier = Modifier
) { 
  Column(
    modifier = modifier
        .fillMaxSize()
        .systemBarsPadding()
        .padding(start = 16.dp, end = 16.dp, top = 16.dp),
  ) { 

    val context = LocalContext.current
        
    Text(
      text = "${foodStop?.name}",
      style = MaterialTheme.typography.headlineMedium,
      fontWeight = FontWeight.Bold
    )

    HorizontalDivider( modifier = Modifier.padding(4.dp) )

    Text(
      text = "Located at:",
      style = MaterialTheme.typography.titleMedium,
      fontWeight = FontWeight.Bold
    )

    Text(
      modifier = Modifier.clickable { 

        openLocation(
          latitude = foodStop?.lat ?: 0.0,
          longitude = foodStop?.lng ?: 0.0,
          context = context,
          label = "${foodStop?.name}"
        )

      },
      text = "${foodStop?.streetAddress}",
      textDecoration = TextDecoration.Underline,
      style = MaterialTheme.typography.titleMedium
    )

    HorizontalDivider( modifier = Modifier.padding(4.dp) )

    Text( 
      text = "Description:",
      style = MaterialTheme.typography.titleMedium,
      fontWeight = FontWeight.Bold
    )
    
    Text(
      text = "${foodStop?.description}",
      style = MaterialTheme.typography.titleMedium
    )
    
  }
}
