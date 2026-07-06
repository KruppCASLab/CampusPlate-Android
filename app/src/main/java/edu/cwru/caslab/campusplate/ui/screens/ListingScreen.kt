package edu.cwru.caslab.campusplate.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import edu.cwru.caslab.campusplate.model.Listing
import android.graphics.BitmapFactory
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.navigation.NavHostController
import edu.cwru.caslab.campusplate.model.FoodStop
import edu.cwru.caslab.campusplate.ui.components.GenericClickableCard
import edu.cwru.caslab.campusplate.ui.icons.getMarkerImage
import edu.cwru.caslab.campusplate.ui.icons.menu
import kotlinx.coroutines.delay
import org.maplibre.compose.camera.CameraPosition
import org.maplibre.compose.camera.rememberCameraState
import org.maplibre.compose.expressions.dsl.const
import org.maplibre.compose.expressions.dsl.image
import org.maplibre.compose.expressions.value.SymbolAnchor
import org.maplibre.compose.layers.SymbolLayer
import org.maplibre.compose.map.MaplibreMap
import org.maplibre.compose.sources.GeoJsonData
import org.maplibre.compose.sources.rememberGeoJsonSource
import org.maplibre.compose.style.BaseStyle
import org.maplibre.compose.util.ClickResult
import org.maplibre.spatialk.geojson.Point
import org.maplibre.spatialk.geojson.Position
import kotlin.time.Duration.Companion.seconds

fun ByteArray.toImageBitmap() = BitmapFactory.decodeByteArray(this, 0, size).asImageBitmap() // Helper

fun String.decodeBase64ToByteArray(): ByteArray { // Helper
  val byteArray = encodeToByteArray()
  return Base64.decode(byteArray, 0, byteArray.size)
}

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

@Composable
fun ListingCard(
    modifier: Modifier = Modifier,
    listing: Listing,
    foodStop: FoodStop?,
    color: String = "000000",
    onClick: () -> Unit
) {
  GenericClickableCard(
    modifier = modifier,
    color = color.toColor(),
    onClick = onClick,
  ) { 
      Text( 
        text = listing.title,
        style = MaterialTheme.typography.titleLarge
      )
      Text(
        text = "${listing.quantityRemaining} Remaining at ${foodStop?.name ?: "Unknown"}",
        style = MaterialTheme.typography.bodyLarge
      )
  } 
}

@Composable
fun ListingScreen(
  modifier: Modifier = Modifier,
  navHostController: NavHostController,
  email: String,
  credential: String,
  listingViewModel: ListingViewModel = viewModel()
) {
    val uiState by listingViewModel.uiState.collectAsState()


    LaunchedEffect(Unit) {
    listingViewModel.setEmailCred(email = email, credential = credential)
      while(true) {
          listingViewModel.getListings()
          listingViewModel.getFoodStops()
          delay(15.seconds)
      }
    }

    if (uiState.selectedListing == null) {
      Box(
        modifier = modifier.systemBarsPadding()
      ) {
        ListingMap(
          modifier = modifier,
          navController = navHostController,
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
              onClick = { listingViewModel.reservationViewInteract(navHostController = navHostController) }
            )
          }
        }
      }
    } else {
      ListingDetailScreen(
        listingViewModel = listingViewModel,
        uiState = uiState,
        navController = navHostController
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
                val foodStop = uiState.foodStopIDMap?.get(listing.foodStopId)
                ListingCard(
                  listing = listing,
                  foodStop = foodStop,
                  color = foodStop?.hexColor ?: "000000",
                  onClick = {
                    listingViewModel.selectListing(listing)
                  }
                )

              }
            }
        }
    ) { innerPadding -> 
      val variant = if (isSystemInDarkTheme()) "fiord" else "liberty"

      MaplibreMap(
        baseStyle = BaseStyle.Uri("https://tiles.openfreemap.org/styles/${variant}"),
        cameraState = camera,
      ) {  

        // Serialization baffles me and seems like a rabbit hole
        //  I should definitley NOT go down...
        
        /* WARN: BROKEN
        val source1 = rememberGeoJsonSource(
          data = GeoJsonData.Features( FeatureCollection(
            uiState.foodStops?.map { Feature (
              geometry = Point(
                Position( latitude = it.lat, longitude = it.lng)
              ),
              properties = mapOf(
                "color" to JsonPrimitive(it.hexColor),
              )
            ) } ?: emptyList()
          ) )
        )
        */  

        for( foodstop in uiState.foodStops ?: emptyList()) {

          val source = rememberGeoJsonSource(
          data = GeoJsonData.Features( Point ( Position (
            latitude = foodstop.lat,
            longitude = foodstop.lng
          ) ) ) ) 


          SymbolLayer(
            id = "${foodstop.foodStopId}-marker",
            source = source,
            iconImage = image(
              rememberVectorPainter(
                image = getMarkerImage(
                  size = 72.dp,
                  color = foodstop.hexColor.toColor()
            ) ) ),
            iconAnchor = const(SymbolAnchor.Bottom),
            iconAllowOverlap = const(true),
            iconIgnorePlacement = const(false),
            onClick = { features ->
              ClickResult.Consume
            },
          )     

        }
        
      }
    }         
  }
}
