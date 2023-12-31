package com.example.noteapp.data

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.noteapp.api.ApiConfig
import com.example.noteapp.database.NoteDatabase
import com.example.noteapp.database.dao.NoteDao
import com.example.noteapp.data.Note

class NoteRepository(app: Application) {

    private val noteDao: NoteDao

    init {
        val noteDatabase: NoteDatabase = NoteDatabase.getInstance(app)
        noteDao = noteDatabase.getNoteDao()
    }

    suspend fun insertNote(note:Note) = noteDao.insertNote(note)
    suspend fun updateNote(note:Note) = noteDao.updateNote(note)
    suspend fun deleteNote(note:Note) = noteDao.deleteNote(note)

    fun getAllNote():LiveData<List<Note>> = noteDao.getAllNote()

    suspend fun getNoteFromApi() = ApiConfig.apiService.getALlNote()
    suspend fun addNoteToServer(note: Note) = ApiConfig.apiService.addNote(note)



}