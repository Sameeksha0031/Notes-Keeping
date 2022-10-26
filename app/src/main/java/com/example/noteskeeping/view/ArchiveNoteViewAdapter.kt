package com.example.noteskeeping.view

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.noteskeeping.R
import com.example.noteskeeping.model.Notes

class ArchiveNoteViewAdapter(private var listOfArchiveNote : ArrayList<Notes>) :  RecyclerView.Adapter<ArchiveNoteViewAdapter.ArchiveNoteViewHolder>() {

    var allNotes = mutableListOf<Notes>().apply {
        addAll(listOfArchiveNote)
        notifyDataSetChanged()
    }

    inner class ArchiveNoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var noteTitle: TextView
        var noteContent: TextView

        init {
            noteTitle = itemView.findViewById(R.id.note_title)
            noteContent = itemView.findViewById(R.id.note_content)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArchiveNoteViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ArchiveNoteViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}