package com.example.noteapp.activities

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.noteapp.AppConfig
import com.example.noteapp.R
import com.example.noteapp.model.Image
import com.example.noteapp.model.Note
import com.example.noteapp.server.NoteClient
import com.example.noteapp.ultis.Logger
import com.example.noteapp.ultis.Utils
import com.example.noteapp.viewmodel.NoteViewModel
import kotlinx.android.synthetic.main.activity_add_note.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class AddNoteActivity : AppCompatActivity() {
    private val noteViewModel: NoteViewModel by lazy {
        ViewModelProvider(
            this,
            NoteViewModel.NoteViewModelFactory(this.application)
        )[NoteViewModel::class.java]
    }
    private var imagePath: String? = null

    companion object {
        const val PICK_IMAGE_REQUEST = 123
        const val REQUEST_PERMISSION_CODE = 234
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        btn_add.setOnClickListener {
//            val note = Note(edt_note_title.text.toString(), edt_note_des.text.toString())
//            noteViewModel.insertNote(note)
//            finish()
            addNote()
        }
        img_note_add.setOnClickListener {
            requestPermissionAndPickImage()
        }

    }

    private fun addNote() {
        if (imagePath != null) {
            val note = Note(edt_note_title.text.toString(), edt_note_des.text.toString(), imagePath!!)
            val call:Call<Note> = client.addNote(note)

            call.enqueue(object:Callback<Note>{
                override fun onResponse(call: Call<Note>, response: Response<Note>) {
                    if(response.isSuccessful){
                        Log.d("kynv1", "onResponse: ${response.body().toString()}")
                        finish()
                    }
                }

                override fun onFailure(call: Call<Note>, t: Throwable) {

                }

            })
        } else {
            Toast.makeText(this@AddNoteActivity, "Image has not been uploaded", Toast.LENGTH_SHORT).show()
        }
    }

    private fun requestPermissionAndPickImage() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            pickImage()
            return
        }
        val result = ContextCompat.checkSelfPermission(
            this,
            READ_EXTERNAL_STORAGE
        )
        if (result == PackageManager.PERMISSION_GRANTED) {
            pickImage()
        } else {
            requestPermissions(
                arrayOf(
                    READ_EXTERNAL_STORAGE
                ),
                REQUEST_PERMISSION_CODE
            )
        }
    }

    private fun pickImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(intent, "select image to upload"),
            PICK_IMAGE_REQUEST
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            img_note_add.setImageURI(data.data)
            uploadImage(data.data!!)
        }
    }

    private val client: NoteClient = AppConfig.retrofit.create(NoteClient::class.java)

    private fun uploadImage(uri: Uri) {
        val file = File(Utils.getPathFromUri(this, uri))
        val requestBody = RequestBody.create(MediaType.parse(contentResolver.getType(uri)!!), file)
        val imagePart = MultipartBody.Part.createFormData("picture", file.name, requestBody)
        val call: Call<Image> = client.uploadImage(imagePart)

        call.enqueue(object : Callback<Image> {
            override fun onResponse(call: Call<Image>, response: Response<Image>) {
                Logger.lod("${response.body()}")
                if (response.isSuccessful) {
                    response.body()?.let {
                        imagePath = it.imagePath
                       Logger.lod("onResponse")
                    }
                }
            }

            override fun onFailure(call: Call<Image>, t: Throwable) {
                Log.d("kynv1", "onFailure: ")
            }

        })
    }
}
