package com.example.kreedaankana.data.model



import androidx.annotation.Keep

@Keep
data class TeamProfile(
    val teamName: String = "",
    val sport: String = "",       // This was 'sportType' in your error, renamed to 'sport'
    val village: String = "",     // Added
    val captainName: String = "",
    val contactNumber: String = "", // This was 'phoneNumber' in your error
    val playerCount: String = "0",  // Added as String to avoid type mismatch
    val homeGround: String = "",    // Added
    val jerseyColor: String = "",   // Added
    val teamMotto: String = "",     // Added
    val foundedYear: String = "",   // Added
    val totalWins: Int = 0,
    val profileImageUrl: String? = null
)