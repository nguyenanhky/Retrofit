package com.example.noteapp.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {
    private const val BASE_URL = "http://192.168.0.104:8080";
    private val builder = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create());

    val retrofit = builder.build()
    val apiService:NoteApi = retrofit.create(NoteApi::class.java)
}