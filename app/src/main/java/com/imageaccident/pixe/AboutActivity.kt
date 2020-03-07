package com.imageaccident.pixe

import android.app.Activity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AboutActivity : AppCompatActivity() {
    private lateinit var aboutTextView : TextView
    val logTag = "ImageAccident.About"

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(logTag, "onCreate()")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
    }


}