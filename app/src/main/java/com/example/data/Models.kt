package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: Int = 1,
    val fullName: String,
    val phoneNumber: String,
    val city: String,
    val batchesCount: Int = 12,
    val joinedDate: String = "2024"
)

@Entity(tableName = "members")
data class Member(
    @PrimaryKey val id: String,
    val name: String,
    val city: String,
    val distance: String,
    val isOnline: Boolean,
    val phoneNumber: String,
    val joinedDate: String,
    val ecoDurationYears: Int,
    val totalBatch: Int
)

@Entity(tableName = "eco_batches")
data class EcoBatch(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val fruitSkinKg: Double,
    val molaseKg: Double,
    val waterLiters: Double,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "chat_messages")
data class ChatMessage(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val senderName: String,
    val receiverName: String,
    val message: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isFromUser: Boolean
)
