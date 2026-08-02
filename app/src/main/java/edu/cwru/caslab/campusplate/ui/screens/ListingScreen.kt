package edu.cwru.caslab.campusplate.ui.screens

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import edu.cwru.caslab.campusplate.model.Listing
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.core.app.ActivityCompat
import androidx.navigation.NavHostController
import edu.cwru.caslab.campusplate.model.FoodStop
import edu.cwru.caslab.campusplate.ui.SheetActiveView
import edu.cwru.caslab.campusplate.ui.components.GenericClickableCard
import edu.cwru.caslab.campusplate.ui.components.TopNavigationBar
import edu.cwru.caslab.campusplate.ui.icons.getMarkerImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
    val scope = rememberCoroutineScope() 
    
    @OptIn(ExperimentalMaterial3Api::class)
    val scaffoldState = rememberBottomSheetScaffoldState( 
        bottomSheetState = rememberStandardBottomSheetState(
            skipHiddenState = true
        )
    )

    LaunchedEffect(Unit) {
    listingViewModel.setEmailCred(email = email, credential = credential)
      while(true) {
          listingViewModel.getListings()
          listingViewModel.getFoodStops()
          delay(15.seconds)
      }
    }

    Box(
      modifier = modifier.systemBarsPadding()
    ) {

      @OptIn(ExperimentalMaterial3Api::class)
      ListingMap(
        modifier = modifier,
        navController = navHostController,
        listingViewModel = listingViewModel,
        scaffoldState = scaffoldState,
        uiState = uiState
      )
        if (uiState.foodStops?.any { it.managed != 0 } == true
          && uiState.sheetActiveView != SheetActiveView.Manage) {
          FilledIconButton(
            onClick = { 
              listingViewModel.manageFoodStopsInteract(navHostController) 

              @OptIn(ExperimentalMaterial3Api::class)
              scope.launch { scaffoldState.bottomSheetState.expand() }
            },
            modifier = modifier
              .align(Alignment.TopEnd)
              .padding(8.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Add,
              contentDescription = "Create Listing"
            )
          }  
      }
    }
  } 



@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ListingMap(
  modifier: Modifier = Modifier,
  navController: NavHostController,
  listingViewModel: ListingViewModel = viewModel(),
  scaffoldState: BottomSheetScaffoldState,
  uiState: ListingUiState
) {
  val scope = rememberCoroutineScope() 
  val configuration = LocalConfiguration.current
  var selectedIndex by remember { mutableIntStateOf(0) } 

  val camera =
    rememberCameraState(
      firstPosition = CameraPosition(target = Position(latitude = 41.502, longitude = -81.606), zoom = 15.5)
    )

  @OptIn(ExperimentalMaterial3Api::class)
  BackHandler(
    enabled = scaffoldState.bottomSheetState.currentValue == SheetValue.Expanded
  ) { 
    scope.launch { scaffoldState.bottomSheetState.partialExpand() }
  }

  Column(
    modifier = Modifier
      .systemBarsPadding()
  ) { 

    @OptIn(ExperimentalMaterial3Api::class)
    BottomSheetScaffold( 
        scaffoldState = scaffoldState,
        sheetPeekHeight = configuration.screenHeightDp.dp / 3,
        sheetContent = {

          when (uiState.sheetActiveView) {

            SheetActiveView.Listing, SheetActiveView.Reservation ->  {

            Column(
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              
                SingleChoiceSegmentedButtonRow(
                  modifier = modifier.fillMaxWidth(0.95f)
                    .padding(bottom = 8.dp),
                  
                ) { 
                  SegmentedButton(
                    label = { Text("Listings") },
                    onClick = { listingViewModel.toggleListingView() },
                    selected = uiState.sheetActiveView == SheetActiveView.Listing,
                    shape = SegmentedButtonDefaults.itemShape(
                      index = 0,
                      count = 2
                    ),
                  )
                  SegmentedButton(
                    label = { Text("Reservations") },
                    onClick = { listingViewModel.toggleListingView() },
                    selected = uiState.sheetActiveView == SheetActiveView.Reservation,
                    shape = SegmentedButtonDefaults.itemShape(
                      index = 1,
                      count = 2
                    ),
                  )
                }

                when(uiState.sheetActiveView) {

                  SheetActiveView.Listing -> LazyColumn (
                      Modifier.fillMaxSize()
                        .padding(start = 8.dp, end = 8.dp),
                      horizontalAlignment = Alignment.CenterHorizontally,
                  ) {
                    items(uiState.listings?.filter { 
                      //if (uiState.selectedFoodStop != null) { it.foodStopId == uiState.selectedFoodStop.foodStopId }
                      //  else true
                      true // TODO: for future use (Filter)
                    } ?: emptyList()) { listing ->
                      val foodStop = uiState.foodStopIDMap?.get(listing.foodStopId)
                      ListingCard(
                        listing = listing,
                        foodStop = foodStop,
                        color = foodStop?.hexColor ?: "000000",
                        onClick = {
                          listingViewModel.selectListing(listing)
                          listingViewModel.getListingImage(listing)
                          scope.launch { scaffoldState.bottomSheetState.expand() }
                        }
                      )
                    }

                  }

                  else -> ReservationScreen(
                    email = uiState.email,
                    credential = uiState.credential,
                    navHostController = navController
                  )

                }

              }
            }

            SheetActiveView.FoodStop -> {
              Column {
                TopNavigationBar(
                  text = "Food Stop Info",
                  onClick = {
                    listingViewModel.selectFoodStop(null)
                    scope.launch { scaffoldState.bottomSheetState.partialExpand() }
                  }
                )
                FoodStopInfoScreen(
                  foodStop = uiState.selectedFoodStop 
                )
              }
            }

            SheetActiveView.ListingInfo -> {
              Column {
                TopNavigationBar(
                  text = "Listing Info",
                  onClick = {
                    listingViewModel.deselectListing()
                    scope.launch { scaffoldState.bottomSheetState.partialExpand() }
                  }
                )
                ListingDetailScreen(
                  listingViewModel = listingViewModel,
                  uiState = uiState,
                  navController = navController
                )
              }
            }

          SheetActiveView.Manage -> {

            ManageFoodStopsScreen(
              email = uiState.email,
              credential = uiState.credential,
              navHostController = navController,
              sheetEnabledOverride = scaffoldState.bottomSheetState.currentValue == SheetValue.PartiallyExpanded,
              onBackInteract = {
                listingViewModel.deselectListing()
                scope.launch { scaffoldState.bottomSheetState.partialExpand() }
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
            onClick = { 
              scope.launch {
                camera.animateTo( CameraPosition(target = Position( latitude = foodstop.lat, longitude = foodstop.lng ), zoom = camera.position.zoom ) )
                listingViewModel.selectFoodStop(foodstop)
                scaffoldState.bottomSheetState.expand() 
              }
              ClickResult.Consume
            },
          )     

        }
        
      }
    }         
  }
}
