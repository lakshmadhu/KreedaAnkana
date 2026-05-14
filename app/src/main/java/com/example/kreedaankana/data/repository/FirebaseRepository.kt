package com.example.kreedaankana.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

object FirebaseRepository {
    val auth = FirebaseAuth.getInstance()
    val database = FirebaseDatabase.getInstance().reference
}