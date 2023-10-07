package com.example.noteapp.server

import com.example.noteapp.model.Image
import com.example.noteapp.model.Note
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface NoteClient {
    @GET("/note")
    fun getAllNote():Call<List<Note>>

    @Multipart
    @POST("/upload")
    fun uploadImage(@Part image:MultipartBody.Part):Call<Image>

    @POST("/note")
    fun addNote(@Body note: Note):Call<Note>

    @PUT("/note/{id}")
    fun updateNote(@Path("id")id:Int, @Body note:Note):Call<Note>

    @DELETE("/note/{id}")
    fun deleteNote(@Path("id")id:Int):Call<Note>
}