package com.imageaccident.pixe

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.service.autofill.FillEventHistory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.imageaccident.pixe.data.ImageCreation
import com.imageaccident.pixe.data.ImageCreationFragment
import com.imageaccident.pixe.data.ImageRepository

private const val PICK_IMAGE_ID = 1
private const val TAKE_PICTURE_ID = 2

class MainFragment :  Fragment(){
    private val logTag = "ImageAccident.MainFrag"
    private lateinit var takePhotoButton : Button
    private lateinit var choosePictureButton : Button
    private lateinit var generateButton : Button
    private lateinit var algorithmButton: Button
    private lateinit var orientationButton: Button
    private lateinit var historyButton: Button
    private lateinit var imageRepository: ImageRepository

    private lateinit var imageUri: Uri

    private var hasChosenPicture: Boolean = false
    private var hasChosenAlgorithm: Boolean = false
    private var hasChosenOrientation: Boolean = false
    private var algorithm = ""
    private var orientation = ""

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

        imageRepository = ImageRepository.getInstance(requireContext())

        takePhotoButton.setOnClickListener{
            Toast.makeText(requireContext(), "User will be directed to Camera app to take photo", Toast.LENGTH_SHORT).show()
            hasChosenPicture = true
            checkGenerateButton()
        }
        choosePictureButton.setOnClickListener{
            Toast.makeText(requireContext(), "User will be directed to Photo Gallery app to choose picture", Toast.LENGTH_SHORT).show()
            pickImageFromGallery()
            hasChosenPicture = true
            checkGenerateButton()
        }
        generateButton.setOnClickListener{
            val newCreation = ImageCreation(
                algorithm = algorithm,
                orientation = orientation,
                version = "FREE")
            imageRepository.addImageCreation(newCreation)

            //generated fragment needs an imageUri to the image to use,
            //the algorithm to use, and the orientation
            val fragment = GeneratedFragment(algorithm, orientation, imageUri)
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

    //creates implicit intent to
    //choose the image from the gallery
    fun pickImageFromGallery(){
        startActivityForResult(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), PICK_IMAGE_ID)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == RESULT_OK && data != null){
            if(requestCode == PICK_IMAGE_ID){
                imageUri = data.data!!
            }
            else{ //must be TAKE_PICTURE_ID, for the camera

            }
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
                    algorithm = "RGB"
                    checkGenerateButton()
                }
                R.id.sort_hue -> {
                    Toast.makeText(requireContext(), "You Clicked : " + item.title, Toast.LENGTH_SHORT).show()
                    hasChosenAlgorithm = true
                    algorithm = "HUE"
                    checkGenerateButton()
                }
                R.id.sort_gamma -> {
                    Toast.makeText(requireContext(), "You Clicked : " + item.title, Toast.LENGTH_SHORT).show()
                    hasChosenAlgorithm = true
                    algorithm = "GAMMA"
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
                    orientation = "Horizontal"
                    checkGenerateButton()
                }
                R.id.orientation_vertical -> {
                    Toast.makeText(requireContext(), "You Clicked : " + item.title, Toast.LENGTH_SHORT).show()
                    hasChosenOrientation = true
                    orientation = "Vertical"
                    checkGenerateButton()
                }
            }
            true

        }
        popupMenu.show()
    }
}