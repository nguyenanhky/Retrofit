package com.example.noteapp.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noteapp.AppConfig
import com.example.noteapp.R
import com.example.noteapp.adapter.NoteAdapter
import com.example.noteapp.model.Note
import com.example.noteapp.server.NoteClient
import com.example.noteapp.ultis.Logger
import com.example.noteapp.viewmodel.NoteViewModel
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    private val noteViewModel: NoteViewModel by lazy {
        ViewModelProvider(
            this,
            NoteViewModel.NoteViewModelFactory(this.application)
        )[NoteViewModel::class.java]
    }
    private val adapter: NoteAdapter by lazy {
        NoteAdapter(this@MainActivity, onItemClick, onItemDelete)
    }


    private val client:NoteClient = AppConfig.retrofit.create(NoteClient::class.java)
    private var service: Call<List<Note>> = client.getAllNote()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initControls()
        initEvents()
    }

    private fun initEvents() {
        btn_open_add_activity.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)
            startActivity(intent)
        }
        swipe_layout.setOnRefreshListener {
            fetchData()
        }
    }

    private fun initControls() {
        rv_note.setHasFixedSize(true)
        rv_note.layoutManager = LinearLayoutManager(this)
        rv_note.adapter = adapter

        /*noteViewModel.getAllNote().observe(this, Observer {
            adapter.setNotes(it)
        })*/
        fetchData()

    }

    private fun fetchData() {
        service  = client.getAllNote()
        service.enqueue(object : Callback<List<Note>>{
            override fun onResponse(call: Call<List<Note>>, response: Response<List<Note>>) {
                Toast.makeText(this@MainActivity, response.code().toString(),Toast.LENGTH_SHORT).show()
                if(response.isSuccessful){
                    val list = response.body()
                    adapter.setNotes(list!!)
                    swipe_layout.isRefreshing = false
                }
            }

            override fun onFailure(call: Call<List<Note>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Fail",Toast.LENGTH_SHORT).show()
                swipe_layout.isRefreshing = false
            }

        })
    }

    private val onItemClick: (Note) -> Unit = {
        val intent = Intent(this, UpdateNoteActivity::class.java)
        intent.putExtra("UPDATE_NOTE", it)
        startActivity(intent)

    }
    private val onItemDelete: (Note) -> Unit = {
        //noteViewModel.deleteNote(it)
        val service = client.deleteNote(it.id)
        service.enqueue(object:Callback<Note>{
            override fun onResponse(call: Call<Note>, response: Response<Note>) {
                if(response.isSuccessful){
                    Logger.lod("delete response")
                    fetchData()
                }
            }

            override fun onFailure(call: Call<Note>, t: Throwable) {
                Logger.lod("onFailure")
            }

        })
    }
}