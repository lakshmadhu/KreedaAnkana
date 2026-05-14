package com.example.kreedaankana.ui.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.kreedaankana.data.model.Challenge
import com.google.firebase.database.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChallengeScreen(navController: NavController, userId: String) {
    val context = LocalContext.current
    val database = FirebaseDatabase.getInstance("https://kreedaankana-2b4fe-default-rtdb.asia-southeast1.firebasedatabase.app/").reference

    var myTeamName by remember { mutableStateOf("") }
    var challengeInput by remember { mutableStateOf("") }
    var selectedSport by remember { mutableStateOf("Cricket") }

    var matchDate by remember { mutableStateOf(LocalDate.now()) }
    var matchTime by remember { mutableStateOf("04:30 PM") }

    val challenges = remember { mutableStateListOf<Challenge>() }
    var showDialog by remember { mutableStateOf(false) }

    // State for the Reply Dialog
    var showReplyDialog by remember { mutableStateOf(false) }
    var replyInput by remember { mutableStateOf("") }
    var selectedChallengeId by remember { mutableStateOf("") }

    val sports = listOf("Cricket", "Football", "Volleyball", "Kabaddi")

    val datePicker = DatePickerDialog(context, { _, y, m, d ->
        matchDate = LocalDate.of(y, m + 1, d)
    }, matchDate.year, matchDate.monthValue - 1, matchDate.dayOfMonth)

    val timePicker = TimePickerDialog(context, { _, h, min ->
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, h)
        cal.set(Calendar.MINUTE, min)
        matchTime = java.text.SimpleDateFormat("hh:mm a", Locale.getDefault()).format(cal.time)
    }, 16, 30, false)

    LaunchedEffect(userId) {
        database.child("teams").child(userId).child("teamName").get().addOnSuccessListener {
            myTeamName = it.getValue(String::class.java) ?: "Athlete"
        }

        database.child("challenges").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                challenges.clear()
                snapshot.children.forEach { child ->
                    child.getValue(Challenge::class.java)?.let { challenges.add(it) }
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    Scaffold(
        containerColor = Color(0xFF0B1020),
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showDialog = true },
                containerColor = Color(0xFF00F2FF),
                contentColor = Color.Black,
                shape = RoundedCornerShape(30.dp),
                icon = { Icon(Icons.Default.FlashOn, null) },
                text = { Text("ISSUE BATTLE CRY", fontWeight = FontWeight.Bold) }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp)) {
            Text(
                text = "CHALLENGE\nARENA ⚔️",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Black,
                color = Color.White,
                lineHeight = 40.sp
            )
            Text(
                text = "Set the date, pick the time, find a rival. ⚡",
                color = Color(0xFF00F2FF),
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(32.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(20.dp), modifier = Modifier.fillMaxSize()) {
                items(challenges.reversed()) { challenge ->
                    ChallengeBattleCard(challenge, currentUserId = userId) {
                        selectedChallengeId = challenge.id
                        replyInput = "" // Clear input so they can type freshly
                        showReplyDialog = true
                    }
                }
            }
        }

        // --- POPUP 1: ISSUE CHALLENGE ---
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                containerColor = Color(0xFF161B2E),
                title = { Text("SETUP YOUR BATTLE", color = Color.White, fontWeight = FontWeight.Black) },
                text = {
                    Column(
                        modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text("Select Sport", color = Color(0xFF00F2FF), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            sports.forEach { sport ->
                                FilterChip(
                                    selected = selectedSport == sport,
                                    onClick = { selectedSport = sport },
                                    label = { Text(sport) },
                                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Color(0xFF0052D4), selectedLabelColor = Color.White, labelColor = Color.Gray)
                                )
                            }
                        }

                        Text("Match Date", color = Color.Gray, fontSize = 12.sp)
                        Surface(
                            onClick = { datePicker.show() },
                            shape = RoundedCornerShape(12.dp),
                            color = Color.White.copy(0.05f),
                            border = BorderStroke(1.dp, Color.White.copy(0.1f)),
                            modifier = Modifier.fillMaxWidth().height(56.dp)
                        ) {
                            Row(Modifier.padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CalendarMonth, null, tint = Color(0xFF00F2FF))
                                Spacer(Modifier.width(12.dp))
                                Text(matchDate.format(DateTimeFormatter.ofPattern("EEEE, dd MMM")), color = Color.White)
                            }
                        }

                        Text("Match Time", color = Color.Gray, fontSize = 12.sp)
                        Surface(
                            onClick = { timePicker.show() },
                            shape = RoundedCornerShape(12.dp),
                            color = Color.White.copy(0.05f),
                            border = BorderStroke(1.dp, Color.White.copy(0.1f)),
                            modifier = Modifier.fillMaxWidth().height(56.dp)
                        ) {
                            Row(Modifier.padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.AccessTime, null, tint = Color(0xFF00F2FF))
                                Spacer(Modifier.width(12.dp))
                                Text(matchTime, color = Color.White)
                            }
                        }

                        OutlinedTextField(
                            value = challengeInput,
                            onValueChange = { challengeInput = it },
                            placeholder = { Text("Add a battle cry (e.g. Winner gets the ground!)", color = Color.Gray, fontSize = 12.sp) },
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedBorderColor = Color(0xFF00F2FF)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00F2FF)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        onClick = {
                            if (challengeInput.isNotBlank()) {
                                val id = database.child("challenges").push().key ?: ""
                                val newChallenge = mapOf(
                                    "id" to id,
                                    "senderId" to userId,
                                    "teamName" to myTeamName,
                                    "message" to challengeInput,
                                    "sport" to selectedSport,
                                    "status" to "Pending",
                                    "dateString" to matchDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                                    "timeString" to matchTime,
                                    "timestamp" to System.currentTimeMillis(),
                                    "acceptedBy" to ""
                                )
                                database.child("challenges").child(id).setValue(newChallenge)
                                challengeInput = ""
                                showDialog = false
                            }
                        }
                    ) { Text("POST CHALLENGE ⚔️", color = Color.Black, fontWeight = FontWeight.Bold) }
                }
            )
        }

        // --- POPUP 2: REPLY DIALOG (PURE USER INPUT) ---
        if (showReplyDialog) {
            AlertDialog(
                onDismissRequest = { showReplyDialog = false },
                containerColor = Color(0xFF161B2E),
                title = { Text("SEND YOUR REPLY", color = Color.White, fontWeight = FontWeight.Black) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("Type your match response:", color = Color.Gray, fontSize = 12.sp)
                        OutlinedTextField(
                            value = replyInput,
                            onValueChange = { replyInput = it },
                            placeholder = { Text("Type here (e.g., Yes we are coming!)", color = Color.Gray) },
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedBorderColor = Color(0xFF00F2FF)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00F2FF)),
                        shape = RoundedCornerShape(12.dp),
                        onClick = {
                            if (replyInput.isNotBlank()) {
                                val updates = mapOf(
                                    "status" to "Matched",
                                    "acceptedBy" to replyInput, // Saves exactly what they type raw
                                    "opponentId" to userId
                                )
                                database.child("challenges").child(selectedChallengeId).updateChildren(updates)
                                    .addOnSuccessListener {
                                        Toast.makeText(context, "Reply Posted! ⚔️", Toast.LENGTH_SHORT).show()
                                    }
                                showReplyDialog = false
                            }
                        }
                    ) {
                        Text("SEND REPLY", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            )
        }
    }
}

