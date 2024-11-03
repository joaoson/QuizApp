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
}
