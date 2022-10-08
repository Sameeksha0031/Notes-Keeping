package com.example.noteskeeping.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.noteskeeping.R
import com.example.noteskeeping.model.Notes

class NoteRecyclerViewAdapter(private var noteList : ArrayList<Notes>) : RecyclerView.Adapter<NoteRecyclerViewAdapter.NoteViewHolder>() {

    var allNotes = mutableListOf<Notes>().apply {
        addAll(noteList)
        notifyDataSetChanged()
    }

    inner class NoteViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

        val title :TextView = itemView.findViewById(R.id.note_title)
        val noteContent : TextView = itemView.findViewById(R.id.note_content)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val layerInflater = LayoutInflater.from(parent.context).inflate(R.layout.notes_cardlayout,parent,false)
        return NoteViewHolder(layerInflater)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.noteContent.text = allNotes[position].notes
        holder.title.text = allNotes[position].title
    }

    override fun getItemCount(): Int {
        return allNotes.size
    }
}