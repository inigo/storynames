package net.surguy.android

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
 * @todo There is no code here, just discussion
 *
 * @author Inigo Surguy
 */
class ImageResizer {

}
