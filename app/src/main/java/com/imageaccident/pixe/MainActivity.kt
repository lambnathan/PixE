package com.imageaccident.pixe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var toggle : ActionBarDrawerToggle
    private lateinit var drawerLayout : DrawerLayout
    private lateinit var takePhotoButton : Button
    private lateinit var choosePictureButton : Button
    private lateinit var generateButton : Button
    private lateinit var algorithmButton: Button
    private lateinit var orientationButton: Button

    private var hasChosenPicture: Boolean = false
    private var hasChosenAlgorithm: Boolean = false
    private var hasChosenOrientation: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawer_layout)
        takePhotoButton = findViewById(R.id.take_photo_button)
        choosePictureButton = findViewById(R.id.choose_picture_button)
        generateButton = findViewById(R.id.generate_button)
        algorithmButton = findViewById(R.id.algorithm_button)
        orientationButton = findViewById(R.id.orientation_button)

        setSupportActionBar(toolbar)
        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navigationView.setNavigationItemSelectedListener(this)

        toggle.isDrawerIndicatorEnabled = true

        takePhotoButton.setOnClickListener{Toast.makeText(baseContext, "User will be directed to Camera app to take photo", Toast.LENGTH_SHORT).show()
            hasChosenPicture = true
            checkGenerateButton()
        }
        choosePictureButton.setOnClickListener{Toast.makeText(baseContext, "User will be directed to Photo Gallery app to choose picture", Toast.LENGTH_SHORT).show()
            hasChosenPicture = true
            checkGenerateButton()
        }
        generateButton.setOnClickListener{
            Toast.makeText(baseContext, "New image will be generated!", Toast.LENGTH_SHORT).show()
            // booleans to make this work or not
        }

        generateButton.visibility = View.INVISIBLE


        algorithmButton.setOnClickListener{ showAlgoMenu(algorithmButton)}
        orientationButton.setOnClickListener { showOrientationMenu(orientationButton)}
    }

    private fun checkGenerateButton(){
        if(hasChosenAlgorithm && hasChosenOrientation && hasChosenPicture){
            generateButton.visibility = View.VISIBLE
        }
    }

    private fun showAlgoMenu(button: Button){
        val popupMenu = PopupMenu(this, button)
        popupMenu.menuInflater.inflate(R.menu.algorithm_popup_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener {item ->
            when(item.itemId){
                R.id.sort_rgb ->{
                    Toast.makeText(this, "You Clicked : " + item.title, Toast.LENGTH_SHORT).show()
                    hasChosenAlgorithm = true
                    checkGenerateButton()
                }
                R.id.sort_hue -> {
                    Toast.makeText(this, "You Clicked : " + item.title, Toast.LENGTH_SHORT).show()
                    hasChosenAlgorithm = true
                    checkGenerateButton()
                }
                R.id.sort_gamma -> {
                    Toast.makeText(this, "You Clicked : " + item.title, Toast.LENGTH_SHORT).show()
                    hasChosenAlgorithm = true
                    checkGenerateButton()
                }
            }
            true

        }
        popupMenu.show()
    }

    private fun showOrientationMenu(button: Button){
        val popupMenu = PopupMenu(this, button)
        popupMenu.menuInflater.inflate(R.menu.orientation_pop_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener {item ->
            when(item.itemId){
                R.id.orientation_horizontal ->{
                    Toast.makeText(this, "You Clicked : " + item.title, Toast.LENGTH_SHORT).show()
                    hasChosenOrientation = true
                    checkGenerateButton()
                }
                R.id.orientation_vertical -> {
                    Toast.makeText(this, "You Clicked : " + item.title, Toast.LENGTH_SHORT).show()
                    hasChosenOrientation = true
                    checkGenerateButton()
                }
            }
            true

        }
        popupMenu.show()
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.about_button -> {
                Toast.makeText(this, "About", Toast.LENGTH_SHORT).show()
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
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
