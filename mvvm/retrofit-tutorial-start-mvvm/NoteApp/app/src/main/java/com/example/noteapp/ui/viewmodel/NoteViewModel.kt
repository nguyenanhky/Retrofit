package com.example.noteapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.noteapp.data.Note
import com.example.noteapp.data.NoteRepository
import com.example.noteapp.ultis.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : ViewModel() {

    private val noteRepository: NoteRepository = NoteRepository(application)

    fun insertNote(note:Note) = viewModelScope.launch {
        noteRepository.insertNote(note)
    }

    fun updateNote(note: Note) =viewModelScope.launch {
        noteRepository.updateNote(note)
    }

    fun deleteNote(note: Note) =viewModelScope.launch {
        noteRepository.deleteNote(note)
    }

    fun getAllNote():LiveData<List<Note>> = noteRepository.getAllNote()

    fun getNotesFromApi() = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(noteRepository.getNoteFromApi()))
        }catch (ex:Exception){
            emit(Resource.error(null,ex.message?:"Error!!!"))
        }
    }

    fun addNoteToServer(note:Note) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(noteRepository.addNoteToServer(note)))
        }catch (ex:Exception){
            emit(Resource.error(null,ex.message?:"Error!!!"))
        }
    }
    class NoteViewModelFactory(private val application: Application) : ViewModelProvider.Factory{

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(NoteViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return  NoteViewModel(application) as T
            }
            
            throw IllegalArgumentException("Unable construct viewModel")
        }

    }



}