package com.example.tareafinal

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var adapter: CountryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        val sharedPrefs = getSharedPreferences("MyAppSettings", MODE_PRIVATE)
        if (sharedPrefs.getBoolean("DarkThemeEnabled", false)) {
            setTheme(R.style.AppTheme_Dark)
        } else {
            setTheme(R.style.AppTheme)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val btnToggleTheme: Button = findViewById(R.id.btnToggleTheme)
        var isDarkThemeEnabled = sharedPrefs.getBoolean("DarkThemeEnabled", false)

        recyclerView = findViewById(R.id.recyclerViewCountries)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = CountryAdapter(emptyList()) { country ->
            val intent = Intent(this, CountryDetailActivity::class.java).apply {
                putExtra("COUNTRY_NAME", country.name.common)
                // Agrega otros extras según sea necesario
            }
            startActivity(intent)
        }
        recyclerView.adapter = adapter

        searchView = findViewById(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchCountries(query)
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        loadInitialCountries()

        btnToggleTheme.setOnClickListener {
            isDarkThemeEnabled = !isDarkThemeEnabled

            val editor = sharedPrefs.edit()
            editor.putBoolean("DarkThemeEnabled", isDarkThemeEnabled)
            editor.apply()

            // Opcional: reiniciar la actividad para aplicar el cambio de tema
            recreate()
        }
    }

    private fun navigateToCountryDetail(country: Country) {
        val intent = Intent(this, CountryDetailActivity::class.java).apply {
            putExtra("COUNTRY_NAME", country.name.common)
            putExtra("COUNTRY_CAPITAL", country.capital?.joinToString() ?: "No Capital")
            putExtra("COUNTRY_REGION", country.region)
            putExtra("COUNTRY_FLAG", country.flags.png)
        }
        startActivity(intent)
    }

    private fun loadInitialCountries() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://restcountries.com/v3.1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(RestCountriesApi::class.java)

        api.getAllCountries().enqueue(object : Callback<List<Country>> {
            override fun onResponse(call: Call<List<Country>>, response: Response<List<Country>>) {
                if (!response.isSuccessful) {
                    Toast.makeText(this@MainActivity, "Error: ${response.code()}", Toast.LENGTH_LONG).show()
                    return
                }

                val countries = response.body()
                if (countries != null) {
                    adapter.updateCountries(countries)
                } else {
                    Toast.makeText(this@MainActivity, "No se encontraron resultados", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<Country>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun searchCountries(countryName: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://restcountries.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(RestCountriesApi::class.java)

        api.getCountryByName(countryName).enqueue(object : Callback<List<Country>> {
            override fun onResponse(call: Call<List<Country>>, response: Response<List<Country>>) {
                if (!response.isSuccessful) {
                    Toast.makeText(this@MainActivity, "Error: ${response.code()}", Toast.LENGTH_LONG).show()
                    return
                }

                val countries = response.body()
                if (countries != null) {
                    adapter = CountryAdapter(countries) { country ->
                        val intent = Intent(this@MainActivity, CountryDetailActivity::class.java)
                        intent.putExtra("COUNTRY_NAME", country.name.common) // Envía el nombre común del país
                        intent.putExtra("COUNTRY_CAPITAL", country.capital?.joinToString() ?: "No Capital") // Envía la capital
                        intent.putExtra("COUNTRY_REGION", country.region) // Envía la región
                        intent.putExtra("COUNTRY_FLAG", country.flags.png) // Envía la URL de la bandera
                        startActivity(intent)
                    }
                    recyclerView.adapter = adapter
                } else {
                    Toast.makeText(this@MainActivity, "No se encontraron resultados", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<Country>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

}





