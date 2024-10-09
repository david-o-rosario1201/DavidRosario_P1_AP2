package edu.ucne.davidrosario_p1_ap2.presentation.venta

import edu.ucne.davidrosario_p1_ap2.data.local.entities.VentaEntity

data class VentaUiState(
    val ventaId: Int? = null,
    val cliente: String = "",
    val galones: String = "",
    val descuentoGalon: String = "",
    val precio: String = "",
    val totalDescontado: String = "",
    val total: String = "",
    val ventas: List<VentaEntity> = emptyList(),
    val errorCliente: String? = "",
    val errorGalones: String? = "",
    val errorDescuentoGalon: String? = "",
    val errorPrecio: String? = "",
    val errorCalculoTotal: String? = "",
    val totalCalculado: Boolean = false,
    val success: Boolean = false
)
