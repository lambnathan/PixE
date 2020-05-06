package com.imageaccident.pixe.data

import android.content.Context
import androidx.room.*

private const val DB_NAME = "image-database"

@Database(entities = [ImageCreation::class], version=3)
@TypeConverters(ImageTypeConverters::class)
abstract class ImageDatabase : RoomDatabase() {

    abstract fun imageDao(): ImageDAO

    companion object {
        private var instance : ImageDatabase? = null

        fun getInstance(context: Context) : ImageDatabase {
            return instance ?: Room.databaseBuilder(
                context,
                ImageDatabase::class.java,
                DB_NAME)
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}