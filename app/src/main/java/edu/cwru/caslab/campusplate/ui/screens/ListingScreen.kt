package edu.cwru.caslab.campusplate.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import edu.cwru.caslab.campusplate.model.Listing

import android.graphics.BitmapFactory
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.cwru.caslab.campusplate.ui.ListingUiState
import edu.cwru.caslab.campusplate.ui.ListingViewModel
import kotlin.io.encoding.Base64
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch

fun ByteArray.toImageBitmap() = BitmapFactory.decodeByteArray(this, 0, size).asImageBitmap() // Helper

fun String.decodeBase64ToByteArray(): ByteArray { // Helper
   val byteArray = encodeToByteArray()
   return Base64.decode(byteArray, 0, byteArray.size)
}

@Composable
fun ListingCard(
    modifier: Modifier = Modifier,
    content: Listing,
) {
    Card(
      modifier = modifier.fillMaxWidth()
        .padding(bottom = 8.dp)
    ) { 
      Row (
        modifier = modifier
      ) {

        Column (
          modifier = Modifier.weight(0.7f)
        ){ 
          Text( text = content.title )
          Text( text = "${content.quantityRemaining} Remaining" )
        }

      }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ListingScreen(
  modifier : Modifier = Modifier,
  navController: NavHostController,
  email: String,
  credential: String,
  listingViewModel: ListingViewModel = viewModel()
) {
   
    val uiState by listingViewModel.uiState.collectAsState()
    val configuration = LocalConfiguration.current
    val scope = rememberCoroutineScope() 
    val scaffoldState = rememberBottomSheetScaffoldState( 
        bottomSheetState = rememberStandardBottomSheetState(
            skipHiddenState = true
        )
    )

    listingViewModel.setEmailCred(email = email, credential = credential)
    listingViewModel.getListings()

    Column(
      modifier = Modifier
    ) {
      Spacer(
        modifier = Modifier.padding(top = 48.dp)
      )

      BottomSheetScaffold(
          scaffoldState = scaffoldState,
          sheetPeekHeight = configuration.screenHeightDp.dp / 3,
          sheetContent = {
              LazyColumn (
                  Modifier.fillMaxSize()
                    .padding(start = 8.dp, end = 8.dp),
                  horizontalAlignment = Alignment.CenterHorizontally,
              ) {
                items(uiState.listings ?: emptyList()) { listing ->
                  ListingCard(content = listing)
                }
              }
          }
      ) { innerPadding ->
        Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
      ) {
        Text(
          text = "Map Goes Here" //TODO
        )      
      }         
      }
  }
}
