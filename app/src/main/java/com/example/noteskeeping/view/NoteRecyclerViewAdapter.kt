package com.example.noteskeeping.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.noteskeeping.R
import com.example.noteskeeping.model.Notes

class NoteRecyclerViewAdapter(private var noteList: ArrayList<Notes>) :
    RecyclerView.Adapter<NoteRecyclerViewAdapter.NoteViewHolder>() , Filterable {

    private var searchNoteList : ArrayList<Notes> = arrayListOf()
    var allNotes = mutableListOf<Notes>().apply {
        addAll(noteList)
        notifyDataSetChanged()
    }
//    lateinit var searchNoteList : List<Notes>
    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var noteTitle: TextView
        var noteContent: TextView
        var menu: ImageView
        lateinit var noteToBeDeleted: String


        init {
            noteTitle = itemView.findViewById(R.id.note_title)
            noteContent = itemView.findViewById(R.id.note_content)
            menu = itemView.findViewById(R.id.edit_delete_menu)
//            searchNoteList = noteList
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
                            bundle.putInt("makeNote_archeive",2)
                            fragment.arguments = bundle
                            val transaction = it.context as AppCompatActivity
                            transaction.supportFragmentManager.beginTransaction()
                                .replace(R.id.home_activity_fragment_container, fragment)
                                .addToBackStack(null)
                                .commit()

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

    override fun getFilter(): Filter {
        searchNoteList = noteList
       var filter = object : Filter(){
           override fun performFiltering(p0: CharSequence?): FilterResults {
               var filterResults = FilterResults()
               if(p0 == null || p0.isEmpty()){
                   filterResults.values = searchNoteList
                   filterResults.count = searchNoteList.size
               }else{
                   var searchChar = p0.toString().toLowerCase()
                   var filteredResults = ArrayList<Notes>()
                   for(noteModel in searchNoteList){
                       if(noteModel.notes.toLowerCase().contains(searchChar) || noteModel.title.toLowerCase().contains(searchChar)){
                           filteredResults.add(noteModel)
                       }
                   }
                   filterResults.values = filteredResults
                   filterResults.count = filteredResults.size
               }
               return filterResults
           }

           override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
               searchNoteList = p1?.values!! as ArrayList<Notes> /* = java.util.ArrayList<com.example.noteskeeping.model.Notes> */
               notifyDataSetChanged()
           }
       }
        return filter
    }

//    fun filterList(filterList: ArrayList<Notes>){
//        allNotes = filterList.toMutableList()
//        notifyDataSetChanged()
//    }

}