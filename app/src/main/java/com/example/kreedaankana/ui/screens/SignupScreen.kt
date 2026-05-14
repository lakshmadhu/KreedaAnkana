package com.example.kreedaankana.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.kreedaankana.ui.navigation.Screen
import com.google.firebase.database.DataSnapshot // Fixes the 'snapshot' type error
import com.google.firebase.database.FirebaseDatabase

@Composable
fun SignupScreen(navController: NavController) {
    val context = LocalContext.current
    val database = FirebaseDatabase.getInstance("https://kreedaankana-2b4fe-default-rtdb.asia-southeast1.firebasedatabase.app/").reference

    var teamName by remember { mutableStateOf("") }
    var captainName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var village by remember { mutableStateOf("") }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFF0B1020))
        .padding(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "CREATE SQUAD",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Black,
                color = Color(0xFF00F2FF)
            )
            Text(
                text = "JOIN THE KREEDA-ANKANA ARENA",
                color = Color.White.copy(alpha = 0.6f)
            )

            Spacer(Modifier.height(32.dp))

            // Input Fields
            NeonInput(teamName, { teamName = it }, "Team Name")
            NeonInput(captainName, { captainName = it }, "Captain Name")
            NeonInput(phoneNumber, { phoneNumber = it }, "Contact Number")
            NeonInput(village, { village = it }, "Village")

            Spacer(Modifier.height(32.dp))

            Button(
                onClick = {
                    if (phoneNumber.length == 10 && teamName.isNotBlank()) {
                        val teamData = mapOf(
                            "teamName" to teamName,
                            "captainName" to captainName,
                            "contactNumber" to phoneNumber,
                            "village" to village
                        )

                        // Saves directly to Firebase using the phone number as the key
                        database.child("teams").child(phoneNumber).setValue(teamData)
                            .addOnSuccessListener {
                                Toast.makeText(context, "Squad Registered!", Toast.LENGTH_SHORT).show()
                                // Move to Home immediately
                                navController.navigate(Screen.Home.route + "/$phoneNumber")
                            }
                            .addOnFailureListener {
                                Toast.makeText(context, "Registration Failed", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        Toast.makeText(context, "Please enter 10 digits & Team Name", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00F2FF)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "REGISTER SQUAD",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            }

            TextButton(onClick = { navController.navigate(Screen.Login.route) }) {
                Text(
                    text = "Already have a squad? Login",
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
fun NeonInput(value: String, onValueChange: (String) -> Unit, label: String) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = Color.Gray) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedBorderColor = Color(0xFF00F2FF),
            cursorColor = Color(0xFF00F2FF)
        ),
        shape = RoundedCornerShape(12.dp),
        singleLine = true
    )
}