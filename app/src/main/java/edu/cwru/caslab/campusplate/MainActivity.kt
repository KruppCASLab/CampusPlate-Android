package edu.cwru.caslab.campusplate

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import edu.cwru.caslab.campusplate.repository.StoredCredentialRepository
import edu.cwru.caslab.campusplate.ui.LoginViewModel
import edu.cwru.caslab.campusplate.ui.screens.CameraScreen
import edu.cwru.caslab.campusplate.ui.screens.MainScreen
import edu.cwru.caslab.campusplate.ui.theme.CampusPlateTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CampusPlateTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CampusPlatePreview() {
    CampusPlateTheme {
        //CampusPlateLogin()
    }
}
