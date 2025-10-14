package com.example.vitaltracker

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity ("Vitals")
data class Vitals(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val date: String,
    val bp: String,
    val sugar: String,
    val oxygen: String,
    val temperature: String,
    val pulse: String,
    val weight: String
)
