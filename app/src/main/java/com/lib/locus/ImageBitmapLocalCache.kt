package com.lib.locus

import android.graphics.Bitmap

class ImageBitmapLocalCache {
    var imageBitmap: Bitmap? = null

    companion object {
        private var instance: ImageBitmapLocalCache? = null

        fun getInstance(): ImageBitmapLocalCache {
            if (instance == null) {
                instance = ImageBitmapLocalCache()
            }
            return instance as ImageBitmapLocalCache
        }
    }

}