package edu.ucne.davidrosario_p1_ap2.presentation.venta

sealed interface VentaUiEvent {
    data class ventaIdChanged(val ventaId: Int): VentaUiEvent
    data class clienteChanged(val cliente: String): VentaUiEvent
    data class galonesChanged(val galones: String): VentaUiEvent
    data class descuentoGalonChanged(val descuentoGalon: String): VentaUiEvent
    data class precioChanged(val precio: String): VentaUiEvent
    data class totalDescontadoChanged(val totalDescontado: String): VentaUiEvent
    data class totalChanged(val total: String): VentaUiEvent
    data class selectedVenta(val ventaId: Int): VentaUiEvent
    data object Save: VentaUiEvent
    data object Delete: VentaUiEvent
}