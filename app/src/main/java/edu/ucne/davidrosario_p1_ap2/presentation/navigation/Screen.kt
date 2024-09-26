package edu.ucne.davidrosario_p1_ap2.presentation.navigation

import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    data object VentaListScreen : Screen()

    @Serializable
    data class VentaScreen(val ventaId: Int) : Screen()
}