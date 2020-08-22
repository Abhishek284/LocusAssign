package com.lib.locus.model

import LocusResponseItem
import android.graphics.Bitmap

class LocusResponse : ArrayList<LocusResponseItem>()

data class LocusDataModel(
    var dataResponse: LocusResponseItem? = null,
    var viewType: ViewType? = null,
    var imageBitmap: Bitmap? = null,
    var selectPosition: Int? = null,
    var isCommentChecked: Boolean?=null,
    var comment: String? = null
)

enum class ViewType {
    IMAGE, OPTIONS, COMMENT, SUBMIT
}