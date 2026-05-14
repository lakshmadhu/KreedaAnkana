package com.example.kreedaankana.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "teams")
data class Team(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val captainName: String,
    val contactNumber: String,
    val preferredSport: String // "Cricket" or "Volleyball"
)
