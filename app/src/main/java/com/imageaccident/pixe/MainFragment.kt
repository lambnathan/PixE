package com.imageaccident.pixe

import android.os.Bundle
import android.service.autofill.FillEventHistory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.imageaccident.pixe.data.ImageCreationFragment

class MainFragment :  Fragment(){
    private val logTag = "ImageAccident.MainFrag"
    private lateinit var takePhotoButton : Button
    private lateinit var choosePictureButton : Button
    private lateinit var generateButton : Button
    private lateinit var algorithmButton: Button
    private lateinit var orientationButton: Button
    private lateinit var historyButton: Button

    private var hasChosenPicture: Boolean = false
    private var hasChosenAlgorithm: Boolean = false
    private var hasChosenOrientation: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(logTag, "onCreateView()")
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        takePhotoButton = view.findViewById(R.id.take_photo_button)
        choosePictureButton = view.findViewById(R.id.choose_picture_button)
        generateButton = view.findViewById(R.id.generate_button)
        algorithmButton = view.findViewById(R.id.algorithm_button)
        orientationButton = view.findViewById(R.id.orientation_button)
        historyButton = view.findViewById(R.id.history_button)

        takePhotoButton.setOnClickListener{
            Toast.makeText(requireContext(), "User will be directed to Camera app to take photo", Toast.LENGTH_SHORT).show()
            hasChosenPicture = true
            checkGenerateButton()
        }
        choosePictureButton.setOnClickListener{
            Toast.makeText(requireContext(), "User will be directed to Photo Gallery app to choose picture", Toast.LENGTH_SHORT).show()
            hasChosenPicture = true
            checkGenerateButton()
        }
        generateButton.setOnClickListener{
            val fragment = GeneratedFragment()
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.content_frame, fragment)
                .addToBackStack(null)
                .commit()

        }

        historyButton.setOnClickListener {
            val fragment = ImageCreationFragment.newInstance()
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.content_frame, fragment)
                .addToBackStack(null)
                .commit()
        }

        generateButton.visibility = View.INVISIBLE


        algorithmButton.setOnClickListener{ showAlgoMenu(algorithmButton)}
        orientationButton.setOnClickListener { showOrientationMenu(orientationButton)}

        return view
    }

    private fun checkGenerateButton(){
        if(hasChosenAlgorithm && hasChosenOrientation && hasChosenPicture){
            generateButton.visibility = View.VISIBLE
        }
    }

    private fun showAlgoMenu(button: Button){
        val popupMenu = PopupMenu(requireContext(), button)
        popupMenu.menuInflater.inflate(R.menu.algorithm_popup_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener {item ->
            when(item.itemId){
                R.id.sort_rgb ->{
                    Toast.makeText(requireContext(), "You Clicked : " + item.title, Toast.LENGTH_SHORT).show()
                    hasChosenAlgorithm = true
                    checkGenerateButton()
                }
                R.id.sort_hue -> {
                    Toast.makeText(requireContext(), "You Clicked : " + item.title, Toast.LENGTH_SHORT).show()
                    hasChosenAlgorithm = true
                    checkGenerateButton()
                }
                R.id.sort_gamma -> {
                    Toast.makeText(requireContext(), "You Clicked : " + item.title, Toast.LENGTH_SHORT).show()
                    hasChosenAlgorithm = true
                    checkGenerateButton()
                }
            }
            true

        }
        popupMenu.show()
    }

    private fun showOrientationMenu(button: Button){
        val popupMenu = PopupMenu(requireContext(), button)
        popupMenu.menuInflater.inflate(R.menu.orientation_pop_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener {item ->
            when(item.itemId){
                R.id.orientation_horizontal ->{
                    Toast.makeText(requireContext(), "You Clicked : " + item.title, Toast.LENGTH_SHORT).show()
                    hasChosenOrientation = true
                    checkGenerateButton()
                }
                R.id.orientation_vertical -> {
                    Toast.makeText(requireContext(), "You Clicked : " + item.title, Toast.LENGTH_SHORT).show()
                    hasChosenOrientation = true
                    checkGenerateButton()
                }
            }
            true

        }
        popupMenu.show()
    }
}