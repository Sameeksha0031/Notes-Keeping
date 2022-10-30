package com.example.noteskeeping.view

import android.app.SearchManager
import android.content.Context
import android.content.Intent
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
                R.id.archive -> {
                    replaceFragment(ArchiveNoteFragment())
                    Toast.makeText(
                    this,
                    "Clicked Archive",
                    Toast.LENGTH_SHORT
                ).show()}
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
                R.id.remainders -> {
                    replaceFragment(ReminderFragment())
                    Toast.makeText(
                    this,
                    "Clicked Reminder",
                    Toast.LENGTH_SHORT
                ).show()}
            }
            true
        }
        replaceFragment(NoteFragment())
    }

    private fun replaceFragment(homeFragment: Fragment) {
        val supportFragment = supportFragmentManager
        val fragment_Transaction = supportFragment.beginTransaction()
        fragment_Transaction.replace(R.id.home_activity_fragment_container, homeFragment)
        fragment_Transaction.commit()
    }

}