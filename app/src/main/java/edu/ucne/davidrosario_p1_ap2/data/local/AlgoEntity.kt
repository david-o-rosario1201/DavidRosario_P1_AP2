package edu.ucne.davidrosario_p1_ap2.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Algos")
data class AlgoEntity(
    @PrimaryKey
    val id: Int? = null
)
