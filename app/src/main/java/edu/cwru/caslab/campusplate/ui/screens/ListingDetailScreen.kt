package edu.cwru.caslab.campusplate.ui.screens

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import edu.cwru.caslab.campusplate.R
import edu.cwru.caslab.campusplate.ui.ListingUiState
import edu.cwru.caslab.campusplate.ui.ListingViewModel
import edu.cwru.caslab.campusplate.ui.State
import edu.cwru.caslab.campusplate.ui.components.GenericClickableCard
import edu.cwru.caslab.campusplate.ui.components.Throbber
import edu.cwru.caslab.campusplate.ui.components.TopNavigationBar
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextDecoration
import kotlin.io.encoding.Base64

private fun getColor(colorString: String): Color { // Helper
  return Color(android.graphics.Color.parseColor("#${colorString}"))
}

private fun String.toColor(): Color{
  return try {
    getColor(this)
  } catch(e: Exception) {
    Color.Black
  }
}

fun openLocation(context: Context, latitude: Double, longitude: Double, label: String) {
    val uri = Uri.parse("geo:0,0?q=$latitude,$longitude($label)")
    val intent = Intent(Intent.ACTION_VIEW, uri)

    runCatching {
        context.startActivity(intent)
    }.onFailure { e ->
        Log.e("MapIntent", "Failed to open map", e)
        Toast.makeText(context, "Failed to open location: ${e.message}", Toast.LENGTH_SHORT).show()
    }
}

@Composable
fun ListingDetailScreen(
  modifier: Modifier = Modifier,
  listingViewModel: ListingViewModel = viewModel(factory = ListingViewModel.Factory),
  uiState: ListingUiState,
  navController: NavHostController
) {

    val selectedListing = uiState.selectedListing
    val selectedFoodStop = uiState.foodStopIDMap?.get(selectedListing?.foodStopId)
    val image = uiState.listingImageMap[selectedListing?.listingId]

    val context = LocalContext.current

    Column(
      modifier = modifier
        .fillMaxSize()
        .systemBarsPadding()
        .padding(start = 16.dp, end = 16.dp, top = 16.dp),
      verticalArrangement = Arrangement.SpaceBetween
    ) { 

      Column { 

        Text(
          text = selectedListing?.title ?: "",
          style = MaterialTheme.typography.headlineMedium,
          fontWeight = FontWeight.Bold
        )

        HorizontalDivider( modifier = Modifier.padding(4.dp) )
        
        Text(
          text = "Pick Up Location:",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold
        )

        GenericClickableCard(
          onClick = {
            listingViewModel.deselectListing()
            selectedFoodStop?.let { 
              listingViewModel.selectFoodStop(it)
            }
          },
          color = selectedFoodStop?.hexColor?.toColor() ?: MaterialTheme.colorScheme.primary
        ) {
          Text( 
            text = "${selectedFoodStop?.name}",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium
          )
        }

        Text(
          modifier = Modifier.clickable { 
            openLocation(
              latitude = selectedFoodStop?.lat ?: 0.0,
              longitude = selectedFoodStop?.lng ?: 0.0,
              context = context,
              label = "${selectedFoodStop?.name}"
            ) 
          },
          text = "${selectedFoodStop?.streetAddress}",
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
          text = "${selectedListing?.description}",
          style = MaterialTheme.typography.titleMedium
        ) 

        HorizontalDivider( modifier = Modifier.padding(4.dp) )

        Text(
          text = "Quantity Remaining:", 
          style = MaterialTheme.typography.titleSmall,
          fontWeight = FontWeight.Bold
        )

        Text(
          text = "${selectedListing?.quantityRemaining}", 
          style = MaterialTheme.typography.titleSmall,
          fontWeight = FontWeight.Bold
        )

        HorizontalDivider( modifier = Modifier.padding(4.dp) )

        val shape = RoundedCornerShape(16.dp)

        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(400.dp)
                .clip(shape)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant,
                    shape = shape
                ),
            contentAlignment = Alignment.Center
        ) {
          if (image != null) {
            Image(
              bitmap = image,
              contentDescription = null,
              contentScale = ContentScale.Crop
            )
          } else {
            Image(
              painter = painterResource(id = R.drawable.spoony),
              contentDescription = null
            )
          }
        }
      }

        Button(
            modifier = Modifier
              .align(Alignment.CenterHorizontally)
              .fillMaxWidth(0.8f)
              .weight(1f, false),
            onClick = {
              listingViewModel.reservationCreationInteract(navController = navController)
            },
            enabled = selectedFoodStop?.reservable == 1,
            colors = if (selectedFoodStop?.reservable == 1) {
              ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
              )
            } else ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surfaceDim,
                contentColor = MaterialTheme.colorScheme.onSurface
              )
        ) {
          Text(
            text = if (selectedFoodStop?.reservable == 1) "Begin Reservation" else "Unreservable"
          )
        }
      
    }  
}
