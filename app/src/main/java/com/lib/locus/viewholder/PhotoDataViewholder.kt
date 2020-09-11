package com.lib.locus.viewholder

import android.view.View
import com.lib.locus.adapter.BaseViewHolder
import com.lib.locus.listener.OnItemClickListener
import com.lib.locus.model.LocusDataModel
import kotlinx.android.synthetic.main.item_camera.view.*

class PhotoDataViewHolder(view: View) : BaseViewHolder(view) {

    fun bindData(
        position: Int,
        locusDataModel: LocusDataModel?,
        onItemClickListener: OnItemClickListener
    ) {
        itemView.run {
            titlePhoto.text = locusDataModel?.dataResponse?.title
            locusDataModel?.imageBitmap?.let {
                captureImageView.setImageBitmap(it)
                deleteImage.visibility = View.VISIBLE
            } ?: let {
                captureImageView.setImageBitmap(null)
                deleteImage.visibility = View.GONE
            }
            captureImageView.setOnClickListener {
                onItemClickListener.onItemClicked(
                    position,
                    locusDataModel?.imageBitmap
                )
            }
            deleteImage.setOnClickListener {
                onItemClickListener.onItemDeleted(position)
            }
        }

    }
}
