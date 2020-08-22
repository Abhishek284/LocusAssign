package com.lib.locus.listener

import android.graphics.Bitmap
import com.lib.locus.model.LocusDataModel

interface OnItemClickListener {

    fun onItemClicked(int: Int, imageBitmap: Bitmap?)

    fun onItemDeleted(int: Int)
}