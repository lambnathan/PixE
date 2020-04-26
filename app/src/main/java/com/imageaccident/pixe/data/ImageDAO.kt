package com.imageaccident.pixe.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import java.util.*

@Dao
interface ImageDAO {

    @Query("SELECT * FROM ImageCreation")
    fun getImageCreations(): LiveData<List<ImageCreation>>

    @Query("SELECT * FROM ImageCreation WHERE id=(:id)")
    fun getImageCreations(id: UUID): LiveData<ImageCreation?>

    @Insert
    fun addImageCreation(image: ImageCreation)
}