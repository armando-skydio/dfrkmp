@file:Suppress("FunctionName")

package com.skydio.ui.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import java.io.File

/**
 * A general-purpose class for providing images to composable functions, regardless of their source.
 * See the factory functions at the bottom of this file for usage.
 */
sealed class ImageSource {

    abstract val contentDescription: String

    @Composable
    abstract fun asPainter(): Painter

    object Empty : ImageSource() {

        override val contentDescription: String = ""

        @Composable
        override fun asPainter(): Painter = ColorPainter(color = Color.Transparent)
    }

    internal data class Bitmap(
        val imageBitmap: ImageBitmap,
        override val contentDescription: String
    ) : ImageSource() {
        @Composable
        override fun asPainter(): Painter = BitmapPainter(imageBitmap)
    }

    internal data class Resource(
        @DrawableRes val resId: Int,
        override val contentDescription: String
    ) : ImageSource() {
        @Composable
        override fun asPainter(): Painter = painterResource(id = resId)
    }
}

fun ImageSource?.orEmpty() = this ?: ImageSource.Empty

fun ImageSource?.isNullOrEmpty() = this == ImageSource.Empty

// MARK: Factory Functions

fun DrawableImageSource(
    @DrawableRes resId: Int,
    contentDescription: String? = null
): ImageSource = ImageSource.Resource(resId, contentDescription.orEmpty())

fun BitmapImageSource(
    bitmap: ImageBitmap,
    contentDescription: String? = null
): ImageSource = ImageSource.Bitmap(bitmap, contentDescription.orEmpty())

fun BitmapImageSource(
    bitmap: Bitmap,
    contentDescription: String? = null
): ImageSource = BitmapImageSource(bitmap.asImageBitmap(), contentDescription)

fun FileImageSource(
    file: File,
    opts: BitmapFactory.Options? = null,
    contentDescription: String? = null
): ImageSource? = BitmapFactory.decodeFile(file.absolutePath, opts)?.let {
    BitmapImageSource(it, contentDescription)
}

