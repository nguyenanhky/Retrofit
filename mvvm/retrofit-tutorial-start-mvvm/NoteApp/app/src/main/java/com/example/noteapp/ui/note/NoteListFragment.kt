package com.example.noteapp.ui.note

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noteapp.R
import com.example.noteapp.data.Note
import com.example.noteapp.ui.adapter.NoteAdapter
import com.example.noteapp.ui.viewmodel.NoteViewModel
import com.example.noteapp.ultis.Status
import kotlinx.android.synthetic.main.fragment_note_list.*

class NoteListFragment : Fragment(R.layout.fragment_note_list) {
    private val noteViewModel: NoteViewModel by lazy {
        ViewModelProvider(
            this,
            NoteViewModel.NoteViewModelFactory(requireActivity().application)
        )[NoteViewModel::class.java]
    }
    private val noteAdapter:NoteAdapter by lazy {
        NoteAdapter(requireContext(),onItemClick,onItemDelete)
    }
    private val controller by lazy {
        findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_note.setHasFixedSize(true)
        rv_note.layoutManager = LinearLayoutManager(requireContext())
        rv_note.adapter = noteAdapter

        btn_open_add_activity.setOnClickListener {
          controller.navigate(R.id.action_noteListFragment_to_addNoteFragment)
        }

        swipe_layout.setOnRefreshListener {
            refreshData()
        }
        refreshData()
    }

    private fun refreshData() {
        noteViewModel.getNotesFromApi().observe(viewLifecycleOwner) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        swipe_layout.isRefreshing = false
                        resource.data?.let{
                            notes ->
                            noteAdapter.setNotes(notes)
                        }
                    }

                    Status.LOADING -> {
                        swipe_layout.isRefreshing = true
                    }

                    Status.ERROR -> {
                        swipe_layout.isRefreshing = false
                        Toast.makeText(requireContext(),it.message,Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private val onItemClick:(Note)->Unit={

    }
    private val onItemDelete:(Note)->Unit = {

    }
}