package com.runanywhere.startup_hackathon20.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "journal_entries")
data class JournalEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val text: String,
    val emotion: String,
    val timestamp: Long = System.currentTimeMillis()
)
