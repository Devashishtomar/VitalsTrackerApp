package com.example.vitaltracker

import android.content.Context



class VitalsRepository(context: Context) {
    private val vitalsDao: VitalsDao = VitalsDatabase.getDatabase(context).vitalsDao()

    suspend fun saveVitals(vitals: Vitals) {
        val existingVitals = vitalsDao.getVitalsByDate(vitals.date)
        if (existingVitals == null) {
            vitalsDao.insert(vitals)
        } else {
            val updatedVitals = existingVitals.copy(
                bp = vitals.bp ,
                sugar = vitals.sugar ,
                oxygen = vitals.oxygen ,
                temperature = vitals.temperature ,
                pulse = vitals.pulse ,
                weight = vitals.weight )
            vitalsDao.update(updatedVitals)
        }
    }

    suspend fun getVitalsByDate(date: String): Vitals? {
        return vitalsDao.getVitalsByDate(date)
    }
    suspend fun getAvailableDates(): List<String> {
        return vitalsDao.getAvailableDates()
    }

    suspend fun insertVitals(vitals: Vitals) {
        vitalsDao.insert(vitals)
    }
}