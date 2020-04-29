package com.imageaccident.pixe

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color.*
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
        val scaledBitmap: Bitmap = decodeSampleBitmap(100, 100)
        val sortedBitmap = getSortedBitmap(scaledBitmap)
        return sortedBitmap
    }

    //pixel sorting happends here
    private fun getSortedBitmap(bitmap: Bitmap): Bitmap{
        val width = bitmap.width
        val height = bitmap.height
        //get the pixels and store in an array
        var pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)

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
        if(orientation == "Horizontal"){
            sortedPixels = sortHorizontal(pixelMap, width, height)
        }
        else if(orientation == "Vertical"){

        }

        return Bitmap.createBitmap(sortedPixels, width, height, Bitmap.Config.ARGB_8888)
    }

    private fun sortHorizontal(pixelMap: ArrayList<PixelMap>, width: Int, height: Int): IntArray{
        //get a row of pixels, then sort that row
        //do this for all rows
        var sorted = ArrayList<PixelMap>()
        var offset = 0
        for(i in 0..height-1){//go through all rows
            val temp = ArrayList<PixelMap>()
            for(j in 0..width-1){ //get an individual row
                temp.add(pixelMap[i + offset])
            }
            var sortedList = temp.sortedWith(compareBy({it.colorPixel})) //sort based on color
            sorted.addAll(sortedList)
            offset += width
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