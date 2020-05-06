package com.imageaccident.pixe

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.service.autofill.FillEventHistory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.core.content.PermissionChecker
import androidx.fragment.app.Fragment
import com.imageaccident.pixe.data.ImageCreation
import com.imageaccident.pixe.data.ImageCreationFragment
import com.imageaccident.pixe.data.ImageRepository
import java.io.File
import java.lang.Exception

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
    private lateinit var imagePreview: ImageView

    private lateinit var imageUri: Uri
    private lateinit var captureUri: Uri
    private lateinit var captureFile: File

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
        imagePreview = view.findViewById(R.id.image_preview)

        takePhotoButton.setOnClickListener{
            Toast.makeText(requireContext(), "User will be directed to Camera app to take photo", Toast.LENGTH_SHORT).show()
            requestImageFromCamera()
            hasChosenPicture = true
            checkGenerateButton()
        }
        choosePictureButton.setOnClickListener{
            //Toast.makeText(requireContext(), "User will be directed to Photo Gallery app to choose picture", Toast.LENGTH_SHORT).show()
            pickImageFromGallery()
            hasChosenPicture = true
            checkGenerateButton()
        }
        generateButton.setOnClickListener{
            // play generated sound
            try {
                val mediaPlayer = MediaPlayer.create(requireContext(), R.raw.generate_sound)
                mediaPlayer.start()
            } catch (e: Exception) {
                e.printStackTrace()
            }


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

    private fun requestImageFromCamera(){

        val dir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        Log.d(logTag, "The directory given is ${dir}")
        captureFile = File.createTempFile("camera_capture", ".jpg", dir)
        captureUri = FileProvider.getUriForFile(
            requireActivity(),
            "com.imageaccident.pixe.fileprovider",
            captureFile
        )
        val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        i.putExtra(MediaStore.EXTRA_OUTPUT, captureUri)
        startActivityForResult(i, TAKE_PICTURE_ID)

    }

    //creates implicit intent to
    //choose the image from the gallery
    fun pickImageFromGallery(){
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
            PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) ==
            PackageManager.PERMISSION_GRANTED) {

            startActivityForResult(
                Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                ), PICK_IMAGE_ID
            )
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), PermissionChecker.PERMISSION_GRANTED
            )
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        Log.d(logTag, "returned result with code ${requestCode} and result ${resultCode}")


        if(resultCode == RESULT_OK ){
            Log.d(logTag, "returned result for selecting picture ")
            if(requestCode == PICK_IMAGE_ID && data != null){
                imageUri = data.data!!
                imagePreview.setImageURI(imageUri)
            }
            if(requestCode == TAKE_PICTURE_ID){
                val dir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                Log.d(logTag, "returned result for taking picture " + (dir.toString() +"camera_capture"+ ".jpg"))
                Log.d(logTag, "path chould be ${captureFile!!.getAbsolutePath()}")

                captureUri = FileProvider.getUriForFile(requireActivity(), "com.imageaccident.pixe.fileprovider", captureFile)
                imagePreview.setImageURI(captureUri)

                imageUri = captureUri

                //val myBitmap = BitmapFactory.decodeFile(captureFile!!.getAbsolutePath())
                //imagePreview.setImageBitmap(myBitmap)

            }
        }
    }

    private fun showAlgoMenu(button: Button){
        val popupMenu = PopupMenu(requireContext(), button)
        popupMenu.menuInflater.inflate(R.menu.algorithm_popup_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener {item ->
            when(item.itemId){
                R.id.sort_red ->{
                    Toast.makeText(requireContext(), "You Clicked : " + item.title, Toast.LENGTH_SHORT).show()
                    hasChosenAlgorithm = true
                    algorithm = "red"
                    checkGenerateButton()
                }
                R.id.sort_blue -> {
                    Toast.makeText(requireContext(), "You Clicked : " + item.title, Toast.LENGTH_SHORT).show()
                    hasChosenAlgorithm = true
                    algorithm = "blue"
                    checkGenerateButton()
                }
                R.id.sort_green -> {
                    Toast.makeText(requireContext(), "You Clicked : " + item.title, Toast.LENGTH_SHORT).show()
                    hasChosenAlgorithm = true
                    algorithm = "green"
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