@Composable
fun ChallengeBattleCard(challenge: Challenge, currentUserId: String, onAccept: () -> Unit) {
    val sportEmoji = when(challenge.sport) {
        "Cricket" -> "🏏"
        "Football" -> "⚽"
        "Volleyball" -> "🏐"
        "Kabaddi" -> "🏃"
        else -> "🏆"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = if(challenge.status == "Matched") Color(0xFF00F2FF).copy(0.05f) else Color(0xFF161B2E)),
        border = BorderStroke(1.dp, if (challenge.status == "Matched") Color(0xFF00F2FF) else Color.White.copy(0.1f))
    ) {
        Column(Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(modifier = Modifier.size(44.dp), shape = CircleShape, color = if(challenge.status == "Matched") Color(0xFF00F2FF) else Color(0xFF1C2237)) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(challenge.teamName.take(1).uppercase(), fontWeight = FontWeight.Black, color = if(challenge.status == "Matched") Color.Black else Color.White)
                    }
                }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(challenge.teamName, fontWeight = FontWeight.Bold, color = Color.White)
                    Text("${challenge.sport} $sportEmoji", color = Color(0xFF00F2FF), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                if (challenge.status == "Matched") {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF00F2FF), modifier = Modifier.size(24.dp))
                }
            }

            Text("\"${challenge.message}\"", color = Color.White, fontStyle = FontStyle.Italic, modifier = Modifier.padding(vertical = 16.dp), fontSize = 15.sp)

            Row(
                modifier = Modifier.fillMaxWidth().background(Color.White.copy(0.05f), RoundedCornerShape(12.dp)).padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CalendarToday, null, tint = Color(0xFF00F2FF), modifier = Modifier.size(14.dp))
                    Text(" ${challenge.dateString}", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AccessTime, null, tint = Color(0xFF00F2FF), modifier = Modifier.size(14.dp))
                    Text(" ${challenge.timeString}", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                }
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = onAccept,
                modifier = Modifier.fillMaxWidth().height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00F2FF)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = if (challenge.status == "Matched") "RE-TYPE REPLY / ACCEPT ⚔️" else "ACCEPT & REPLY",
                    color = Color.Black,
                    fontWeight = FontWeight.Black
                )
            }

            if (challenge.status == "Matched") {
                Spacer(Modifier.height(12.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF00F2FF).copy(0.1f), RoundedCornerShape(12.dp))
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("REPLY RESPONSE:", color = Color(0xFF00F2FF), fontSize = 10.sp, fontWeight = FontWeight.Black)
                    Text(challenge.acceptedBy, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
        }
    }
}