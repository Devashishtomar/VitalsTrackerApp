package com.example.vitaltracker


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.ActionBarDrawerToggle
import java.text.SimpleDateFormat
import java.util.*
import android.widget.ImageButton
import androidx.core.view.GravityCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.widget.Toast
import android.widget.TextView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var repository: VitalsRepository
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var btnMenu: ImageButton
    private lateinit var tvCurrentDate: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        repository = VitalsRepository(this)

        tvCurrentDate = findViewById(R.id.tvCurrentDate)

        drawerLayout = findViewById(R.id.drawer_layout)
        val navigationView: NavigationView = findViewById(R.id.navigation_view)
        navigationView.setNavigationItemSelectedListener(this)

        btnMenu = findViewById(R.id.btnMenu)
        btnMenu.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }




        val etHBp: EditText = findViewById(R.id.etHBp)
        val etLBp: EditText = findViewById(R.id.etLBp)
        val etSugar: EditText = findViewById(R.id.etSugar)
        val etOxygen: EditText = findViewById(R.id.etOxygen)
        val etTemperature: EditText = findViewById(R.id.etTemperature)
        val etPulse: EditText = findViewById(R.id.etPulse)
        val etWeight: EditText = findViewById(R.id.etWeight)
        val btnSave: Button = findViewById(R.id.btnSave)


        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(calendar.time)
        tvCurrentDate.text = formattedDate

        btnSave.setOnClickListener {
            val hbp = etHBp.text.toString().takeIf { it.isNotEmpty() } ?:""
            val lbp = etLBp.text.toString().takeIf { it.isNotEmpty() } ?:""
            val sugar = etSugar.text.toString().takeIf { it.isNotEmpty() } ?:""
            val oxygen = etOxygen.text.toString().takeIf { it.isNotEmpty() } ?:""
            val temperature = etTemperature.text.toString().takeIf { it.isNotEmpty() } ?:""
            val pulse = etPulse.text.toString().takeIf { it.isNotEmpty() } ?:""
            val weight = etWeight.text.toString().takeIf { it.isNotEmpty() } ?:""

            val selectedDate = tvCurrentDate.text.toString()

            val vitals = Vitals(date = selectedDate, bp = "$hbp/$lbp", sugar = sugar, oxygen = oxygen, temperature = temperature, weight = weight, pulse = pulse)

            CoroutineScope(Dispatchers.IO).launch {
                repository.saveVitals(vitals)

                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Data saved successfully", Toast.LENGTH_SHORT).show()
                }
            }

            val intent = Intent(this, ViewDataActivity::class.java)
            startActivity(intent)
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        return if (toggle.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_view_data -> {
                val intent = Intent(this, ViewDataActivity::class.java)
                startActivity(intent)
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
