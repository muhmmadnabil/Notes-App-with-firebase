package com.muhmmadnabil.notes

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.Toast
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.add_note.view.*
import kotlinx.android.synthetic.main.update_note.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private val database= FirebaseDatabase.getInstance()
    private val mRef=database.getReference("Notes")
    var mNoteList:ArrayList<Note>?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mNoteList= ArrayList()
        fab_add.setOnClickListener {
            ShowDialogAddNote()
        }
        note_list.onItemClickListener=
            AdapterView.OnItemClickListener { p0, p1, position, p3 ->
                val myNotes= mNoteList!![position]

                val intent= Intent(this,NoteActivity::class.java)
                intent.putExtra("title_key",myNotes.title)
                intent.putExtra("note_key",myNotes.note)
                startActivity(intent)
            }
        note_list.onItemLongClickListener=
            AdapterView.OnItemLongClickListener { p0, p1, position, p3 ->
                val alertBuilder=AlertDialog.Builder(this)
                val view=layoutInflater.inflate(R.layout.update_note,null)
                val alertDialog=alertBuilder.create()
                alertDialog.setView(view)
                alertDialog.show()

                val myNote= mNoteList!![position]
                view.ed_update_title.setText(myNote.title)
                view.ed_update_note.setText(myNote.note)

                view.btn_update.setOnClickListener {

                    val afterUpdate=Note(myNote.id
                        ,view.ed_update_title.text.toString()
                        ,view.ed_update_note.text.toString()
                        ,getCurrentDate())

                    mRef.child(myNote.id.toString()).setValue(afterUpdate)
                    alertDialog.dismiss()

                }

                view.btn_delete.setOnClickListener {
                    mRef.child(myNote.id!!).removeValue()
                    alertDialog.dismiss()
                }

                false
            }
    }

    override fun onStart() {
        super.onStart()
        mRef.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                mNoteList?.clear()
                for(s in snapshot.children){
                    val note=s.getValue(Note::class.java)
                    mNoteList?.add(0,note!!)
                }
                val noteadapter=NoteAdapter(applicationContext,mNoteList!!)
                note_list.adapter=noteadapter
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun ShowDialogAddNote(){
        val alertBuilder=AlertDialog.Builder(this)
        val view=layoutInflater.inflate(R.layout.add_note,null)
        alertBuilder.setView(view)
        val alertDialog=alertBuilder.create()
            alertDialog.show()

        view.btn_add.setOnClickListener {
            val title=view.ed_add_title.text.toString()
            val note=view.ed_add_note.text.toString()

            if(title.isNotEmpty() && note.isNotEmpty()){
                val id=mRef.push().key
                val myNote=Note(id,title,note,getCurrentDate())
                mRef.child(id!!).setValue(myNote)
                alertDialog.dismiss()
            }else{
                Toast.makeText(this,"Empty",Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun getCurrentDate():String{
        val calendar= Calendar.getInstance()
        val mdformat= SimpleDateFormat("EEE hh:mm a")
        val strDate=mdformat.format(calendar.time)
        return strDate
    }
}