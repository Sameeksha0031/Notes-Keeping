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

    inner class NoteViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

        var noteContent : TextView = itemView.findViewById(R.id.note_content)

//         init{
//             noteList = itemView.findViewById(R.id.note_content)
//
//             itemView.setOnClickListener{
//                 val position : Int = adapterPosition
//                 Toast.makeText(it.context,"you clicked on ${contentInNote[position]}",Toast.LENGTH_SHORT).show()
//             }
//         }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val layerInflater = LayoutInflater.from(parent.context).inflate(R.layout.notes_cardlayout,parent,false)
        return NoteViewHolder(layerInflater)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val notes : Notes = noteList[position]
        holder.noteContent.text = notes.notes
    }

    override fun getItemCount(): Int {
        return noteList.size
    }
}