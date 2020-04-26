package com.imageaccident.pixe

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import java.io.File
import java.util.*


class GeneratedFragment : Fragment() {
    private var logTag = "ImageAccident.GenFrag"
    private lateinit var imageView : ImageView
    private lateinit var resetButton : Button
    private lateinit var shareButton : Button
    private lateinit var saveButton: Button

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
        shareButton.setOnClickListener { Toast.makeText(requireContext(), "Share options here!", Toast.LENGTH_SHORT).show() }
        saveButton.setOnClickListener { Toast.makeText(requireContext(), "Save options here!", Toast.LENGTH_SHORT).show() }

        return view
    }

    private fun saveImage(bitmap: Bitmap) {
        val root = Environment.getExternalStorageDirectory().toString()
        val saveDir = File(root + "/pixe_photos")
        saveDir.mkdirs()
        val gen = Random()
        var temp = gen.nextInt()
    }

}