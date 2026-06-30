package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.*
import com.example.repository.EcoRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AppViewModel(
    application: Application,
    private val repository: EcoRepository
) : AndroidViewModel(application) {

    val userProfile: StateFlow<UserProfile?> = repository.userProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val members: StateFlow<List<Member>> = repository.members
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val batches: StateFlow<List<EcoBatch>> = repository.batches
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val chatMessages: StateFlow<List<ChatMessage>> = repository.chatMessages
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // UI state for the calculator
    private val _calcMolase = MutableStateFlow(0.0)
    val calcMolase: StateFlow<Double> = _calcMolase

    private val _calcFruitSkin = MutableStateFlow(0.0)
    val calcFruitSkin: StateFlow<Double> = _calcFruitSkin

    private val _calcWater = MutableStateFlow(0.0)
    val calcWater: StateFlow<Double> = _calcWater

    init {
        viewModelScope.launch {
            repository.prepopulateMembersIfNeeded()
            
            // Insert initial chat messages to match mockup conversation between Made & User
            val allMsgs = repository.chatMessages.firstOrNull() ?: emptyList()
            if (allMsgs.isEmpty()) {
                repository.insertChatMessage(
                    ChatMessage(
                        senderName = "User",
                        receiverName = "Made",
                        message = "Halo Made, selamat pagi!",
                        timestamp = System.currentTimeMillis() - 600000,
                        isFromUser = true
                    )
                )
                repository.insertChatMessage(
                    ChatMessage(
                        senderName = "Made",
                        receiverName = "User",
                        message = "Halo Yuli, selamat pagi juga!",
                        timestamp = System.currentTimeMillis() - 500000,
                        isFromUser = false
                    )
                )
                repository.insertChatMessage(
                    ChatMessage(
                        senderName = "User",
                        receiverName = "Made",
                        message = "Saya ingin belajar lebih banyak tentang Eco Enzyme",
                        timestamp = System.currentTimeMillis() - 400000,
                        isFromUser = true
                    )
                )
                repository.insertChatMessage(
                    ChatMessage(
                        senderName = "Made",
                        receiverName = "User",
                        message = "Wah, bagus sekali! Nanti kita bisa berbagi pengalaman.",
                        timestamp = System.currentTimeMillis() - 300000,
                        isFromUser = false
                    )
                )
                repository.insertChatMessage(
                    ChatMessage(
                        senderName = "Made",
                        receiverName = "User",
                        message = "Kalau ada waktu, yuk ketemu hari Minggu di rumah saya.",
                        timestamp = System.currentTimeMillis() - 200000,
                        isFromUser = false
                    )
                )
                repository.insertChatMessage(
                    ChatMessage(
                        senderName = "User",
                        receiverName = "Made",
                        message = "Baik, terima kasih Made",
                        timestamp = System.currentTimeMillis() - 100000,
                        isFromUser = true
                    )
                )
            }
        }
    }

    fun login(name: String, phone: String, city: String) {
        viewModelScope.launch {
            val profile = UserProfile(
                fullName = name,
                phoneNumber = phone,
                city = city,
                batchesCount = 12,
                joinedDate = "2024"
            )
            repository.saveUserProfile(profile)
        }
    }

    fun logout() {
        viewModelScope.launch {
            // We can delete profile to simulate logout
            val db = AppDatabase.getDatabase(getApplication())
            db.clearAllTables()
            // Reset state or re-prepopulate
            repository.prepopulateMembersIfNeeded()
        }
    }

    fun calculateRecipe(type: Int, amount: Double) {
        // type: 0 = Fruit skin, 1 = Water, 2 = Molase
        when (type) {
            0 -> { // Based on Fruit Skin (1 : 3 : 10)
                _calcFruitSkin.value = amount
                _calcMolase.value = amount / 3.0
                _calcWater.value = (amount / 3.0) * 10.0
            }
            1 -> { // Based on Water
                _calcWater.value = amount
                _calcMolase.value = amount / 10.0
                _calcFruitSkin.value = (amount / 10.0) * 3.0
            }
            2 -> { // Based on Molase
                _calcMolase.value = amount
                _calcFruitSkin.value = amount * 3.0
                _calcWater.value = amount * 10.0
            }
        }
    }

    fun saveCalculation(name: String) {
        viewModelScope.launch {
            val batch = EcoBatch(
                name = name,
                fruitSkinKg = _calcFruitSkin.value,
                molaseKg = _calcMolase.value,
                waterLiters = _calcWater.value
            )
            repository.insertBatch(batch)
            
            // Also increment user profile batch count
            val currentProfile = repository.userProfile.firstOrNull()
            if (currentProfile != null) {
                repository.saveUserProfile(
                    currentProfile.copy(batchesCount = currentProfile.batchesCount + 1)
                )
            }
        }
    }

    fun deleteBatchHistory(batch: EcoBatch) {
        viewModelScope.launch {
            repository.deleteBatch(batch)
        }
    }

    fun sendMessage(receiverName: String, text: String) {
        if (text.trim().isEmpty()) return
        viewModelScope.launch {
            // Save user message
            val userMsg = ChatMessage(
                senderName = "User",
                receiverName = receiverName,
                message = text,
                isFromUser = true
            )
            repository.insertChatMessage(userMsg)

            // Simulate typing delay and reply
            delay(1000)
            
            val replyText = when {
                text.contains("hello", ignoreCase = true) || text.contains("halo", ignoreCase = true) -> {
                    "Halo! Ada yang bisa saya bantu tentang pembuatan Eco Enzyme hari ini?"
                }
                text.contains("cara", ignoreCase = true) || text.contains("bagaimana", ignoreCase = true) -> {
                    "Untuk membuat Eco Enzyme, perbandingannya 1:3:10 (molase:kulit buah:air). Diamkan selama 3 bulan di tempat teduh."
                }
                text.contains("kulit buah", ignoreCase = true) || text.contains("buah", ignoreCase = true) -> {
                    "Gunakan kulit buah yang segar dan tidak busuk. Hindari kulit buah yang keras seperti durian atau bergetah seperti nangka."
                }
                text.contains("lokasi", ignoreCase = true) || text.contains("alamat", ignoreCase = true) -> {
                    "Saya tinggal di kawasan Bali, kita bisa berkumpul di basecamp komunitas untuk sharing batch terbaru!"
                }
                text.contains("panen", ignoreCase = true) || text.contains("umur", ignoreCase = true) -> {
                    "Masa fermentasi standar adalah 3 bulan. Setelah itu cairan bisa disaring dan digunakan untuk berbagai kebutuhan rumah tangga."
                }
                else -> {
                    "Wah menarik sekali! Mari terus berbagi informasi mengenai Eco Enzyme untuk menjaga kelestarian lingkungan kita."
                }
            }

            val replyMsg = ChatMessage(
                senderName = receiverName,
                receiverName = "User",
                message = replyText,
                isFromUser = false
            )
            repository.insertChatMessage(replyMsg)
        }
    }

    class Factory(
        private val application: Application,
        private val repository: EcoRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AppViewModel::class.java)) {
                return AppViewModel(application, repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
