package edu.ucne.davidrosario_p1_ap2.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import edu.ucne.davidrosario_p1_ap2.data.local.entities.VentaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VentaDao {
    @Upsert
    suspend fun save(venta: VentaEntity)

    @Query("""
        SELECT * FROM Ventas
        WHERE ventaId = :id
        LIMIT 1
    """)
    suspend fun findVenta(id: Int): VentaEntity?

    @Query("""
        SELECT * FROM Ventas
        WHERE cliente = :cliente
        LIMIT 1
    """)
    suspend fun findCliente(cliente: String): VentaEntity?

    @Delete
    suspend fun delete(venta: VentaEntity)

    @Query("SELECT * FROM Ventas")
    fun getAll(): Flow<List<VentaEntity>>
}