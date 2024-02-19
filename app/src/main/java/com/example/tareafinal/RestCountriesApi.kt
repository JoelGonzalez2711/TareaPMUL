package com.example.tareafinal

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RestCountriesApi {
    @GET("v3.1/name/{name}")
    fun getCountryByName(@Path("name") countryName: String): Call<List<Country>>

    @GET("all")
    fun getAllCountries(): Call<List<Country>>
}
