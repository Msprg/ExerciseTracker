package com.msprg.exerciseTracker.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream

object ImageUtils {
    private const val MAX_RESOLUTION = 1024

    fun encodeImageToBase64(bitmap: Bitmap): String {
        val scaledBitmap = scaleDownBitmap(bitmap)
        val byteArrayOutputStream = ByteArrayOutputStream()
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun scaleDownBitmap(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height

        val scale = MAX_RESOLUTION.toFloat() / maxOf(width, height)

        if (scale >= 1f) {
            return bitmap
        }

        val scaledWidth = (width * scale).toInt()
        val scaledHeight = (height * scale).toInt()

        return Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true)
    }

    fun decodeBase64ToImage(encodedString: String): Bitmap {
        val decodedBytes = Base64.decode(encodedString, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }
}