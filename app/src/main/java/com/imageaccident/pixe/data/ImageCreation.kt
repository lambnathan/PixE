package com.imageaccident.pixe.data

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*

@Entity
data class ImageCreation(@PrimaryKey val id: UUID = UUID.randomUUID(),
                         var algorithm: String = "",
                         var orientation: String = "",
                         var version: String = "FREE",
                         var date: String = "no date",
                         var uri: Uri) {

    init {
        val dateFormat = SimpleDateFormat("yyy-mm-dd HH:mm")
        date = dateFormat.format(Date())
    }
}