package com.students.portfolio

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import java.io.File
import java.io.FileOutputStream

object FileUtil {
    fun saveTextToDownloads(ctx: Context, filename: String, text: String): File? {
        val dir = ctx.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) ?: ctx.filesDir
        val file = File(dir, filename)
        file.writeText(text)
        return file
    }

    fun saveBitmapToCache(ctx: Context, bitmap: Bitmap?): Uri? {
        if (bitmap == null) return null
        val file = File(ctx.cacheDir, "photo_${System.currentTimeMillis()}.png")
        val fos = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos)
        fos.flush(); fos.close()
        return Uri.fromFile(file)
    }
}
