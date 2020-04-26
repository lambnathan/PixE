package com.imageaccident.pixe.data

import android.content.Context
import androidx.lifecycle.LiveData
import java.lang.IllegalStateException
import java.util.*
import java.util.concurrent.Executors

class ImageRepository (private val imageDAO: ImageDAO) {
    private val executor = Executors.newSingleThreadExecutor()

    fun getImageCreations(): LiveData<List<ImageCreation>> = imageDAO.getImageCreations()

    fun getImageCreation(id: UUID): LiveData<ImageCreation?> = imageDAO.getImageCreations(id)

    fun addImageCreation(image: ImageCreation) {
        executor.execute{
            imageDAO.addImageCreation(image)
        }
    }

    companion object {
        private var instance : ImageRepository? = null

        fun getInstance(context: Context) : ImageRepository {
            return instance ?: let {
                if (instance == null) {
                    val db = ImageDatabase.getInstance(context)
                    instance = ImageRepository(db.imageDao())
                }
                return get()
            }
        }

        fun get(): ImageRepository {
            return instance ?:
                    throw IllegalStateException("ImageRepository must be initialized")
        }
    }
}