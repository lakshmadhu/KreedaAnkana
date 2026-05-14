package com.example.kreedaankana.ui.screens

import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.database.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamProfileScreen(navController: NavController, userId: String) {
    val context = LocalContext.current
    val database = FirebaseDatabase.getInstance("https://kreedaankana-2b4fe-default-rtdb.asia-southeast1.firebasedatabase.app/").reference

    // Identity State
    var teamName by remember { mutableStateOf("") }
    var village by remember { mutableStateOf("") }
    var sport by remember { mutableStateOf("Cricket 🏏") }
    var captainName by remember { mutableStateOf("") }
    var playerCount by remember { mutableStateOf("") }
    var jerseyColor by remember { mutableStateOf("") }
    var teamMotto by remember { mutableStateOf("") }

    // Dropdown State
    var expanded by remember { mutableStateOf(false) }
    val sportsList = listOf("Cricket 🏏", "Football ⚽", "Volleyball 🏐", "Kabaddi 🏃", "Badminton 🏸")

    LaunchedEffect(userId) {
        database.child("teams").child(userId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    teamName = snapshot.child("teamName").getValue(String::class.java) ?: ""
                    village = snapshot.child("village").getValue(String::class.java) ?: ""
                    sport = snapshot.child("sport").getValue(String::class.java) ?: "Cricket 🏏"
                    captainName = snapshot.child("captainName").getValue(String::class.java) ?: ""
                    playerCount = snapshot.child("playerCount").getValue(String::class.java) ?: ""
                    jerseyColor = snapshot.child("jerseyColor").getValue(String::class.java) ?: ""
                    teamMotto = snapshot.child("teamMotto").getValue(String::class.java) ?: ""
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    Scaffold(
        containerColor = Color(0xFF070B19),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "TEAM IDENTITY 👤",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF070B19))
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Arc / Circular Profile Visual Placeholder from image
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF111E38)),
                contentAlignment = Alignment.TopCenter
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .background(Color(0xFF00F2FF).copy(alpha = 0.3f))
                )
            }

            Spacer(Modifier.height(16.dp))

            // Large Title Header
            Text(
                text = teamName.uppercase(),
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                color = Color.White
            )

            Spacer(Modifier.height(28.dp))

            // --- SECTION 1: CORE IDENTITY ---
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "CORE IDENTITY",
                    color = Color(0xFF00F2FF),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )

                ProfileField(value = teamName, onValueChange = { teamName = it }, label = "Team Name", icon = null)
                ProfileField(value = teamMotto, onValueChange = { teamMotto = it }, label = "Motto / Slogan", icon = null)

                // Dropdown matching image styling
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = sport,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Primary Sport", color = Color.Gray) },
                        modifier = Modifier.fillMaxWidth().menuAnchor(type = MenuAnchorType.PrimaryNotEditable, enabled = true),
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color(0xFF00F2FF),
                            unfocusedBorderColor = Color.White.copy(0.15f),
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedLabelColor = Color(0xFF00F2FF),
                            unfocusedLabelColor = Color.Gray,
                            focusedTrailingIconColor = Color(0xFF00F2FF),
                            unfocusedTrailingIconColor = Color.Gray
                        ),
                        shape = RoundedCornerShape(14.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.background(Color(0xFF111726))
                    ) {
                        sportsList.forEach { selection ->
                            DropdownMenuItem(
                                text = { Text(selection, color = Color.White) },
                                onClick = {
                                    sport = selection
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(32.dp))

            // --- SECTION 2: SQUAD SPECS ---
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "SQUAD SPECS",
                    color = Color(0xFF00F2FF),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )

                ProfileField(value = captainName, onValueChange = { captainName = it }, label = "Captain Name", icon = Icons.Default.Person)
                ProfileField(value = village, onValueChange = { village = it }, label = "Home Village", icon = Icons.Default.LocationOn)

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Box(Modifier.weight(1f)) {
                        ProfileField(value = playerCount, onValueChange = { playerCount = it }, label = "Squad Size", icon = Icons.Default.Numbers)
                    }
                    Box(Modifier.weight(1f)) {
                        ProfileField(value = jerseyColor, onValueChange = { jerseyColor = it }, label = "Jersey Theme", icon = Icons.Default.Palette)
                    }
                }
            }

            Spacer(Modifier.height(40.dp))

            // Action Button
            Button(
                onClick = {
                    val profile = mapOf(
                        "teamName" to teamName,
                        "village" to village,
                        "sport" to sport,
                        "captainName" to captainName,
                        "playerCount" to playerCount,
                        "jerseyColor" to jerseyColor,
                        "teamMotto" to teamMotto,
                        "contactNumber" to userId
                    )
                    database.child("teams").child(userId).updateChildren(profile)
                        .addOnSuccessListener { Toast.makeText(context, "SQUAD DATA SYNCED! ⚡", Toast.LENGTH_SHORT).show() }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00F2FF))
            ) {
                Text("SAVE SQUAD DATA", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun ProfileField(value: String, onValueChange: (String) -> Unit, label: String, icon: ImageVector?) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = Color.Gray) },
        modifier = Modifier.fillMaxWidth(),
        leadingIcon = if (icon != null) {
            { Icon(icon, null, tint = Color(0xFF00F2FF), modifier = Modifier.size(20.dp)) }
        } else null,
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedBorderColor = Color(0xFF00F2FF),
            unfocusedBorderColor = Color.White.copy(0.15f),
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedLabelColor = Color(0xFF00F2FF),
            unfocusedLabelColor = Color.Gray,
            cursorColor = Color(0xFF00F2FF)
        ),
        shape = RoundedCornerShape(14.dp)
    )
}