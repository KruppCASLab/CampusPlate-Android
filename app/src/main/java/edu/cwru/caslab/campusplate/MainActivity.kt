package edu.cwru.caslab.campusplate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import edu.cwru.caslab.campusplate.ui.theme.CampusPlateTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CampusPlateTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CampusPlateLogin(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun CampusPlateLogin(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        var value by remember { mutableStateOf("") }

        Text(
            text = "Campus Plate Login"
        )
        TextField(
            value = value,
            singleLine = true,
            modifier = modifier,
            onValueChange = {},
            label = { Text(text = "Username") },
            keyboardOptions = KeyboardOptions.Default
        )
        Button(
            onClick = {}
        ) {
            Text(text = "Send PIN")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CampusPlatePreview() {
    CampusPlateTheme {
        CampusPlateLogin()
    }
}