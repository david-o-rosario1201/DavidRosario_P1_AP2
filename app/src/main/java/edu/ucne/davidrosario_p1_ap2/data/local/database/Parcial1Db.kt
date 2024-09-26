package edu.ucne.davidrosario_p1_ap2.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.ucne.davidrosario_p1_ap2.data.local.entities.VentaEntity
import edu.ucne.davidrosario_p1_ap2.data.local.dao.VentaDao

@Database(
    version = 1,
    exportSchema = false,
    entities = [VentaEntity::class]
)
abstract class Parcial1Db: RoomDatabase() {
    abstract fun ventaDao(): VentaDao
}