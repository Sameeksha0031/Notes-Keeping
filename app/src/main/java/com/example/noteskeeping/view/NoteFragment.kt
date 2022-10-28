package com.example.noteskeeping.view

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.MenuItemCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.noteskeeping.R
import com.example.noteskeeping.database.DataBaseHelper
import com.example.noteskeeping.databinding.FragmentNoteBinding
import com.example.noteskeeping.model.NoteServices
import com.example.noteskeeping.model.Notes
import com.example.noteskeeping.viewModel.NotesViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*
import kotlin.collections.ArrayList

class NoteFragment : Fragment() {
    lateinit var binding: FragmentNoteBinding
    lateinit var floatingActionButton: FloatingActionButton
    lateinit var notesViewModel: NotesViewModel
    lateinit var noteList: ArrayList<Notes> //
    lateinit var filteredList : ArrayList<Notes>
    lateinit var archiveNoteList : ArrayList<Notes>
    lateinit var recyclerView: RecyclerView
   // lateinit var noteRecyclerViewAdapter: NoteRecyclerViewAdapter //
    var currentViewMode : Int = 0
    val viewModeListView : Int = 0
    val viewModeGridView : Int = 1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNoteBinding.bind(view)
        floatingActionButton = FloatingActionButton(requireContext())
        notesViewModel = NotesViewModel(NoteServices(DataBaseHelper(requireContext())))
        setHasOptionsMenu(true)

        recyclerView = binding.recyclerViewNoteList
        noteList = ArrayList<Notes>() //
        filteredList = ArrayList<Notes>()
        //noteRecyclerViewAdapter = NoteRecyclerViewAdapter(noteList) //
        recyclerView.setHasFixedSize(true)

        recyclerView.layoutManager = LinearLayoutManager(requireActivity())

        displayAllNotesInRecyclerView()

        floatingActionButton = binding.floatingButton
        floatingActionButton.setOnClickListener {
            Toast.makeText(context, "Floating button is click", Toast.LENGTH_SHORT).show()
            val fragment = CreatingNotesFragment()
            fragmentManager?.beginTransaction()
                ?.replace(R.id.home_activity_fragment_container, fragment)?.commit()
        }

        removeNote()
        //puttingNoteInArchiev()
    }

    var OperationToBePerform : Int ?= null
    fun removeNote(){
       // var OperationToBePerform : Int ?= null
        var noteId : String
        OperationToBePerform  = arguments?.getInt("perform_deletion")
        if(OperationToBePerform != null && OperationToBePerform == 1){
            noteId = arguments?.getString("noteId")!!.toString()
            notesViewModel.deleteNote(noteId)
            notesViewModel.deleteNote.observe(viewLifecycleOwner,Observer{
                if(it.status){
                    Toast.makeText(context,it.msg,Toast.LENGTH_SHORT).show()
                    recyclerView.adapter?.notifyDataSetChanged()
                }
            })
        }
    }

    fun displayAllNotesInRecyclerView(){
        notesViewModel.getNotes()
        notesViewModel.readNote.observe(viewLifecycleOwner, Observer {
            if (it.status) {
                noteList = it.noteArrayList
                filteredList.addAll(noteList)
                recyclerView.adapter = NoteRecyclerViewAdapter(noteList)
                Toast.makeText(context, it.msg, Toast.LENGTH_SHORT).show()
             // noteRecyclerViewAdapter = NoteRecyclerViewAdapter(it.noteArrayList)
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.search_bar -> return true
            R.id.grid_linear_view -> {
                if(viewModeListView == currentViewMode){
                    switchView(item)
                    recyclerView.layoutManager = GridLayoutManager(requireActivity(),2)
                    currentViewMode = viewModeGridView
                }else{
                    switchView(item)
                    recyclerView.layoutManager = LinearLayoutManager(requireActivity())
                    currentViewMode = viewModeListView
                }

                return true
            }
            R.id.profile_pic -> {
                //Toast.makeText(context, "Profile is selected", Toast.LENGTH_SHORT).show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        val inflater = menuInflater
        inflater.inflate(R.menu.search_menu, menu)

        var menuItem = menu?.findItem(R.id.profile_pic)
        var view = MenuItemCompat.getActionView(menuItem)
        var profileImage: CircleImageView = view.findViewById(R.id.profile_image)

        profileImage.setOnClickListener {
            Toast.makeText(context, "Profile is selected", Toast.LENGTH_SHORT).show()
            val dialog = CustomDialogFragment()
            dialog.show(childFragmentManager, "custom Dialog")
        }

        //val manager = getSystemService(requireContext(),Ser) as SearchManager
        val searchItem = menu?.findItem(R.id.search_bar)
        val searchView = searchItem?.actionView as androidx.appcompat.widget.SearchView

        //searchView.setSearchableInfo(manager.getSearchableInfo(componentName))

        searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filteredList.clear()
                val searchText = newText!!.toLowerCase(Locale.getDefault())
                if(searchText.isNotEmpty()){
                    for(note in noteList){
                        if(note.notes.toLowerCase(Locale.getDefault()).contains(searchText) || note.title.toLowerCase(Locale.getDefault()).contains(searchText) )
                        filteredList.add(note)
                    }
                    recyclerView.adapter = NoteRecyclerViewAdapter(filteredList)
                }else{
                    filteredList.clear()
                    filteredList.addAll(noteList)
                    recyclerView.adapter = NoteRecyclerViewAdapter(noteList)
                }
                return false
            }
        })
        return super.onCreateOptionsMenu(menu, inflater)
    }

    fun switchView(menuItem: MenuItem){
        if(currentViewMode == viewModeGridView){
            menuItem.setIcon(resources.getDrawable(R.drawable.ic_baseline_grid_view_24))
            currentViewMode = viewModeListView
        }else{
            menuItem.setIcon(resources.getDrawable(R.drawable.ic_baseline_horizontal_split_24))
            currentViewMode = viewModeGridView
        }
    }

    fun puttingNoteInArchiev(notes: Notes){
        if(notes.noteIsArchive == true){
            noteList.remove(notes)
            archiveNoteList.add(notes)
        }
        var archive = ArrayList<Notes>()
        var noteId = arguments?.get("noteId")
        OperationToBePerform = arguments?.getInt("makenote_archive")
        if(OperationToBePerform != null && OperationToBePerform == 2){
            notesViewModel.readSingleNote(noteId as String)
            notesViewModel.readSingleNote.observe(viewLifecycleOwner , Observer {
                if(it.status){
                   // it.notes
                }
            })
        }
    }
}
