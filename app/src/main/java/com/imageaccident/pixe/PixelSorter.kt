package com.imageaccident.pixe

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import java.io.InputStream

private const val logTag = "ImageAccident.PixelSort"

class PixelSorter(private val algorithm: String, private val orientation: String, private val imageUri: Uri, private val context: Context){
    private lateinit var bitmapOptions: BitmapFactory.Options
    private lateinit var inputStream: InputStream

    private var originalWidth = 0
    private var originalHeight = 0
    private var originalType: String? = ""

    fun generateImage(): Bitmap{
        Log.d(logTag, "uri: ${imageUri.path}")
        readDimensionsAndType()
        var scaled_bitmap: Bitmap = decodeSampleBitmap(100, 100)
        return scaled_bitmap
    }

    private fun readDimensionsAndType(){
        bitmapOptions = BitmapFactory.Options().apply{
            //avoids memory allocation, returning null for the bitmap object,
            //but setting outWidth, outHeight, and outMimeType
            //read dimensions of image prior to construction (and memory allocation)
            inJustDecodeBounds = true
        }
        inputStream = context.contentResolver.openInputStream(imageUri)!!
        //Log.d(logTag, "input stream: ${inputStream.toString()}")
        BitmapFactory.decodeStream(inputStream, null, bitmapOptions)
        originalHeight = bitmapOptions.outHeight
        originalWidth = bitmapOptions.outWidth
        originalType = bitmapOptions.outMimeType
        Log.d(logTag, "width: ${originalWidth}, height: ${originalHeight}")
    }

    //load a scaled down version into memory
    private fun decodeSampleBitmap(reqWidth: Int, reqHeight: Int): Bitmap{
        bitmapOptions.outMimeType = Bitmap.Config.ARGB_8888.toString()
        bitmapOptions.inJustDecodeBounds = true
        inputStream = context.contentResolver.openInputStream(imageUri)!!
        val testBitmap = BitmapFactory.decodeStream(inputStream, null, bitmapOptions)
        if(testBitmap == null){
            Log.d(logTag, "first is NULLLLL")
        }
        val inSampleSize = calculateInSampleSize(reqWidth, reqHeight)
        bitmapOptions.inJustDecodeBounds = false
        bitmapOptions.inSampleSize = inSampleSize
        inputStream = context.contentResolver.openInputStream(imageUri)!!
        val finalBitmap = BitmapFactory.decodeStream(inputStream, null, bitmapOptions)
        //val scaledAgain = Bitmap.createScaledBitmap(finalBitmap!!, originalWidth, originalHeight, false)
        return finalBitmap!!
        //return scaledAgain
    }

    //calculate a sample size value that is a power of two based on a target width and height
    private fun calculateInSampleSize(reqWidth: Int, reqHeight: Int): Int{
        // Raw height and width of image
        val (height: Int, width: Int) = bitmapOptions.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {

            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }
}