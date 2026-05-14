package com.example.kreedaankana.ui.screens

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.animation.*
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.database.*
import java.util.Calendar

// Data class for Firebase
data class ScorePost(
    val id: String = "",
    val teamA: String = "",
    val teamB: String = "",
    val scoreA: Int = 0,
    val scoreB: Int = 0,
    val sport: String = "",
    val winner: String = "",
    val bestPlayer: String = "",
    val matchDate: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoreWallScreen() {
    val context = LocalContext.current
    val database = FirebaseDatabase.getInstance("https://kreedaankana-2b4fe-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("scores")

    var showForm by remember { mutableStateOf(false) }
    val scoreList = remember { mutableStateListOf<ScorePost>() }

    // Form States
    var teamA by remember { mutableStateOf("") }
    var teamB by remember { mutableStateOf("") }
    var scoreA by remember { mutableStateOf("") }
    var scoreB by remember { mutableStateOf("") }
    var bestPlayer by remember { mutableStateOf("") }
    var matchDate by remember { mutableStateOf("") }

    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(context, { _, y, m, d -> matchDate = "$d/${m + 1}/$y" },
        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
    )

    LaunchedEffect(Unit) {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                scoreList.clear()
                snapshot.children.mapNotNullTo(scoreList) { it.getValue(ScorePost::class.java) }
                scoreList.sortByDescending { it.timestamp }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    Scaffold(
        containerColor = Color(0xFF0B1020),
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showForm = !showForm },
                containerColor = Color(0xFF00F2FF),
                contentColor = Color.Black,
                icon = { Icon(if (showForm) Icons.Default.Close else Icons.Default.EmojiEvents, contentDescription = null) },
                text = { Text(if (showForm) "Close" else "Post Result", fontWeight = FontWeight.Bold) }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // --- HEADER ---
            item {
                Column {
                    Text(
                        text = "THE WALL OF FAME",
                        style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Black, letterSpacing = (-1).sp),
                        color = Color.White
                    )
                    Text("VPL Season Match Results", color = Color(0xFF00F2FF), fontWeight = FontWeight.Bold)
                }
            }

            // --- FORM SECTION ---
            item {
                AnimatedVisibility(visible = showForm, enter = expandVertically(), exit = shrinkVertically()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.05f)),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
                    ) {
                        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                CustomScoreField(teamA, { teamA = it }, "Team A", Modifier.weight(1f))
                                CustomScoreField(scoreA, { scoreA = it }, "Pts", Modifier.weight(0.4f))
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                CustomScoreField(teamB, { teamB = it }, "Team B", Modifier.weight(1f))
                                CustomScoreField(scoreB, { scoreB = it }, "Pts", Modifier.weight(0.4f))
                            }
                            CustomScoreField(bestPlayer, { bestPlayer = it }, "MVP ⭐")

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("Date: $matchDate", color = Color.White, modifier = Modifier.weight(1f))
                                IconButton(onClick = { datePickerDialog.show() }) {
                                    Icon(Icons.Default.Event, contentDescription = null, tint = Color(0xFF00F2FF))
                                }
                            }

                            Button(
                                onClick = {
                                    if (teamA.isNotBlank() && teamB.isNotBlank() && matchDate.isNotBlank()) {
                                        val sA = scoreA.toIntOrNull() ?: 0
                                        val sB = scoreB.toIntOrNull() ?: 0
                                        val winner = if (sA > sB) teamA else if (sB > sA) teamB else "Draw"
                                        val id = database.push().key ?: ""
                                        database.child(id).setValue(ScorePost(id, teamA, teamB, sA, sB, "Cricket", winner, bestPlayer, matchDate))
                                        showForm = false
                                        teamA = ""; teamB = ""; scoreA = ""; scoreB = ""; bestPlayer = ""; matchDate = ""
                                        Toast.makeText(context, "Victory Recorded! 🏆", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00F2FF))
                            ) {
                                Text("SUBMIT", color = Color.Black, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // --- EMPTY STATE ---
            if (scoreList.isEmpty()) {
                item {
                    Text(
                        "No results recorded. Be the first to post a victory!",
                        color = Color.Gray,
                        modifier = Modifier.fillMaxWidth().padding(top = 40.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }

            // --- LIST OF RESULTS ---
            items(scoreList) { score ->
                ScoreCard(score)
            }
        }
    }
}

@Composable
fun ScoreCard(score: ScorePost) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.05f)),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
    ) {
        Column(Modifier.padding(20.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(score.sport.uppercase(), color = Color(0xFF00F2FF), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Text(score.matchDate, color = Color.Gray, fontSize = 12.sp)
            }

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ScoreTeamColumn(score.teamA, score.scoreA, score.winner == score.teamA)
                Text("VS", fontWeight = FontWeight.Black, color = Color.White.copy(alpha = 0.2f), fontSize = 14.sp)
                ScoreTeamColumn(score.teamB, score.scoreB, score.winner == score.teamB)
            }

            HorizontalDivider(Modifier.padding(vertical = 16.dp), color = Color.White.copy(alpha = 0.1f))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFD700), modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(8.dp))
                Text("MVP: ", color = Color.Gray, fontSize = 12.sp)
                Text(score.bestPlayer, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun ScoreTeamColumn(name: String, score: Int, isWinner: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = name.uppercase(),
            style = MaterialTheme.typography.titleSmall,
            fontWeight = if (isWinner) FontWeight.Black else FontWeight.Medium,
            color = if (isWinner) Color.White else Color.Gray
        )
        Text(
            text = score.toString(),
            fontSize = 36.sp,
            fontWeight = FontWeight.Black,
            color = if (isWinner) Color(0xFF00F2FF) else Color.White.copy(alpha = 0.2f)
        )
        if (isWinner) {
            Surface(color = Color(0xFF00F2FF), shape = CircleShape) {
                Text("WINNER", Modifier.padding(horizontal = 8.dp, vertical = 2.dp), fontSize = 8.sp, fontWeight = FontWeight.Black, color = Color.Black)
            }
        }
    }
}

@Composable
fun CustomScoreField(value: String, onValueChange: (String) -> Unit, label: String, modifier: Modifier = Modifier) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = Color.Gray) },
        modifier = modifier,
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedBorderColor = Color(0xFF00F2FF)
        ),
        shape = RoundedCornerShape(12.dp)
    )
}