package com.example.noteskeeping.view

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.noteskeeping.R
import com.example.noteskeeping.model.NoteServices
import com.example.noteskeeping.model.Notes
import com.example.noteskeeping.viewModel.NotesViewModel
import com.example.noteskeeping.viewModel.NotesViewModelFactory

class NoteRecyclerViewAdapter(private var noteList: ArrayList<Notes>) :
    RecyclerView.Adapter<NoteRecyclerViewAdapter.NoteViewHolder>() {

    var allNotes = mutableListOf<Notes>().apply {
        addAll(noteList)
        notifyDataSetChanged()
    }

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var noteTitle: TextView
        var noteContent: TextView
        var menu : ImageView
        lateinit var noteToBeDeleted : String


        init{
            noteTitle = itemView.findViewById(R.id.note_title)
            noteContent = itemView.findViewById(R.id.note_content)
            menu = itemView.findViewById(R.id.edit_delete_menu)
            //var notesViewModel = NotesViewModel(NoteServices())
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
       var notesViewModel = NotesViewModel(NoteServices())

        holder.menu.setOnClickListener{
            val popup = PopupMenu(it.context, holder.menu)
            popup.inflate(R.menu.notes_menu)
            //notesViewModel = ViewModelProvider(, NotesViewModelFactory(NoteServices())).get(NotesViewModel::class.java)
            popup.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener{
                override fun onMenuItemClick(item: MenuItem): Boolean {
                    when (item.getItemId()) {
                        R.id.edit_option -> {
                            val inflater = LayoutInflater.from(it.context).inflate(R.layout.fragment_home,null)
                            var title = it.findViewById<EditText>(R.id.note_title)
                            var notes = it.findViewById<EditText>(R.id.note_content)

                            val fragment = HomeFragment()
                            val appCompatActivity = it.context as AppCompatActivity
                            appCompatActivity.supportFragmentManager.
                            beginTransaction()
                                .replace(R.id.home_activity_fragment_container, fragment)
                                .addToBackStack(null)
                                .commit()


                            Toast.makeText(it.context,"Edit Text",Toast.LENGTH_SHORT).show()
                            return true
                        }
                        R.id.delete_option -> {
                            var noteId  : String = notes.noteId
                            notesViewModel.deleteNote(noteId)
                            notesViewModel.deleteNote.observe(viewLifecycleOwner, Observer {
                                if (it.status) {
                                    recyclerView.adapter = NoteRecyclerViewAdapter(it.noteArrayList)
                                    //Log.d("NoteFragment","${it.noteArrayList.size.toString()}")
                                    Toast.makeText(context, it.msg, Toast.LENGTH_SHORT).show()

                                }
                            })
                            Toast.makeText(it.context,"Delete the note ",Toast.LENGTH_SHORT).show()
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