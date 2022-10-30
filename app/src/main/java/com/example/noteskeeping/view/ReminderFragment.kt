package com.example.noteskeeping.view

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.noteskeeping.R
import com.example.noteskeeping.database.DataBaseHelper
import com.example.noteskeeping.databinding.FragmentNoteBinding
import com.example.noteskeeping.databinding.FragmentReminderBinding
import com.example.noteskeeping.model.NoteServices
import com.example.noteskeeping.model.Notes
import com.example.noteskeeping.viewModel.NotesViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class ReminderFragment : Fragment() {
    lateinit var binding : FragmentReminderBinding
    lateinit var noteList: ArrayList<Notes> //
    lateinit var filteredList : ArrayList<Notes>
    lateinit var recyclerView: RecyclerView
    lateinit var floatingActionButton: FloatingActionButton
    var currentViewMode : Int = 0
    val viewModeListView : Int = 0
    val viewModeGridView : Int = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reminder, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentReminderBinding.bind(view)
        floatingActionButton = FloatingActionButton(requireContext())
       // notesViewModel = NotesViewModel(NoteServices(DataBaseHelper(requireContext())))
        setHasOptionsMenu(true)
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
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        val inflater = menuInflater
        inflater.inflate(R.menu.search_menu, menu)

        val searchItem = menu?.findItem(R.id.search_bar)
        val searchView = searchItem?.actionView as androidx.appcompat.widget.SearchView

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
                        if(note.notes.toLowerCase(Locale.getDefault()).contains(searchText) || note.title.toLowerCase(
                                Locale.getDefault()).contains(searchText) )
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
}