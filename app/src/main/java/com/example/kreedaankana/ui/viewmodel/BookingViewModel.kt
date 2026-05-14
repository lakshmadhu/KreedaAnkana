package com.example.kreedaankana.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.kreedaankana.data.local.entities.Booking
import com.example.kreedaankana.data.repository.BookingRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class BookingViewModel(private val repository: BookingRepository) : ViewModel() {

    val allBookings: StateFlow<List<Booking>> = repository.allBookings
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // FIXED: Explicitly defined the type <BookingUiState> to clear the 'Cannot infer type' error
    private val _uiState = MutableStateFlow<BookingUiState>(BookingUiState.Idle)
    val uiState: StateFlow<BookingUiState> = _uiState.asStateFlow()

    fun bookSlot(
        teamName: String,
        sportType: String,
        date: String,
        timeSlot: String
    ) {
        _uiState.value = BookingUiState.Loading

        // 1. Clean out emojis so strings match perfectly across all devices
        val cleanSport = sportType.split(" ")[0].trim()

        // 2. Build a rigid unique identifier for the exact slot node in the cloud
        val slotKey = "${date}_${timeSlot.replace(" ", "")}_$cleanSport"

        val dbRef = FirebaseDatabase.getInstance("https://kreedaankana-2b4fe-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .reference
            .child("slot_registry")
            .child(slotKey)

        // 3. Execute Server-Side Transaction Evaluation (The Ultimate Referee)
        dbRef.runTransaction(object : Transaction.Handler {
            override fun doTransaction(currentData: MutableData): Transaction.Result {
                // Check if someone else already claimed this slot on the cloud server
                if (currentData.value != null) {
                    // Abort! The slot is taken. This flags onComplete with success = false
                    return Transaction.abort()
                }

                // Path is clear! Claim it atomically right now on the server
                val registryData = mapOf(
                    "teamName" to teamName,
                    "date" to date,
                    "timeSlot" to timeSlot,
                    "sport" to cleanSport,
                    "status" to "Confirmed"
                )
                currentData.value = registryData
                return Transaction.success(currentData)
            }

            override fun onComplete(error: DatabaseError?, committed: Boolean, snapshot: DataSnapshot?) {
                viewModelScope.launch {
                    if (error != null) {
                        _uiState.value = BookingUiState.Error("Network error: ${error.message}")
                    } else if (committed) {
                        // 4. CLOUD TRANSACTION SUCCESSFUL - Safe to insert into local Room DB now
                        try {
                            val booking = Booking(
                                teamName = teamName,
                                sportType = cleanSport,
                                date = date,
                                timeSlot = timeSlot,
                                slotOrder = getSlotOrder(timeSlot)
                            )

                            val localSuccess = repository.insertBooking(booking)
                            if (localSuccess) {
                                _uiState.value = BookingUiState.Success("Slot Secured Successfully! ⚡")
                            } else {
                                _uiState.value = BookingUiState.Success("Slot Secured on Cloud! (Synced locally)")
                            }
                        } catch (e: Exception) {
                            _uiState.value = BookingUiState.Error("Local sync failed: ${e.localizedMessage}")
                        }
                    } else {
                        // 5. CLOUD TRANSACTION REJECTED - Slot was already occupied in the cloud
                        val bookedBy = snapshot?.child("teamName")?.getValue(String::class.java) ?: "Another Team"
                        _uiState.value = BookingUiState.Error("Booking Failed! This slot is already taken by $bookedBy.")
                    }
                }
            }
        })
    }

    private fun getSlotOrder(timeSlot: String): Int {
        return when (timeSlot.trim()) {
            "06:00 AM - 08:00 AM" -> 1
            "08:00 AM - 10:00 AM" -> 2
            "04:00 PM - 06:00 PM" -> 4
            "06:00 PM - 08:00 PM" -> 5
            else -> 999
        }
    }

    fun resetUiState() {
        _uiState.value = BookingUiState.Idle
    }
}

// FIXED: Re-added the missing state sealed class that caused the compiler errors
sealed class BookingUiState {
    object Idle : BookingUiState()
    object Loading : BookingUiState()
    data class Success(val message: String) : BookingUiState()
    data class Error(val message: String) : BookingUiState()
}

class BookingViewModelFactory(private val repository: BookingRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BookingViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}