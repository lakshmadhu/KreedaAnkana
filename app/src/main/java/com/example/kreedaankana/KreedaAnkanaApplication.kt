package com.example.kreedaankana

import android.app.Application
import androidx.room.Room
import com.example.kreedaankana.data.local.AppDatabase

class KreedaAnkanaApplication : Application() {

    lateinit var database: AppDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        ).build()
    }
}
