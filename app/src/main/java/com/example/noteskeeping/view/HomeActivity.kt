package com.example.noteskeeping.view

import android.app.SearchManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.MenuItemCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.noteskeeping.R
import com.example.noteskeeping.databinding.ActivityHomeBinding
import com.example.noteskeeping.databinding.ActivityHomeBinding.inflate
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import de.hdodenhof.circleimageview.CircleImageView


class HomeActivity : AppCompatActivity() {
    lateinit var binding: ActivityHomeBinding
    lateinit var auth: FirebaseAuth
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var toolbar: MaterialToolbar
    lateinit var circleImageView: CircleImageView
    var bundle = Bundle()
    var defaultview : Int = 0
    val noteFragment = NoteFragment()

    override fun onCreate(savedInstanceState: Bundle?) { //make const class take two grid by default -> 1 , linear -> 2 , display
        super.onCreate(savedInstanceState)
        binding = inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        toolbar = binding.customToolbar
        setSupportActionBar(toolbar)

        circleImageView = CircleImageView(this)

        var drawerLayout: DrawerLayout = binding.drawerLayout
        var navView: NavigationView = binding.navigationView
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        getSupportActionBar()?.setDisplayShowTitleEnabled(false);

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.notes -> Toast.makeText(this, "Clicked Notes", Toast.LENGTH_SHORT)
                    .show()
                R.id.add -> Toast.makeText(this, "Clicked Create new Note", Toast.LENGTH_SHORT)
                    .show()
                R.id.Delete -> Toast.makeText(this, "Clicked Delete", Toast.LENGTH_SHORT)
                    .show()
                R.id.archive -> Toast.makeText(
                    this,
                    "Clicked Archive",
                    Toast.LENGTH_SHORT
                ).show()
                R.id.setting -> Toast.makeText(
                    this,
                    "Clicked Setting",
                    Toast.LENGTH_SHORT
                ).show()
                R.id.help_and_feedback -> Toast.makeText(
                    this,
                    "Clicked Help and Feedback",
                    Toast.LENGTH_SHORT
                ).show()
                R.id.remainders -> Toast.makeText(
                    this,
                    "Clicked Reminder",
                    Toast.LENGTH_SHORT
                ).show()
            }
            true
        }
        replaceFragment(NoteFragment())

//        bundle.putInt("ByDefaultLinearView",defaultview)
//        noteFragment.arguments = bundle


    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            R.id.search_bar -> return true
//            R.id.grid_linear_view -> {
//               defaultview = 1
//                bundle.putInt("GridViewSelected",defaultview)
//               // val fragment = NoteFragment()
//                noteFragment.arguments = bundle
//                return true
//            }
//            R.id.profile_pic -> {
//                //Toast.makeText(context, "Profile is selected", Toast.LENGTH_SHORT).show()
//                return true
//            }
//        }
//        return super.onOptionsItemSelected(item)
//    }


    private fun replaceFragment(homeFragment: Fragment) {
        val supportFragment = supportFragmentManager
        val fragment_Transaction = supportFragment.beginTransaction()
        fragment_Transaction.replace(R.id.home_activity_fragment_container, homeFragment)
        fragment_Transaction.commit()
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        val inflater = menuInflater
//        inflater.inflate(R.menu.search_menu, menu)
//
//        var menuItem = menu?.findItem(R.id.profile_pic)
//        var view = MenuItemCompat.getActionView(menuItem)
//        var profileImage: CircleImageView = view.findViewById(R.id.profile_image)
//
//        profileImage.setOnClickListener {
//            Toast.makeText(this, "Profile is selected", Toast.LENGTH_SHORT).show()
//            val dialog = CustomDialogFragment()
//            dialog.show(supportFragmentManager, "custom Dialog")
//        }
//
//        val manager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
//        val searchItem = menu?.findItem(R.id.search_bar)
//        val searchView = searchItem?.actionView as androidx.appcompat.widget.SearchView
//
//        searchView.setSearchableInfo(manager.getSearchableInfo(componentName))
//
//        searchView.setOnQueryTextListener(object :
//            androidx.appcompat.widget.SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                searchView.clearFocus()
//                searchView.setQuery("", false)
//                searchItem.collapseActionView()
//                Toast.makeText(this@HomeActivity, "Looking for $query", Toast.LENGTH_SHORT).show()
//                return true
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                return false
//            }
//        })
//        return true
//    }
}