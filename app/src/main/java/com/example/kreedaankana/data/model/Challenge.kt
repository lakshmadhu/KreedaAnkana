package com.example.kreedaankana.data.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Challenge(
    val id: String = "",
    val senderId: String = "",
    val teamName: String = "",
    val message: String = "",
    val sport: String = "Cricket",      // Added for sport selection
    val status: String = "Pending",     // Pending or Matched
    val timestamp: Long = 0L,
    val timeString: String = "",        // Formatted time (e.g., 10:30 PM)
    val dateString: String = "",        // Formatted date (e.g., 14/05/2026)
    val acceptedBy: String = "",        // Stores the name of the team that accepted
    val opponentId: String = ""         // Stores the ID of the team that accepted
)

@IgnoreExtraProperties
data class ChallengeReply(
    val replyId: String = "",
    val senderId: String = "",
    val teamName: String = "",
    val message: String = "",
    val timestamp: Long = System.currentTimeMillis()
)