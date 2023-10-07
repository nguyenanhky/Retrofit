package com.example.noteapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.noteapp.AppConfig
import com.example.noteapp.R
import com.example.noteapp.model.Note
import com.example.noteapp.server.NoteClient
import com.example.noteapp.ultis.Logger
import com.example.noteapp.viewmodel.NoteViewModel
import kotlinx.android.synthetic.main.activity_update_note.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateNoteActivity : AppCompatActivity() {
    private val noteViewModel: NoteViewModel by lazy {
        ViewModelProvider(
            this,
            NoteViewModel.NoteViewModelFactory(this.application)
        )[NoteViewModel::class.java]
    }

    private val client: NoteClient = AppConfig.retrofit.create(NoteClient::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_note)

        val note=intent.getSerializableExtra("UPDATE_NOTE") as Note
        edt_note_title.setText(note.title)
        edt_note_des.setText(note.description)

        Glide.with(this).load(note.imgPath).into(img_note_update)

        btn_update.setOnClickListener {
            note.title=edt_note_title.text.toString()
            note.description=edt_note_des.text.toString()
            val call:Call<Note> = client.updateNote(note.id,note)

            call.enqueue(object: Callback<Note>{
                override fun onResponse(call: Call<Note>, response: Response<Note>) {
                   Logger.lod("onResponse update")
                    finish()
                }

                override fun onFailure(call: Call<Note>, t: Throwable) {
                    Logger.lod("onFailure update")
                }

            })
            noteViewModel.updateNote(note)
            finish()
        }

    }
}