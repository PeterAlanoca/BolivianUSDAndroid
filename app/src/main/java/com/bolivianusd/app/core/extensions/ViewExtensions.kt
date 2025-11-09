package com.bolivianusd.app.core.extensions

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.net.Uri
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.graphics.createBitmap
import com.bolivianusd.app.R
import java.io.File
import java.io.FileOutputStream

fun View.gone() {
    this.visibility = View.GONE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.captureAsBitmap(
    context: Context,
    @ColorRes viewBackgroundColorRes: Int = R.color.martinique,
    cornerRadiusDp: Float = 16f,
    marginDp: Float = 16f,
    @ColorRes outerBackgroundColorRes: Int = R.color.cool_grey
): Uri? {
    return try {
        val density = context.resources.displayMetrics.density
        val marginPx = marginDp * density
        val cornerPx = cornerRadiusDp * density
        val bitmapWidth = (width + 2 * marginPx).toInt()
        val bitmapHeight = (height + 2 * marginPx).toInt()
        val bitmap = createBitmap(bitmapWidth, bitmapHeight)
        val canvas = Canvas(bitmap)
        canvas.drawColor(ContextCompat.getColor(context, outerBackgroundColorRes))
        val innerRect = RectF(
            marginPx,
            marginPx,
            marginPx + width,
            marginPx + height
        )
        val paintViewBg = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = ContextCompat.getColor(context, viewBackgroundColorRes)
        }
        canvas.drawRoundRect(innerRect, cornerPx, cornerPx, paintViewBg)

        canvas.save()

        val clipPath = Path().apply {
            addRoundRect(innerRect, cornerPx, cornerPx, Path.Direction.CW)
        }
        canvas.clipPath(clipPath)

        canvas.translate(marginPx, marginPx)
        draw(canvas)
        canvas.restore()

        val cachePath = File(context.cacheDir, "images")
        cachePath.mkdirs()
        val file = File(cachePath, "screenshot.png")
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }
        FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}