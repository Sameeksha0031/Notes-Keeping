package com.example.noteskeeping.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.example.noteskeeping.R
import com.example.noteskeeping.model.Notes

class NoteRecyclerViewAdapter() : RecyclerView.Adapter<NoteRecyclerViewAdapter.NoteViewHolder>() {

    private val contentInNote = arrayOf("first","second","third","four","five","six","seven","eight","nine","ten")

    inner class NoteViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
         var noteContent : TextView

         init{
             noteContent = itemView.findViewById(R.id.note_content)

             itemView.setOnClickListener{
                 val position : Int = adapterPosition
                 Toast.makeText(it.context,"you clicked on ${contentInNote[position]}",Toast.LENGTH_SHORT).show()
             }
         }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val layerInflater = LayoutInflater.from(parent.context).inflate(R.layout.notes_cardlayout,parent,false)
        return NoteViewHolder(layerInflater)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.noteContent.text = contentInNote[position]
    }

    override fun getItemCount(): Int {
        return contentInNote.size
    }
}