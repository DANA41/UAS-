package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface EcoDao {
    // User Profile
    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    fun getUserProfile(): Flow<UserProfile?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserProfile(userProfile: UserProfile)

    // Members
    @Query("SELECT * FROM members")
    fun getMembers(): Flow<List<Member>>

    @Query("SELECT * FROM members WHERE id = :id LIMIT 1")
    fun getMemberById(id: String): Flow<Member?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMembers(members: List<Member>)

    // Eco Batches (Calculated Recipes)
    @Query("SELECT * FROM eco_batches ORDER BY timestamp DESC")
    fun getEcoBatches(): Flow<List<EcoBatch>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEcoBatch(batch: EcoBatch)

    @Delete
    suspend fun deleteEcoBatch(batch: EcoBatch)

    // Chat Messages
    @Query("SELECT * FROM chat_messages ORDER BY timestamp ASC")
    fun getAllChatMessages(): Flow<List<ChatMessage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChatMessage(message: ChatMessage)
}
