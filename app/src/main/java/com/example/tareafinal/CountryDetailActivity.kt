package com.example.tareafinal

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso

class CountryDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPrefs = getSharedPreferences("MyAppSettings", MODE_PRIVATE)
        if (sharedPrefs.getBoolean("DarkThemeEnabled", false)) {
            setTheme(R.style.AppTheme_Dark)
        } else {
            setTheme(R.style.AppTheme)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_country_detail)

        val countryName = intent.getStringExtra("COUNTRY_NAME")
        val countryCapital = intent.getStringExtra("COUNTRY_CAPITAL")
        val countryRegion = intent.getStringExtra("COUNTRY_REGION")
        val countryFlag = intent.getStringExtra("COUNTRY_FLAG")

        val textViewName: TextView = findViewById(R.id.textViewDetailName)
        val textViewCapital: TextView = findViewById(R.id.textViewDetailCapital)
        val textViewRegion: TextView = findViewById(R.id.textViewDetailRegion)
        val imageViewFlag: ImageView = findViewById(R.id.imageViewFlagDetail)

        textViewName.text = countryName
        textViewCapital.text = countryCapital
        textViewRegion.text = countryRegion
        Picasso.get().load(countryFlag).into(imageViewFlag)

        val btnVolver: Button = findViewById(R.id.btnVolver)
        btnVolver.setOnClickListener {
            finish() // Finaliza la actividad actual
        }
    }
}

