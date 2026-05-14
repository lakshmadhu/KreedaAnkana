package com.example.kreedaankana.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kreedaankana.data.local.entities.Booking
import com.example.kreedaankana.ui.viewmodel.BookingViewModel
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@Composable
fun CalendarScreen(viewModel: BookingViewModel) {
    val allBookingsState = viewModel.allBookings.collectAsState(initial = emptyList())
    val allBookings = allBookingsState.value

    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }

    val timeSlots = listOf(
        "06:00 AM - 08:00 AM", "08:00 AM - 10:00 AM",
        "04:00 PM - 06:00 PM", "06:00 PM - 08:00 PM"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0B1020))
    ) {
        // --- HEADER (Matching May 2026 Style) ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF00F2FF).copy(alpha = 0.1f), Color.Transparent)
                    )
                )
                .padding(24.dp)
        ) {
            Column {
                Text("GROUND SLOTS", color = Color(0xFFFBC02D), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(
                    text = currentMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")),
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Black),
                    color = Color.White
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item {
                CalendarGrid(
                    currentMonth = currentMonth,
                    selectedDate = selectedDate,
                    onDateSelected = { selectedDate = it },
                    bookings = allBookings
                )
            }

            item {
                Text(
                    text = "Available Windows",
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = Color.White
                )
            }

            // --- TIME SLOT CARDS ---
            items(timeSlots) { slot ->
                val bookingForSlot = allBookings.find { it.date == selectedDate.toString() && it.timeSlot == slot }

                SlotCard(
                    time = slot,
                    booking = bookingForSlot,
                    onBookClick = { /* Handle navigation to BookingScreen if needed */ }
                )
            }
        }
    }
}

@Composable
fun SlotCard(time: String, booking: Booking?, onBookClick: () -> Unit) {
    val isBooked = booking != null

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.05f)
        ),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.AccessTime,
                contentDescription = null,
                tint = if (isBooked) Color.Gray else Color(0xFF90CAF9),
                modifier = Modifier.size(32.dp)
            )

            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(time, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(
                    text = if (isBooked) "Booked: ${booking?.teamName}" else "Available for Booking",
                    color = if (isBooked) Color(0xFFFBC02D) else Color(0xFFFBC02D), // Matches your yellow text
                    fontSize = 12.sp
                )
            }

            Button(
                onClick = onBookClick,
                enabled = !isBooked,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFBBDEFB),
                    contentColor = Color(0xFF0D47A1)
                )
            ) {
                Text(if (isBooked) "SECURED" else "SECURE", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun CalendarGrid(
    currentMonth: YearMonth,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    bookings: List<Booking>
) {
    val daysInMonth = currentMonth.lengthOfMonth()
    val firstDayOfMonth = currentMonth.atDay(1).dayOfWeek.value % 7
    val days = (1..daysInMonth).toList()
    val gridItems = List(daysInMonth + firstDayOfMonth) { index ->
        if (index < firstDayOfMonth) null else days[index - firstDayOfMonth]
    }

    Card(
        modifier = Modifier.padding(horizontal = 24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.05f)),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                listOf("S", "M", "T", "W", "T", "F", "S").forEach { day ->
                    Text(day, Modifier.weight(1f), textAlign = TextAlign.Center, color = Color.Gray, fontSize = 12.sp)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))

            // Explicitly defining 'day' as Int? to fix the mismatch error
            LazyVerticalGrid(
                columns = GridCells.Fixed(7),
                modifier = Modifier.height(240.dp),
                userScrollEnabled = false
            ) {
                items(gridItems) { day: Int? ->
                    if (day != null) {
                        val date = currentMonth.atDay(day)
                        val isSelected = date == selectedDate
                        val hasBooking = bookings.any { it.date == date.toString() }

                        Box(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .padding(4.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) Color(0xFF90CAF9) else Color.Transparent)
                                .clickable { onDateSelected(date) },
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = day.toString(),
                                    color = if (isSelected) Color.Black else Color.White
                                )
                                if (hasBooking && !isSelected) {
                                    Box(Modifier.size(4.dp).background(Color(0xFF00F2FF), CircleShape))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}