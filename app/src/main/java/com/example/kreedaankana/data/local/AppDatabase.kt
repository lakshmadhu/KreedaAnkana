package com.example.kreedaankana.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.kreedaankana.data.local.dao.BookingDao
import com.example.kreedaankana.data.local.dao.MatchDao
import com.example.kreedaankana.data.local.dao.TeamDao
import com.example.kreedaankana.data.local.entities.Booking
import com.example.kreedaankana.data.local.entities.Match
import com.example.kreedaankana.data.local.entities.Team

@Database(entities = [Team::class, Booking::class, Match::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun teamDao(): TeamDao
    abstract fun bookingDao(): BookingDao
    abstract fun matchDao(): MatchDao

    companion object {
        const val DATABASE_NAME = "kreeda_ankana_db"
    }
}
