package com.imageaccident.pixe.data

import androidx.lifecycle.ViewModel


class ImageCreationListViewModel(val imageRepository: ImageRepository) : ViewModel() {
    val listLiveData = imageRepository.getImageCreations()

    fun addImageCreation(imageCreation: ImageCreation) {
        imageRepository.addImageCreation(imageCreation)
    }
}