package com.muhmmadnabil.notes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_note.*

class NoteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        textview_title.text= intent.extras?.getString("title_key")
        textview_note.text=intent.extras?.getString("note_key")
    }
}