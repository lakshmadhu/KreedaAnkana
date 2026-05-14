package com.example.kreedaankana.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookings")
data class Booking(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val teamName: String,
    val sportType: String, // "Cricket" or "Volleyball"
    val date: String, // YYYY-MM-DD
    val timeSlot: String, // e.g., "06:00 AM - 08:00 AM"
    val slotOrder: Int
)
