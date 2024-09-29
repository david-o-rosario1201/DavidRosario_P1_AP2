package edu.ucne.davidrosario_p1_ap2.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import edu.ucne.davidrosario_p1_ap2.presentation.venta.VentaListScreen
import edu.ucne.davidrosario_p1_ap2.presentation.venta.VentaScreen
import edu.ucne.davidrosario_p1_ap2.ui.theme.DavidRosario_P1_AP2Theme

@Composable
fun DavidRosario_P1_AP2NavHost(
    navHostController: NavHostController
){
    NavHost(
        startDestination = Screen.VentaListScreen,
        navController = navHostController
    ) {
        composable<Screen.VentaListScreen> {
            VentaListScreen(
                onVentaClick = { ventaId ->
                    navHostController.navigate(Screen.VentaScreen(ventaId))
                },
                onVentaAdd = {
                    navHostController.navigate(Screen.VentaScreen(0))
                }
            )
        }
        composable<Screen.VentaScreen> { argumentos ->
            val id= argumentos.toRoute<Screen.VentaScreen>().ventaId
            VentaScreen(
                ventaId = id,
                goVentaList = {
                    navHostController.navigate(Screen.VentaListScreen)
                }
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