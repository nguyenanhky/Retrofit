package com.example.noteapp.api


import com.example.noteapp.data.Note
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface NoteApi {
    @GET("/note")
    suspend fun getALlNote():List<Note>

    @POST("/note")
    suspend fun addNote(@Body note:Note):Note

    @DELETE("/note/{id}")
    suspend fun deleteNote(@Path("id")id:Int): Note

    @PUT("/note/{id}")
    suspend fun updateNote(@Path("id")id:Int,@Body note:Note)
}