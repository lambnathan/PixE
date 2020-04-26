package com.imageaccident.pixe.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class ImageCreation(@PrimaryKey val id: UUID = UUID.randomUUID(),
                         var algorithm: String = "",
                         var orientation: String = "",
                         var version: String = "FREE",
                         var date: Date = Date())