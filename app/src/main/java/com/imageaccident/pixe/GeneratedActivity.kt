package com.imageaccident.pixe

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class GeneratedActivity : AppCompatActivity() {
    private lateinit var imageView : ImageView
    private lateinit var resetButton : Button
    private lateinit var shareButton : Button
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generated)

        imageView = findViewById(R.id.generated_image_view)
        resetButton = findViewById(R.id.reset_button)
        shareButton = findViewById(R.id.share_button)
        saveButton = findViewById(R.id.save_button)

        resetButton.setOnClickListener {
            startActivity(Intent(baseContext, MainActivity::class.java))
        }
        shareButton.setOnClickListener { Toast.makeText(this, "Share options here!", Toast.LENGTH_SHORT).show() }
        saveButton.setOnClickListener { Toast.makeText(this, "Save options here!", Toast.LENGTH_SHORT).show() }
    }

}