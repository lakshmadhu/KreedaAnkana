package com.example.kreedaankana.data.repository

import com.example.kreedaankana.data.local.dao.BookingDao
import com.example.kreedaankana.data.local.entities.Booking
import kotlinx.coroutines.flow.Flow

class BookingRepository(private val bookingDao: BookingDao) {

    val allBookings: Flow<List<Booking>> = bookingDao.getAllBookings()

    fun getBookingsByDate(date: String): Flow<List<Booking>> =
        bookingDao.getBookingsByDate(date)

    suspend fun insertBooking(booking: Booking): Boolean {

        val existingBooking = bookingDao.getBookingBySlot(
            booking.date,
            booking.timeSlot
        )

        return if (existingBooking == null) {
            bookingDao.insertBooking(booking)
            true
        } else {
            false
        }
    }

    suspend fun deleteBooking(booking: Booking) {
        bookingDao.deleteBooking(booking)
    }
}