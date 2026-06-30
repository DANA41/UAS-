package com.example.repository

import com.example.data.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class EcoRepository(private val ecoDao: EcoDao) {

    val userProfile: Flow<UserProfile?> = ecoDao.getUserProfile()
    val members: Flow<List<Member>> = ecoDao.getMembers()
    val batches: Flow<List<EcoBatch>> = ecoDao.getEcoBatches()
    val chatMessages: Flow<List<ChatMessage>> = ecoDao.getAllChatMessages()

    suspend fun saveUserProfile(profile: UserProfile) {
        ecoDao.insertUserProfile(profile)
    }

    suspend fun insertBatch(batch: EcoBatch) {
        ecoDao.insertEcoBatch(batch)
    }

    suspend fun deleteBatch(batch: EcoBatch) {
        ecoDao.deleteEcoBatch(batch)
    }

    suspend fun insertChatMessage(message: ChatMessage) {
        ecoDao.insertChatMessage(message)
    }

    suspend fun prepopulateMembersIfNeeded() {
        val currentMembers = ecoDao.getMembers().firstOrNull()
        if (currentMembers.isNullOrEmpty()) {
            val initialMembers = listOf(
                Member(
                    id = "made",
                    name = "Made",
                    city = "Denpasar",
                    distance = "1.2 km",
                    isOnline = true,
                    phoneNumber = "0812 3456 7890",
                    joinedDate = "2020",
                    ecoDurationYears = 4,
                    totalBatch = 320
                ),
                Member(
                    id = "ayu",
                    name = "Ayu",
                    city = "Badung",
                    distance = "2 km",
                    isOnline = true,
                    phoneNumber = "0812 9876 5432",
                    joinedDate = "2021",
                    ecoDurationYears = 3,
                    totalBatch = 150
                ),
                Member(
                    id = "budi",
                    name = "Budi",
                    city = "Gianyar",
                    distance = "5 km",
                    isOnline = true,
                    phoneNumber = "0813 4567 8901",
                    joinedDate = "2019",
                    ecoDurationYears = 5,
                    totalBatch = 420
                ),
                Member(
                    id = "komang",
                    name = "Komang",
                    city = "Tabanan",
                    distance = "7 km",
                    isOnline = true,
                    phoneNumber = "0819 8765 4321",
                    joinedDate = "2022",
                    ecoDurationYears = 2,
                    totalBatch = 80
                ),
                Member(
                    id = "lina",
                    name = "Lina",
                    city = "Bangli",
                    distance = "10 km",
                    isOnline = true,
                    phoneNumber = "0821 3456 7890",
                    joinedDate = "2023",
                    ecoDurationYears = 1,
                    totalBatch = 45
                )
            )
            ecoDao.insertMembers(initialMembers)
        }
    }
}
