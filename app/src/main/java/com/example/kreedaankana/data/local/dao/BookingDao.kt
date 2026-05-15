package com.example.kreedaankana.data.local.dao

import androidx.room.*
import com.example.kreedaankana.data.local.entities.Booking
import kotlinx.coroutines.flow.Flow

@Dao
interface BookingDao {


    @Query("SELECT * FROM bookings ORDER BY date ASC, slotOrder ASC")
    fun getAllBookings(): Flow<List<Booking>>


    @Query("SELECT * FROM bookings WHERE date = :date ORDER BY slotOrder ASC")
    fun getBookingsByDate(date: String): Flow<List<Booking>>


    @Query("""
        SELECT * FROM bookings
        WHERE date = :date AND timeSlot = :timeSlot
    """)
    suspend fun getBookingBySlot(
        date: String,
        timeSlot: String
    ): Booking?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertBooking(booking: Booking)

    @Delete
    suspend fun deleteBooking(booking: Booking)
}