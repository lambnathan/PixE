package com.imageaccident.pixe

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Environment.getExternalStorageDirectory
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream

import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import java.lang.Exception
import java.util.*
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import com.imageaccident.pixe.data.ImageCreation
import com.imageaccident.pixe.data.ImageRepository


class GeneratedFragment(private val algorithm: String, private val orientation: String, private val imageUri: Uri) : Fragment() {
    private var logTag = "ImageAccident.GenFrag"
    private lateinit var imageView : ImageView
    private lateinit var resetButton : Button
    private lateinit var shareButton : Button
    private lateinit var saveButton: Button
    private lateinit var imageRepository: ImageRepository

    private lateinit var saveFile: File
    private lateinit var saveUri: Uri

    private var makeHistory: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imageRepository = ImageRepository.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(logTag, "onCreateView()")
        val view = inflater.inflate(R.layout.activity_generated, container, false)
        imageView = view.findViewById(R.id.generated_image_view)
        resetButton = view.findViewById(R.id.reset_button)
        shareButton = view.findViewById(R.id.share_button)
        saveButton = view.findViewById(R.id.save_button)

        resetButton.setOnClickListener {
            startActivity(Intent(requireContext(), MainActivity::class.java))
        }

        shareButton.setOnClickListener {
            Log.d(logTag, "In")
            sendImage()
        }
        saveButton.setOnClickListener {

            val result = saveImage(imageView.drawable.toBitmap())
            if (result) {
                Toast.makeText(requireContext(), "Saved successfully!", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(requireContext(), "Error saving, try again.", Toast.LENGTH_SHORT).show()
            }
        }

        //display the chose/take image:
        //imageView.setImageURI(imageUri)
//        val inputStream = requireActivity().contentResolver.openInputStream(imageUri)
//        val drawable = Drawable.createFromStream(inputStream, imageUri.toString())
//        imageView.setImageBitmap(drawable.toBitmap())
        Log.d(logTag, "URI: ${imageUri.path}")

        return view
    }

    override fun onResume() {
        super.onResume()
        sortImage()
    }

    //create a pixel sorter object with specified algorithm and orientation
    //and get the sorted image
    private fun sortImage(){
        val pixelSorter = PixelSorter(algorithm, orientation, imageUri, requireContext(), requireActivity())
        val bitmap: Bitmap = pixelSorter.generateImage()
        Log.d(logTag, "gneerated size: ${bitmap.width}, ${bitmap.height}")
        imageView.setImageBitmap(bitmap) //display the sorted image
}

    private fun sendImage() {
        if(saveUri == null) {
            Toast.makeText(requireContext(), "You must save the image before sending!", Toast.LENGTH_SHORT).show()
            return
        }
        try {
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {

                if (saveUri != null) {
                    var sendPic = Intent(Intent.ACTION_SEND)
                    sendPic.putExtra(Intent.EXTRA_STREAM, saveUri)
                    sendPic.setType("image/jpg")
                    startActivity(sendPic)
                } else {
                    Toast.makeText(requireContext(), "Image must be saved before sharing!", Toast.LENGTH_SHORT).show()
                }



            } else {
                 requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
                     Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSION_GRANTED)

            }

//=======
//            var sendPic = Intent(Intent.ACTION_SEND)
//            sendPic.putExtra(Intent.EXTRA_STREAM, saveUri)
//            sendPic.setType("image/jpg")
//            startActivity(sendPic)
//>>>>>>> 5a04d76708d71d39e755e12d78f1dd9b843c8813
        }catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Error sending image.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveImage(bitmap: Bitmap) : Boolean {

        try {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) ==
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                val gen = Random()
                var temp = gen.nextInt(99999)
                val filename = "img$temp.jpg"

                val dir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                saveFile = File.createTempFile(filename, ".jpg", dir)
                saveUri = FileProvider.getUriForFile(
                    requireActivity(),
                    "com.imageaccident.pixe.fileprovider",
                    saveFile
                )
                val out = FileOutputStream(saveFile)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
                if (makeHistory) {
                    val newCreation = ImageCreation(
                        algorithm = algorithm,
                        orientation = orientation,
                        version = "FREE",
                        uri = saveUri
                    )
                    imageRepository.addImageCreation(newCreation)
                }
                return true


            } else {
                requestPermissions(
                    requireActivity(), arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ), PERMISSION_GRANTED
                )
                return false

            }

        } catch (e: Exception) {
            e.printStackTrace()

            return false
        }
    }






}