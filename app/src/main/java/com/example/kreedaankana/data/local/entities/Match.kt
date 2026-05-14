package com.example.kreedaankana.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "matches")
data class Match(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val teamA: String,
    val teamB: String,
    val sportType: String,
    val scoreA: String,
    val scoreB: String,
    val date: String,
    val result: String
)
