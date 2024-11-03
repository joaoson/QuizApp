package com.example.quizapp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LeaderboardDao {
    @Insert
    suspend fun insertEntry(entry: LeaderboardEntry)

    @Query("SELECT * FROM leaderboard ORDER BY score DESC, timeTaken ASC")
    suspend fun getAllEntries(): List<LeaderboardEntry>

    @Query("SELECT * FROM leaderboard WHERE category = :category ORDER BY score DESC, timeTaken ASC LIMIT 10")
    suspend fun getTop10EntriesByCategory(category: String): List<LeaderboardEntry>
}
