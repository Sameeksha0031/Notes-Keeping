package com.example.noteskeeping.view

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import android.widget.Toast
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
    lateinit var recyclerView: RecyclerView
    //lateinit var noteAdapter : ArrayAdapter<String>
    lateinit var noteRecyclerViewAdapter: NoteRecyclerViewAdapter //
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
        noteRecyclerViewAdapter = NoteRecyclerViewAdapter(noteList) //
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
//        var inputTitle = arguments ?.getString("title")
//        var context = arguments ?.getString("content")
//        if (inputTitle != null && context != null) {
//            noteList.add(element = inputTitle)
//            noteList.add(context)
//        }

        removeNote()
        puttingNoteInArchiev()
    }

//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        super.onCreateOptionsMenu(menu, inflater)
//        inflater.inflate(R.menu.notes_menu,menu)
//        return
//    }
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
                recyclerView.adapter = NoteRecyclerViewAdapter(it.noteArrayList)
                Toast.makeText(context, it.msg, Toast.LENGTH_SHORT).show()
                noteList = it.noteArrayList
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

        //val manager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchItem = menu?.findItem(R.id.search_bar)
        val searchView = searchItem?.actionView as androidx.appcompat.widget.SearchView

        //searchView.setSearchableInfo(manager.getSearchableInfo(componentName))

        searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
//                searchView.clearFocus()
//                searchView.setQuery("", false)
//                searchItem.collapseActionView()
//                Toast.makeText(context, "Looking for $query", Toast.LENGTH_SHORT).show()
////                if(noteList.contains(query)){
////                   noteList.filter {
////
////                   }
////                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
               recyclerView.adapter = noteRecyclerViewAdapter
                noteRecyclerViewAdapter.filter.filter(newText)
                return true
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

    fun puttingNoteInArchiev(){
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

//    fun filter(text : String){
//        var filteredList =  ArrayList<Notes>()
//        Log.d("NoteFragment","${noteList.size}")
//         for(item in noteList){
//             if(item.notes.toLowerCase(Locale.getDefault()).contains(text.toLowerCase())){
//                 filteredList.add(item)
//             }
//         }
//        if(filteredList.isEmpty()){
//            Toast.makeText(requireContext(),"No Data Found...",Toast.LENGTH_SHORT).show()
//        }else{
//            noteRecyclerViewAdapter.filterList(filteredList)
//            recyclerView.adapter?.notifyDataSetChanged()
//        }
//    }
}
