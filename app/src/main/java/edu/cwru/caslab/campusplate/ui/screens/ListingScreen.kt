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
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.compose.rememberLifecycleOwner
import androidx.lifecycle.whenResumed
import androidx.navigation.NavHostController
import edu.cwru.caslab.campusplate.R
import edu.cwru.caslab.campusplate.model.FoodStop
import edu.cwru.caslab.campusplate.ui.ActiveScreen
import edu.cwru.caslab.campusplate.ui.icons.menu
import kotlinx.coroutines.launch
import org.maplibre.compose.camera.CameraPosition
import org.maplibre.compose.camera.rememberCameraState
import org.maplibre.compose.expressions.dsl.const
import org.maplibre.compose.expressions.dsl.image
import org.maplibre.compose.expressions.value.SymbolAnchor
import org.maplibre.compose.layers.CircleLayer
import org.maplibre.compose.layers.SymbolLayer
import org.maplibre.compose.map.MaplibreMap
import org.maplibre.compose.sources.GeoJsonData
import org.maplibre.compose.sources.getBaseSource
import org.maplibre.compose.sources.rememberGeoJsonSource
import org.maplibre.compose.style.BaseStyle
import org.maplibre.spatialk.geojson.Feature
import org.maplibre.spatialk.geojson.FeatureCollection
import org.maplibre.spatialk.geojson.MultiPoint
import org.maplibre.spatialk.geojson.Point
import org.maplibre.spatialk.geojson.Position

fun ByteArray.toImageBitmap() = BitmapFactory.decodeByteArray(this, 0, size).asImageBitmap() // Helper

fun String.decodeBase64ToByteArray(): ByteArray { // Helper
   val byteArray = encodeToByteArray()
   return Base64.decode(byteArray, 0, byteArray.size)
}

@Composable
fun ListingCard(
    modifier: Modifier = Modifier,
    content: Listing,
    onClick: () -> Unit
) {
    Card(
      modifier = modifier.fillMaxWidth()
        .padding(bottom = 8.dp),
      onClick = onClick
    ) { 
      Row (
        modifier = modifier
      ) {
        Column (
          modifier = Modifier.weight(0.7f)
        ){ 
          Text( 
            text = content.title,
            style = MaterialTheme.typography.titleLarge
          )
          Text(
            text = "${content.quantityRemaining} Remaining",
            style = MaterialTheme.typography.bodyLarge
          )
        }

      }
    }
}

@Composable
fun ListingScreen(
  modifier: Modifier = Modifier,
  navController: NavHostController,
  email: String,
  credential: String,
  listingViewModel: ListingViewModel = viewModel()
) {
    val uiState by listingViewModel.uiState.collectAsState()   

    listingViewModel.setEmailCred(email = email, credential = credential)
    listingViewModel.getListings()
    listingViewModel.getFoodStops()

    if (uiState.selectedListing == null) {
      Box(
        modifier = modifier.systemBarsPadding()
      ) {
        ListingMap(
          modifier = modifier,
          navController = navController,
          listingViewModel = listingViewModel,
          uiState = uiState
        )

        Column(
          modifier = modifier
            .align(Alignment.TopEnd)
            .padding(8.dp)
        )
        {  
          FilledIconButton(
            onClick = { listingViewModel.menuButtonInteract() },
          ) {
            Icon(
              imageVector = menu,
              contentDescription = "Menu"
            )
          }
          DropdownMenu(
            expanded = uiState.menuExpanded,
            onDismissRequest = {
              listingViewModel.menuButtonInteract( toggle = false )
            }
          ) 
          {
            DropdownMenuItem(
              text = { Text("View Reservations") },
              onClick = {}
            )
          }
        }
      }
    } else {
      ListingDetailScreen(
        listingViewModel = listingViewModel,
        uiState = uiState
      )
    }
}


@Composable
fun ListingMap(
  modifier: Modifier = Modifier,
  navController: NavHostController,
  listingViewModel: ListingViewModel = viewModel(),
  uiState: ListingUiState
) {
  val scope = rememberCoroutineScope() 
  val configuration = LocalConfiguration.current

  @OptIn(ExperimentalMaterial3Api::class)
  val scaffoldState = rememberBottomSheetScaffoldState( 
      bottomSheetState = rememberStandardBottomSheetState(
          skipHiddenState = true
      )
  )
  val camera =
    rememberCameraState(
      firstPosition = CameraPosition(target = Position(latitude = 41.502, longitude = -81.606), zoom = 15.5)
    )
  Column(
    modifier = Modifier
      .systemBarsPadding()
  ) { 

    @OptIn(ExperimentalMaterial3Api::class)
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
                ListingCard(
                  content = listing,
                  onClick = {
                    listingViewModel.selectListing(listing)
                  }
                )
              }
            }
        }
    ) { innerPadding -> 
      val variant = if (isSystemInDarkTheme()) "dark" else "liberty"

      MaplibreMap(
        baseStyle = BaseStyle.Uri("https://tiles.openfreemap.org/styles/${variant}"),
        cameraState = camera,
      ) {  
        val markerIcon = painterResource(R.drawable.map_marker) 

        val source = rememberGeoJsonSource(
          data = GeoJsonData.Features( MultiPoint ( 
            uiState.foodStops?.map { Position( 
              latitude = it.lat,
              longitude = it.lng
            ) } ?: emptyList<Position>()
          ) )
        ) 

        SymbolLayer(
          id = "marker-layer",
          source = source,
          iconImage = image(markerIcon),
          iconAnchor = const(SymbolAnchor.Bottom),
          iconAllowOverlap = const(true),
          iconIgnorePlacement = const(false),
        )     
      }
    }         
  }
}
