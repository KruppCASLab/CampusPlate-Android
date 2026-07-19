package edu.cwru.caslab.campusplate.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import edu.cwru.caslab.campusplate.model.FoodStop
import edu.cwru.caslab.campusplate.ui.components.TopNavigationBar

@Composable
fun FoodStopInfoScreen(
  foodStop: FoodStop?
) { 
  Column(
    modifier = Modifier.fillMaxSize()
  ) {
    Text(
      text = foodStop?.name ?: ""
    )   

    HorizontalDivider()

    Text(
      text = foodStop?.description ?: ""
    )
  }
}
