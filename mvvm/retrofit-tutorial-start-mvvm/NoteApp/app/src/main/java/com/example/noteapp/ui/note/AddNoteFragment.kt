package com.example.noteapp.ui.note

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.noteapp.R
import com.example.noteapp.data.Note
import com.example.noteapp.ui.viewmodel.NoteViewModel
import kotlinx.android.synthetic.main.fragment_add_note.*

class AddNoteFragment : Fragment(R.layout.fragment_add_note){
    private val noteViewModel: NoteViewModel by lazy {
        ViewModelProvider(
            this,
            NoteViewModel.NoteViewModelFactory(requireActivity().application)
        )[NoteViewModel::class.java]
    }

    private val controller by lazy {
        findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_add.setOnClickListener {
            val note = Note(edt_note_title.text.toString(),edt_note_des.text.toString())
        }
    }
}