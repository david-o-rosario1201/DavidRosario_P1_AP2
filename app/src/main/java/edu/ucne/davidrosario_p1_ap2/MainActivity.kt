package edu.ucne.davidrosario_p1_ap2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import edu.ucne.davidrosario_p1_ap2.presentation.navigation.DavidRosario_P1_AP2NavHost
import edu.ucne.davidrosario_p1_ap2.ui.theme.DavidRosario_P1_AP2Theme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DavidRosario_P1_AP2Theme {
                val navHostController = rememberNavController()
                DavidRosario_P1_AP2NavHost(navHostController = navHostController)
            }
        }
    }
}