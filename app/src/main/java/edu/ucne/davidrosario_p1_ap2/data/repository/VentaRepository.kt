package edu.ucne.davidrosario_p1_ap2.data.repository

import edu.ucne.davidrosario_p1_ap2.data.local.entities.VentaEntity
import edu.ucne.davidrosario_p1_ap2.data.local.dao.VentaDao
import javax.inject.Inject

class VentaRepository @Inject constructor(
    private val ventaDao: VentaDao
) {
    suspend fun save(venta: VentaEntity) = ventaDao.save(venta)

    suspend fun findVenta(id: Int) = ventaDao.findVenta(id)

    suspend fun findCliente(cliente: String) = ventaDao.findCliente(cliente)

    suspend fun delete(venta: VentaEntity) = ventaDao.delete(venta)

    fun getAll() = ventaDao.getAll()
}