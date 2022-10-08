package com.example.noteskeeping.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.noteskeeping.R
import com.example.noteskeeping.model.Notes

class NoteRecyclerViewAdapter(private var noteList : ArrayList<Notes>) : RecyclerView.Adapter<NoteRecyclerViewAdapter.NoteViewHolder>() {

    var allNotes = mutableListOf<Notes>().apply {
        addAll(noteList)
        notifyDataSetChanged()
    }

    inner class NoteViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

        var noteTitle : TextView = itemView.findViewById(R.id.note_title)
        var noteContent : TextView = itemView.findViewById(R.id.note_content)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val layerInflater = LayoutInflater.from(parent.context).inflate(R.layout.notes_cardlayout,parent,true)
        return NoteViewHolder(layerInflater)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val notes : Notes = allNotes[position]
        holder.noteContent.text = notes.notes.toString()
        holder.noteTitle.text = notes.title.toString()
    }

    override fun getItemCount(): Int {
        return allNotes.size
    }
}