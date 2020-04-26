package com.imageaccident.pixe

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val logTag = "ImageAccident.MainAct"
    private lateinit var toggle : ActionBarDrawerToggle
    private lateinit var drawerLayout : DrawerLayout



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(logTag, "onCreate()")
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawer_layout)

        toolbar.setTitleTextColor(Color.WHITE)
        setSupportActionBar(toolbar)
        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navigationView.setNavigationItemSelectedListener(this)

        toggle.isDrawerIndicatorEnabled = true
        toolbar.setOnClickListener {
            Toast.makeText(baseContext, "toolbar", Toast.LENGTH_SHORT).show()
        }
        navigationView.setOnClickListener {
            Toast.makeText(baseContext, "nav view", Toast.LENGTH_SHORT).show()
        }
        drawerLayout.setOnClickListener {
            Toast.makeText(baseContext, "drawer", Toast.LENGTH_SHORT).show()
        }



        val currentFragment = supportFragmentManager.findFragmentById(R.id.content_frame)
        if (currentFragment == null) {
            val fragment = MainFragment()
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit()
        }
    }

//    private fun checkGenerateButton(){
//        if(hasChosenAlgorithm && hasChosenOrientation && hasChosenPicture){
//            generateButton.visibility = View.VISIBLE
//        }
//    }
//
//    private fun showAlgoMenu(button: Button){
//        val popupMenu = PopupMenu(this, button)
//        popupMenu.menuInflater.inflate(R.menu.algorithm_popup_menu, popupMenu.menu)
//        popupMenu.setOnMenuItemClickListener {item ->
//            when(item.itemId){
//                R.id.sort_rgb ->{
//                    Toast.makeText(this, "You Clicked : " + item.title, Toast.LENGTH_SHORT).show()
//                    hasChosenAlgorithm = true
//                    checkGenerateButton()
//                }
//                R.id.sort_hue -> {
//                    Toast.makeText(this, "You Clicked : " + item.title, Toast.LENGTH_SHORT).show()
//                    hasChosenAlgorithm = true
//                    checkGenerateButton()
//                }
//                R.id.sort_gamma -> {
//                    Toast.makeText(this, "You Clicked : " + item.title, Toast.LENGTH_SHORT).show()
//                    hasChosenAlgorithm = true
//                    checkGenerateButton()
//                }
//            }
//            true
//
//        }
//        popupMenu.show()
//    }
//
//    private fun showOrientationMenu(button: Button){
//        val popupMenu = PopupMenu(this, button)
//        popupMenu.menuInflater.inflate(R.menu.orientation_pop_menu, popupMenu.menu)
//        popupMenu.setOnMenuItemClickListener {item ->
//            when(item.itemId){
//                R.id.orientation_horizontal ->{
//                    Toast.makeText(this, "You Clicked : " + item.title, Toast.LENGTH_SHORT).show()
//                    hasChosenOrientation = true
//                    checkGenerateButton()
//                }
//                R.id.orientation_vertical -> {
//                    Toast.makeText(this, "You Clicked : " + item.title, Toast.LENGTH_SHORT).show()
//                    hasChosenOrientation = true
//                    checkGenerateButton()
//                }
//            }
//            true
//
//        }
//        popupMenu.show()
//    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        Log.d(logTag, "onNavigationItemSelected()")
        when (menuItem.itemId) {
            R.id.about_button -> {
                val fragment = AboutFragment()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .addToBackStack(null)
                    .commit()
            }
            R.id.donate_button -> {
                Toast.makeText(this, "Thanks for the donation!", Toast.LENGTH_SHORT).show()
            }
            R.id.upgrade_button -> {
                Toast.makeText(this, "Upgrade to full-resolution images!", Toast.LENGTH_SHORT).show()
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        Log.d(logTag, "onBackPressed()")
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
