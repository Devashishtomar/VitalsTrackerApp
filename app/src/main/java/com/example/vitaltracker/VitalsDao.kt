package com.example.vitaltracker

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface VitalsDao {
    @Query("SELECT * FROM Vitals WHERE id = :id LIMIT 1")
    suspend fun getVitalsById(id: Long): Vitals?

    @Query("SELECT * FROM Vitals WHERE date = :date LIMIT 1")
    suspend fun getVitalsByDate(date: String): Vitals?

    @Insert
    suspend fun insert(vitals: Vitals): Long

    @Update
    suspend fun update(vitals: Vitals)

    @Query("SELECT DISTINCT date FROM vitals")
    suspend fun getAvailableDates(): List<String>
}