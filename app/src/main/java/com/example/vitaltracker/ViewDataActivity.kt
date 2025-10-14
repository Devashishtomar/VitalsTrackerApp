package com.example.vitaltracker

import com.example.vitaltracker.VitalsRepository
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.util.Log

class ViewDataActivity : AppCompatActivity() {
    private lateinit var repository: VitalsRepository

    companion object {
        private const val TAG = "ViewDataActivity"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_data)

        repository = VitalsRepository(this)

        val datePickerSpinner = findViewById<Spinner>(R.id.datePickerSpinner)
        val btnLoad = findViewById<Button>(R.id.btnLoad)
        val tvData = findViewById<TextView>(R.id.tvVitals)


        CoroutineScope(Dispatchers.IO).launch {
            val availableDates = repository.getAvailableDates()

            withContext(Dispatchers.Main) {
                val adapter = ArrayAdapter(this@ViewDataActivity, android.R.layout.simple_spinner_item, availableDates)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                datePickerSpinner.adapter = adapter
            }
        }

        btnLoad.setOnClickListener {
            val selectedDate = datePickerSpinner.selectedItem?.toString() ?: ""

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val vitals = repository.getVitalsByDate(selectedDate)
                    withContext(Dispatchers.Main) {
                        if (vitals != null) {
                            val bpValues = vitals.bp.split("/")
                            val systolic = bpValues[0]
                            val diastolic = bpValues[1]
                            tvData.text = getString(R.string.vitals_data, vitals.date,systolic,
                                diastolic, vitals.sugar, vitals.oxygen, vitals.temperature,
                                vitals.pulse, vitals.weight)
                        } else {
                            tvData.text = getString(R.string.no_data)
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@ViewDataActivity, "Error retrieving data", Toast.LENGTH_SHORT).show()
                    }
                    Log.e(TAG, "Error loading data for date: $selectedDate", e)
                }
            }
        }
    }
}