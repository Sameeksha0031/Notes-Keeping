package com.example.noteskeeping.view

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.constraintlayout.widget.ConstraintLayoutStates.TAG
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.noteskeeping.R
import com.example.noteskeeping.model.Notes
import com.google.android.gms.common.SupportErrorDialogFragment.newInstance
import java.lang.reflect.Array.newInstance
import javax.xml.parsers.DocumentBuilderFactory.newInstance

class NoteRecyclerViewAdapter(private var noteList: ArrayList<Notes>) :
    RecyclerView.Adapter<NoteRecyclerViewAdapter.NoteViewHolder>(){

    //private var archiveNoteList : ArrayList<Notes> = arrayListOf()
    var allNotes = mutableListOf<Notes>().apply {
        addAll(noteList)
        notifyDataSetChanged()
    }
    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var noteTitle: TextView
        var noteContent: TextView
        var menu: ImageView


        init {
            noteTitle = itemView.findViewById(R.id.note_title)
            noteContent = itemView.findViewById(R.id.note_content)
            menu = itemView.findViewById(R.id.edit_delete_menu)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val layerInflater =
            LayoutInflater.from(parent.context).inflate(R.layout.notes_cardlayout, parent, false)
        return NoteViewHolder(layerInflater)
    }

    override fun getItemCount(): Int {
        return allNotes.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int){
        val notes: Notes = allNotes[position]
        holder.noteContent.text = notes.notes.toString()
        holder.noteTitle.text = notes.title.toString()
        val bundle = Bundle()

        holder.menu.setOnClickListener{
            val popup = PopupMenu(it.context, holder.menu)
            popup.inflate(R.menu.notes_menu)
            //           notesViewModel = ViewModelProvider(this, NotesViewModelFactory(NoteServices())).get(NotesViewModel::class.java)
            popup.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener{
                override fun onMenuItemClick(item: MenuItem): Boolean {
                    when (item.getItemId()) {
                        R.id.edit_option -> {
                            val fragment = CreatingNotesFragment()
                            var noteId  : String = notes.noteId
                            bundle.putString("noteId",noteId)
                            bundle.putInt("edit_note",0)
                            fragment.arguments = bundle
                            val transaction = it.context as AppCompatActivity
                            transaction.supportFragmentManager.beginTransaction()
                                .replace(R.id.home_activity_fragment_container, fragment)
                                .addToBackStack(null)
                                .commit()
                            Toast.makeText(it.context,"Edit Text",Toast.LENGTH_SHORT).show()
                            return true
                        }
                        R.id.delete_option -> {
                            val fragment = NoteFragment()
                            var noteId  : String = notes.noteId
                            bundle.putString("noteId",noteId)
                            bundle.putInt("perform_deletion",1)
                            fragment.arguments = bundle
                            val transaction = it.context as AppCompatActivity
                            transaction.supportFragmentManager.beginTransaction()
                                .replace(R.id.home_activity_fragment_container, fragment)
                                .addToBackStack(null)
                                .commit()

                            Toast.makeText(it.context,"Delete the note ",Toast.LENGTH_SHORT).show()
                            return true
                        }
                        R.id.archeive -> {
                            val fragment = NoteFragment()
                            var noteId  : String = notes.noteId
                            bundle.putString("noteId",noteId)
                            bundle.putInt("add_note_to_archive",2)
                            fragment.arguments = bundle
                            val transaction = it.context as AppCompatActivity
                            transaction.supportFragmentManager.beginTransaction()
                                .replace(R.id.home_activity_fragment_container, fragment)
                                .addToBackStack(null)
                                .commit()
                            //Log.d("Adapter","allNotes - $allNotes")
                            //Log.d("Adapter","allNotes - $archiveNoteList")
//                           Toast.makeText(it.context,"Note add to Archive",Toast.LENGTH_SHORT).show()
                            return true
                        }
                        R.id.reminder ->{
                            val transaction = it.context as AppCompatActivity
                            transaction.supportFragmentManager.beginTransaction()
                                .replace(R.id.home_activity_fragment_container, ReminderFragment())
                                .addToBackStack(null)
                                .commit()
                            return true
                        }
                        else -> return false
                    }
                }
            })
            popup.show()
        }

    }

}