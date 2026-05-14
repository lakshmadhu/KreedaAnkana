package com.example.kreedaankana.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.kreedaankana.ui.navigation.Screen
import com.google.firebase.database.*

@Composable
fun HomeScreen(navController: NavController, userId: String) {
    val database = FirebaseDatabase.getInstance("https://kreedaankana-2b4fe-default-rtdb.asia-southeast1.firebasedatabase.app/").reference
    var teamName by remember { mutableStateOf("Athlete") }
    var challengeCount by remember { mutableIntStateOf(0) }

    LaunchedEffect(userId) {
        database.child("teams").child(userId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                teamName = snapshot.child("teamName").getValue(String::class.java) ?: "Athlete"
            }
            override fun onCancelled(error: DatabaseError) {}
        })

        database.child("challenges").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                challengeCount = snapshot.children.count {
                    it.child("status").getValue(String::class.java) == "Pending"
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    Scaffold(containerColor = Color(0xFF0B1020)) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Brush.verticalGradient(listOf(Color(0xFF0B1020), Color(0xFF1E3A8A).copy(alpha = 0.4f))))
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth().padding(24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Hello, $teamName ⚡", style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Black), color = Color.White)
                    Text("KREEDA-ANKANA PORTAL", style = MaterialTheme.typography.labelMedium, color = Color(0xFF00F2FF), fontWeight = FontWeight.Bold)
                }

                IconButton(
                    onClick = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    modifier = Modifier.background(Color.White.copy(alpha = 0.1f), CircleShape)
                ) {
                    Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Logout", tint = Color(0xFFFF4B4B))
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item {
                    // FIXED: Now passing userId to the Calendar route
                    MainActionCard(
                        "Reserve Ground",
                        "Book your village slot",
                        Icons.Default.SportsCricket,
                        Brush.linearGradient(listOf(Color(0xFF00C6FF), Color(0xFF0072FF)))
                    ) {
                        navController.navigate("${Screen.Calendar.route}/$userId")
                    }
                }

                item {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        SmallQuickCard(Modifier.weight(1f), "Challenges", Icons.Default.EmojiEvents, Color(0xFFFFD700), challengeCount > 0) {
                            navController.navigate("${Screen.ChallengeBoard.route}/$userId")
                        }
                        SmallQuickCard(Modifier.weight(1f), "Score Wall", Icons.Default.Poll, Color(0xFF00F2FF), false) {
                            navController.navigate(Screen.ScoreWall.route)
                        }
                    }
                }

                item {
                    // FIXED: Now passing userId to the Profile route
                    DashboardListTile("Team Roster", "Manage village lineup", Icons.Default.Groups) {
                        navController.navigate("${Screen.Profile.route}/$userId")
                    }
                }
            }
        }
    }
}

// Helpers (MainActionCard, SmallQuickCard, DashboardListTile) remain the same as your previous code
@Composable
fun MainActionCard(title: String, subtitle: String, icon: ImageVector, gradient: Brush, onClick: () -> Unit) {
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth().height(140.dp), shape = RoundedCornerShape(24.dp)) {
        Box(modifier = Modifier.fillMaxSize().background(gradient).padding(20.dp)) {
            Column(Modifier.align(Alignment.CenterStart)) {
                Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black, color = Color.White)
                Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.8f))
            }
            Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(90.dp).align(Alignment.BottomEnd).alpha(0.2f), tint = Color.White)
        }
    }
}

@Composable
fun SmallQuickCard(modifier: Modifier, title: String, icon: ImageVector, color: Color, hasBadge: Boolean, onClick: () -> Unit) {
    Card(onClick = onClick, modifier = modifier.height(110.dp), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.05f)), border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))) {
        Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Column(Modifier.align(Alignment.CenterStart)) {
                Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(28.dp))
                Spacer(Modifier.height(12.dp))
                Text(title, fontWeight = FontWeight.Bold, color = Color.White)
            }
            if (hasBadge) { Box(modifier = Modifier.size(10.dp).align(Alignment.TopEnd).background(Color(0xFFFF0055), CircleShape)) }
        }
    }
}

@Composable
fun DashboardListTile(title: String, subtitle: String, icon: ImageVector, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        color = Color.White.copy(alpha = 0.05f),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
    ) {
        Row(Modifier.padding(16.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(44.dp).background(Color(0xFF00F2FF).copy(alpha = 0.1f), CircleShape), contentAlignment = Alignment.Center) {
                Icon(imageVector = icon, contentDescription = null, tint = Color(0xFF00F2FF), modifier = Modifier.size(20.dp))
            }
            Spacer(Modifier.width(16.dp))
            Column {
                Text(title, fontWeight = FontWeight.Bold, color = Color.White)
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.4f))
            }
            Spacer(Modifier.weight(1f))
            Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = Color.White.copy(alpha = 0.3f))
        }
    }
}