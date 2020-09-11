package com.lib.locus.viewholder

import android.view.View
import com.lib.locus.adapter.BaseViewHolder
import com.lib.locus.adapter.DataAdapter
import com.lib.locus.model.LocusDataModel
import kotlinx.android.synthetic.main.item_comment.view.*

class CommentViewHolder(view: View, private val customTextWatcher: DataAdapter.CustomTextWatcher) :
    BaseViewHolder(view) {

    fun bindData(position: Int, locusDataModel: LocusDataModel?) {
        itemView.run {
            commentView.removeTextChangedListener(customTextWatcher)
            locusDataModel?.dataResponse?.title?.let {
                titleComment.text = it
            }
            switchComment.setOnCheckedChangeListener(null)
            if (locusDataModel?.isCommentChecked == true) {
                commentView.visibility = View.VISIBLE
                commentView.setText(locusDataModel.comment)
                switchComment.isChecked = true
            } else {
                commentView.visibility = View.GONE
                commentView.setText("")
                switchComment.isChecked = false
            }
            switchComment.setOnCheckedChangeListener { _, boolean ->
                if (boolean) {
                    commentView.visibility = View.VISIBLE
                    locusDataModel?.isCommentChecked = true
                } else {
                    commentView.visibility = View.GONE
                    locusDataModel?.isCommentChecked = false
                }
            }
            locusDataModel?.comment?.let {
                commentView.setText(it)
            }
            customTextWatcher.position = position
            commentView.addTextChangedListener(customTextWatcher)
        }
    }
}
