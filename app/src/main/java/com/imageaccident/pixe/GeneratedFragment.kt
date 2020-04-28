package com.imageaccident.pixe

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment.getExternalStorageDirectory
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import java.lang.Exception
import java.util.*
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED


class GeneratedFragment : Fragment() {
    private var logTag = "ImageAccident.GenFrag"
    private lateinit var imageView : ImageView
    private lateinit var resetButton : Button
    private lateinit var shareButton : Button
    private lateinit var saveButton: Button
    private var imagePath: String? = null

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
                Toast.makeText(requireContext(), "Saved successfully! $imagePath", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(requireContext(), "Error saving, try again.", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun sendImage() {
        if(imagePath == null) {
            Toast.makeText(requireContext(), "You must save the image before sending!", Toast.LENGTH_SHORT).show()
            return
        }
        try {
            val sendPic = Intent(Intent.ACTION_SEND)
            val path = "$imagePath"
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {
                Log.d(logTag, "HERE")
                sendPic.putExtra(Intent.EXTRA_STREAM, Uri.parse(path))
                sendPic.setType("image/jpg")
                Log.d(logTag, "THERE")
                startActivity(sendPic)


            } else {
                 requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
                     Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSION_GRANTED)

            }

        }catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Error sending image", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveImage(bitmap: Bitmap) : Boolean {
        val num = Random().nextInt(99999)
        val imageName = "img$num.jpg"
        val path = requireContext().getExternalFilesDir("image/*")
        imagePath = "$path/DCIM/$imageName"

        try {
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_GRANTED) {
                MediaStore.Images.Media.insertImage(
                    requireActivity().contentResolver,
                    bitmap,
                    imageName,
                    "")

            } else {
                requestPermissions(requireActivity(), arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_GRANTED)

            }


            return true
        }catch (e: Exception) {
            e.printStackTrace()
            return false
        }


//        val root = Environment.getExternalStorageDirectory().toString()
//        val saveDir = File(root + "/pixe_photos/")
//        saveDir.mkdirs()
//

//
//        val file = File(saveDir, filename)
//        if (file.exists()) {
//            file.delete()
//        }
//
//        try {
//            val output = FileOutputStream(file)
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output)
//            imageName = file.absolutePath
//
//            output.flush()
//            output.close()
//            return true
//        }
//        catch (e: Exception) {
//            e.printStackTrace()
//            Log.d(logTag, e.stackTrace.toString())
//            return false
//        }
    }






}