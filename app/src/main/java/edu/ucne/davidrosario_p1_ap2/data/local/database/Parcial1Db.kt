package edu.ucne.davidrosario_p1_ap2.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.ucne.davidrosario_p1_ap2.data.local.AlgoEntity
import edu.ucne.davidrosario_p1_ap2.data.local.dao.AlgoDao

@Database(
    version = 1,
    exportSchema = false,
    entities = [AlgoEntity::class]
)
abstract class Parcial1Db: RoomDatabase() {
    abstract fun algoDao(): AlgoDao
}