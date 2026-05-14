package com.example.kreedaankana.ui.screens

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.kreedaankana.ui.viewmodel.BookingUiState
import com.example.kreedaankana.ui.viewmodel.BookingViewModel
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(viewModel: BookingViewModel, userId: String) {
    val context = LocalContext.current
    val databaseRoot = FirebaseDatabase.getInstance("https://kreedaankana-2b4fe-default-rtdb.asia-southeast1.firebasedatabase.app/").reference

    var teamName by remember { mutableStateOf("") }
    var selectedSport by remember { mutableStateOf("Cricket") }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    val durations = listOf("06:00 AM - 08:00 AM", "08:00 AM - 10:00 AM", "04:00 PM - 06:00 PM", "06:00 PM - 08:00 PM")
    var selectedDuration by remember { mutableStateOf(durations[0]) }

    val sports = listOf("Cricket", "Football", "Volleyball", "Kabaddi")
    val datePickerDialog = DatePickerDialog(context, { _, y, m, d ->
        selectedDate = LocalDate.of(y, m + 1, d)
    }, selectedDate.year, selectedDate.monthValue - 1, selectedDate.dayOfMonth)

    // Collect UI State from ViewModel to show accurate Toast messages
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(userId) {
        if (userId.isNotEmpty()) {
            databaseRoot.child("teams").child(userId).child("teamName").get()
                .addOnSuccessListener { teamName = it.getValue(String::class.java) ?: "" }
        }
    }

    // Logic to handle Success/Error messages accurately
    LaunchedEffect(uiState) {
        when (uiState) {
            is BookingUiState.Success -> {
                Toast.makeText(context, (uiState as BookingUiState.Success).message, Toast.LENGTH_SHORT).show()
                viewModel.resetUiState() // Reset so message doesn't repeat
            }
            is BookingUiState.Error -> {
                Toast.makeText(context, (uiState as BookingUiState.Error).message, Toast.LENGTH_LONG).show()
                viewModel.resetUiState()
            }
            else -> {}
        }
    }

    Scaffold(containerColor = Color(0xFF0B1020)) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "SECURE YOUR SLOT 🔒",
                style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Black),
                color = Color.White
            )

            Text("Select Sport", color = Color(0xFFFFB800), fontWeight = FontWeight.Bold)
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                sports.forEach { sport ->
                    val sportEmoji = when(sport) {
                        "Cricket" -> "🏏"
                        "Football" -> "⚽"
                        "Volleyball" -> "🏐"
                        "Kabaddi" -> "🏃"
                        else -> ""
                    }
                    FilterChip(
                        selected = selectedSport == sport,
                        onClick = { selectedSport = sport },
                        label = { Text("$sport $sportEmoji") },
                        enabled = true,
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFF0052D4),
                            selectedLabelColor = Color.White,
                            containerColor = Color(0xFF1C2237),
                            labelColor = Color.Gray
                        )
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF161B2E), RoundedCornerShape(24.dp))
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                OutlinedTextField(
                    value = teamName,
                    onValueChange = { teamName = it },
                    label = { Text("Team Name", color = Color.Gray) },
                    leadingIcon = { Icon(Icons.Default.Groups, null, tint = Color(0xFF00F2FF)) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color(0xFF00F2FF),
                        unfocusedBorderColor = Color.Gray.copy(0.3f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                Surface(
                    onClick = { datePickerDialog.show() },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = Color.Transparent,
                    border = BorderStroke(1.dp, Color.Gray.copy(0.3f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.CalendarToday, null, tint = Color(0xFF00F2FF))
                        Spacer(Modifier.width(16.dp))
                        Text(
                            selectedDate.format(DateTimeFormatter.ofPattern("EEEE, dd MMM")),
                            color = Color.White
                        )
                    }
                }

                Text("Select Duration", color = Color.Gray, style = MaterialTheme.typography.labelLarge)

                durations.forEach { time ->
                    val isSelected = selectedDuration == time
                    Surface(
                        onClick = { selectedDuration = time },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).height(60.dp),
                        shape = RoundedCornerShape(16.dp),
                        color = if (isSelected) Color(0xFF0052D4).copy(0.2f) else Color(0xFF1C2237),
                        border = BorderStroke(1.dp, if (isSelected) Color(0xFF00F2FF) else Color.White.copy(0.05f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = null,
                                enabled = true,
                                colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF00F2FF))
                            )
                            Text(
                                time,
                                color = if (isSelected) Color(0xFF00F2FF) else Color.White,
                                fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                                modifier = Modifier.padding(start = 12.dp)
                            )
                        }
                    }
                }
            }

            Button(
                onClick = {
                    if (teamName.isNotBlank()) {
                        // The ViewModel now handles BOTH Firebase and Room in one go
                        viewModel.bookSlot(teamName, selectedSport, selectedDate.toString(), selectedDuration)
                    } else {
                        Toast.makeText(context, "Please enter team name", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(60.dp),
                enabled = uiState !is BookingUiState.Loading,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00F2FF)),
                shape = RoundedCornerShape(16.dp)
            ) {
                if (uiState is BookingUiState.Loading) {
                    CircularProgressIndicator(color = Color.Black, modifier = Modifier.size(24.dp))
                } else {
                    Text("CONFIRM RESERVATION", color = Color.Black, fontWeight = FontWeight.ExtraBold)
                }
            }
        }
    }
}