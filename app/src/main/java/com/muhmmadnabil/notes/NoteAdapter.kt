package com.muhmmadnabil.notes

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.note_layout.view.*

class NoteAdapter(context: Context,noteList:ArrayList<Note>)
                :ArrayAdapter<Note>(context,0,noteList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view=LayoutInflater.from(context).inflate(R.layout.note_layout,parent,false)
        val note: Note= getItem(position)!!
        view.tv_title.text= note.title
        view.tv_date.text=note.timestamp.toString()
        return view
    }
}