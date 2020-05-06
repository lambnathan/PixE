package com.imageaccident.pixe.data

import android.net.Uri
import androidx.room.TypeConverter
import java.util.*



class ImageTypeConverters {

    @TypeConverter
    fun toUUID(uuid: String?): UUID? {
        return UUID.fromString(uuid)
    }

    @TypeConverter
    fun fromUUID(uuid: UUID?): String? {
        return uuid?.toString()
    }

    @TypeConverter
    fun fromUri(uri: Uri?): String? {
        return uri?.toString()
    }

    @TypeConverter
    fun toUri(str: String?): Uri? {
        return Uri.parse(str)
    }

}