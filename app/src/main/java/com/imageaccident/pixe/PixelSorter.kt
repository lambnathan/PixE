package com.imageaccident.pixe

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color.*
import android.graphics.Matrix
import android.net.Uri
import android.util.DisplayMetrics
import android.util.Log
import java.io.InputStream

private const val logTag = "ImageAccident.PixelSort"

class PixelSorter(private val algorithm: String, private val orientation: String, private val imageUri: Uri, private val context: Context, private val activity: Activity){
    private lateinit var bitmapOptions: BitmapFactory.Options
    private lateinit var inputStream: InputStream

    private var originalWidth = 0
    private var originalHeight = 0
    private var originalType: String? = ""

    private var generateWidth: Int = 0
    private var generateHeight: Int = 0

    fun generateImage(): Bitmap{
        Log.d(logTag, "uri: ${imageUri.path}")
        readDimensionsAndType()
        getRequiredWidthAndHeight()
        var scaledBitmap: Bitmap = decodeSampleBitmap(generateWidth, generateHeight)
        val sortedBitmap = getSortedBitmap(scaledBitmap)
        return sortedBitmap
    }

    private fun getRequiredWidthAndHeight(){
        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getRealMetrics(displayMetrics)
        generateWidth = displayMetrics.widthPixels //image fills width
        generateHeight = displayMetrics.heightPixels / 3 //imageview fills roughly a third of the screen's height
        Log.d(logTag, "to display width: ${generateWidth}, to display height: ${generateHeight}")
    }

    //pixel sorting happends here
    private fun getSortedBitmap(bitmap: Bitmap): Bitmap{
        var width = bitmap.width
        var height = bitmap.height
        //get the pixels and store in an array
        var pixels = IntArray(width * height)
        if(orientation == "Vertical"){
            val matrix = Matrix()
            matrix.postRotate(90f)
            val tempBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true)
            //since we rotated, swap width and height (only do this for vertical, because we rotated)
            val temp = width
            width = height
            height = temp
            tempBitmap.getPixels(pixels, 0, width,0, 0, width, height)
        }
        else{
            bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
        }

        //depending on what color they want to sort by, create another array only with that color value
        //var colorPixels = IntArray(pixels.size)
        val pixelMap = ArrayList<PixelMap>()
        for(i in 0..pixels.size - 1){
            val pixel = pixels[i]
            if(algorithm == "red"){
                //colorPixels[i] = red(pixel)
                pixelMap.add(PixelMap(pixel, red(pixel)))
            }
            else if(algorithm == "blue"){
                pixelMap.add(PixelMap(pixel, blue(pixel)))
            }
            else if(algorithm == "green"){
                pixelMap.add(PixelMap(pixel, green(pixel)))
            }
        }

        var sortedPixels = IntArray(pixels.size)
        sortedPixels = getSortedPixels(pixelMap, width, height)

        if(orientation == "Vertical"){//rotate the pixels back to original
            val matrix = Matrix()
            matrix.postRotate(-90f)
            val tempBitmap = Bitmap.createBitmap(sortedPixels, width, height, Bitmap.Config.ARGB_8888)
            return Bitmap.createBitmap(tempBitmap, 0, 0, width, height, matrix, true)
        }
        else{
            return Bitmap.createBitmap(sortedPixels, width, height, Bitmap.Config.ARGB_8888)
        }

    }

    private fun getSortedPixels(pixelMap: ArrayList<PixelMap>, width: Int, height: Int): IntArray{
        //get a row of pixels, then sort that row
        //do this for all rows
        var sorted = ArrayList<PixelMap>()
        var index = 0
        for(i in 0..height-1){//go through all rows
            val row = ArrayList<PixelMap>()
            for(j in 0..width-1){
                index = i * width + j
                row.add(pixelMap[index])
            }
            val sortedRow = row.sortedWith(compareBy({it.colorPixel}))
            sorted.addAll(sortedRow)
        }

        //after going through all the mapped pixels, just isolate orignal pixels
        //that are now sorted and return
        val sortedOriginalPixels = IntArray(width*height)
        for(i in 0..sorted.size - 1){
            sortedOriginalPixels[i] = sorted[i].originalPixel
        }
        return sortedOriginalPixels
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
        Log.d(logTag, "original pic width: ${originalWidth}, original pic height: ${originalHeight}")
    }

    //load a scaled down version into memory
    private fun decodeSampleBitmap(reqWidth: Int, reqHeight: Int): Bitmap{
        bitmapOptions.outMimeType = Bitmap.Config.ARGB_8888.toString()
        bitmapOptions.inJustDecodeBounds = true
        inputStream = context.contentResolver.openInputStream(imageUri)!!
        val testBitmap = BitmapFactory.decodeStream(inputStream, null, bitmapOptions)
        if(testBitmap == null){
            //Log.d(logTag, "first is NULLLLL")
        }
        val inSampleSize = calculateInSampleSize(reqWidth, reqHeight)
        bitmapOptions.inJustDecodeBounds = false
        bitmapOptions.inSampleSize = inSampleSize
        inputStream = context.contentResolver.openInputStream(imageUri)!!
        val finalBitmap = BitmapFactory.decodeStream(inputStream, null, bitmapOptions)
        Log.d(logTag, "decode width: ${finalBitmap?.width}, decode height: ${finalBitmap?.height}")
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