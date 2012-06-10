package net.surguy.android

import android.graphics.{Canvas, BitmapFactory, Bitmap}


/**
 * Android devices have a range of different screen sizes and aspect ratios. Displaying background images on them requires
 * either a range of images with different aspect ratios (hard work), or automatic scaling (generally doesn't look good).
 * Ideally, we'd have automatic scaling to different aspect ratios that looked good.
 *
 * "Seam carving" removes low-energy strips from an image. This doesn't always work well for Android image scaling,
 * because it's slow, and can strip out 'low energy' foreground leaving the 'high energy' background (e.g. see the
 * "Celtic Cross against sky" picture - the cross silhouette is massively distorted, since it's featureless).
 *
 * There is GPL Java seam carving code at http://www.semanticmetadata.net/2007/09/20/content-based-image-resizing-update-to-v2/
 *
 * Android buttons are "NinePatchDrawables" - images split into nine areas, that work like typical scalable web page button images.
 * That is, on scaling, the corners remain the same size, the edges scale either horizontally or vertically, and the centre image
 * scales both horizontally and vertically. This is convenient for button backgrounds, but not good for background images, since it's the
 * centre of the image that's typically most interesting and least stretchable.
 *
 * NinePatchDrawables are described at http://developer.android.com/guide/topics/graphics/2d-graphics.html
 *
 * ImageMagick has a number of resizing algorithms described at http://www.imagemagick.org/Usage/resize/#geometry
 *
 * A possible implementation is an inverse nine-patch-drawable - using a similar approach, but keeping the middle area the same
 * aspect ratio, and scaling the outside edges to pad the content to fit appropriately (perhaps using seam carving, if that can be
 * done fast enough)
 *
 * Galaxy Nexus images should by 720 x 1184 in portrait (720 x 1280 screen, 96px used by controls).
 *
 * @author Inigo Surguy
 */
class ImageResizer {

  def resizeImage(bmp: Bitmap, newWidth: Int, newHeight: Int, marginPercent: Float = 0.1F): Bitmap = {
    val originalWidth = bmp.getWidth
    val originalHeight = bmp.getHeight
    if (newWidth >= originalWidth && newHeight >= originalHeight) return bmp

    val xMargin: Int = (originalWidth * marginPercent).toInt
    val yMargin: Int = (originalHeight * marginPercent).toInt

    // Split into 9 separate pieces, using the specified margin size
    var topLeft = Bitmap.createBitmap(bmp, 0, 0, xMargin, yMargin)
    var topMiddle = Bitmap.createBitmap(bmp, xMargin, 0, originalWidth - (xMargin*2), yMargin)
    var topRight = Bitmap.createBitmap(bmp, originalWidth - xMargin, 0, xMargin, yMargin)
    var middleLeft = Bitmap.createBitmap(bmp, 0, yMargin, xMargin, originalHeight - (yMargin*2))
    var middleMiddle = Bitmap.createBitmap(bmp, xMargin, yMargin, originalWidth - (xMargin*2), originalHeight - (yMargin*2))
    var middleRight = Bitmap.createBitmap(bmp, originalWidth - xMargin, yMargin, xMargin, originalHeight - (yMargin*2))
    var bottomLeft = Bitmap.createBitmap(bmp, 0, originalHeight - yMargin, xMargin, yMargin)
    var bottomMiddle = Bitmap.createBitmap(bmp, xMargin, originalHeight - yMargin, originalWidth - (xMargin*2), yMargin)
    var bottomRight = Bitmap.createBitmap(bmp, originalWidth - xMargin, originalHeight - yMargin, xMargin, yMargin)

    // Resize the pieces, leaving the middle more-or-less the same size
    val newMiddleWidth: Int = math.max( ((newWidth - originalWidth) * 0.10F) + originalWidth, originalWidth ).toInt
    val newMiddleHeight: Int = math.max( ((newHeight - originalHeight) * 0.10F) + originalHeight, originalHeight ).toInt
    val newXMargin: Int = ((newWidth - newMiddleWidth) / 2).toInt
    val newYMargin: Int = ((newHeight - newMiddleHeight) / 2).toInt
    topLeft = Bitmap.createScaledBitmap(topLeft, newXMargin, newYMargin, false)
    topMiddle = Bitmap.createScaledBitmap(topMiddle, newMiddleWidth, newYMargin, false)
    topRight = Bitmap.createScaledBitmap(topRight, newXMargin, newYMargin, false)
    middleLeft = Bitmap.createScaledBitmap(middleLeft, newXMargin, newMiddleHeight, false)
    middleMiddle = Bitmap.createScaledBitmap(middleMiddle, newMiddleWidth, newMiddleHeight, false)
    middleRight = Bitmap.createScaledBitmap(middleRight, newXMargin, newMiddleHeight, false)
    bottomLeft = Bitmap.createScaledBitmap(bottomLeft, newXMargin, newYMargin, false)
    bottomMiddle = Bitmap.createScaledBitmap(bottomMiddle, newMiddleWidth, newYMargin, false)
    bottomRight = Bitmap.createScaledBitmap(bottomRight, newXMargin, newYMargin, false)

    // Join the 9 disparate pieces back together again
    val destination = Bitmap.createBitmap(newWidth, newHeight, bmp.getConfig)
    val canvas = new Canvas(destination)
    canvas.drawBitmap(topLeft, 0, 0, null)
    canvas.drawBitmap(topMiddle, newXMargin, 0, null)
    canvas.drawBitmap(topRight, newWidth - newXMargin, 0, null)
    canvas.drawBitmap(middleLeft, 0, newYMargin, null)
    canvas.drawBitmap(middleMiddle, newXMargin, newYMargin, null)
    canvas.drawBitmap(middleRight, newWidth - newXMargin, newYMargin, null)
    canvas.drawBitmap(bottomLeft, 0, newHeight-newYMargin, null)
    canvas.drawBitmap(bottomMiddle, newXMargin, newHeight-newYMargin, null)
    canvas.drawBitmap(bottomRight, newWidth - newXMargin, newHeight-newYMargin, null)

    destination
  }

}
