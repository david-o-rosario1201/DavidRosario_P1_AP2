package edu.ucne.davidrosario_p1_ap2.presentation.navigation

import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import edu.ucne.davidrosario_p1_ap2.ui.theme.DavidRosario_P1_AP2Theme

@Composable
fun DavidRosario_P1_AP2NavHost(
    navHostController: NavHostController
){
    NavHost(
        startDestination = Screen.ListScreen,
        navController = navHostController
    ) {
        composable<Screen.ListScreen> {
            Button(
                onClick = {
                    navHostController.navigate(Screen.RegistroScreen(0))
                }
            ) {
                Text(
                    text = "Ir a la segunda pantalla"
                )
            }
        }
        composable<Screen.RegistroScreen> {

            Text(
                text = "Estas en la segunda pantalla"
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DavidRosario_P1_AP2NavHostPreview(){
    DavidRosario_P1_AP2Theme {
        val navHostController = rememberNavController()
        DavidRosario_P1_AP2NavHost(
            navHostController = navHostController
        )
    }
}