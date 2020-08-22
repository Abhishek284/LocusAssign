package com.lib.locus.adapter

import android.graphics.Bitmap
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.lib.locus.R
import com.lib.locus.listener.OnItemClickListener
import com.lib.locus.model.LocusDataModel
import com.lib.locus.model.ViewType
import kotlinx.android.synthetic.main.item_camera.view.*
import kotlinx.android.synthetic.main.item_choice.view.*
import kotlinx.android.synthetic.main.item_comment.view.*
import kotlinx.android.synthetic.main.item_submit.view.*


class DataAdapter(private val onItemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<BaseViewHolder>() {

    var locusDataModel: MutableList<LocusDataModel>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        when (viewType) {
            ViewType.IMAGE.ordinal -> {
                return PhotoDataViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_camera,
                        parent,
                        false
                    )
                )
            }
            ViewType.OPTIONS.ordinal -> {
                return ChoiceDataViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_choice,
                        parent,
                        false
                    )
                )
            }

            ViewType.COMMENT.ordinal -> {
                return CommentViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_comment,
                        parent,
                        false
                    )
                )
            }
            else -> {
                return BlankViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_blank,
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return locusDataModel?.get(position)?.viewType?.ordinal ?: ViewType.IMAGE.ordinal
    }

    override fun getItemCount(): Int {
        return locusDataModel?.size ?: 0
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        when (holder) {
            is PhotoDataViewHolder -> {
                holder.bindData(position)
            }
            is ChoiceDataViewHolder -> {
                holder.bindData(position)
            }
            is CommentViewHolder -> {
                holder.bindData(position)
            }
        }
    }

    fun setImageBitmap(position: Int, imageBitmap: Bitmap?) {
        locusDataModel?.get(position)?.imageBitmap = imageBitmap
        notifyItemChanged(position)
    }

    fun getDataModel(): MutableList<LocusDataModel>? {
        return locusDataModel

    }

    inner class PhotoDataViewHolder(view: View) : BaseViewHolder(view) {

        fun bindData(position: Int) {
            itemView.titlePhoto.text = locusDataModel?.get(position)?.dataResponse?.title
            locusDataModel?.get(position)?.imageBitmap?.let {
                itemView.captureImageView.setImageBitmap(it)
                itemView.deleteImage.visibility = View.VISIBLE
            } ?: let {
                itemView.captureImageView.setImageBitmap(null)
                itemView.deleteImage.visibility = View.GONE
            }
            itemView.captureImageView.setOnClickListener {
                onItemClickListener.onItemClicked(
                    position,
                    locusDataModel?.get(position)?.imageBitmap
                )
            }
            itemView.deleteImage.setOnClickListener {
                onItemClickListener.onItemDeleted(position)
            }
        }
    }

    inner class ChoiceDataViewHolder(view: View) : BaseViewHolder(view) {

        fun bindData(position: Int) {
            itemView.titleChoice.text =
                locusDataModel?.get(position)?.dataResponse?.title
            itemView.radioGroup.removeAllViews()
            locusDataModel?.get(position)?.dataResponse?.dataMap?.options?.forEach {
                val radioButton: RadioButton = RadioButton(itemView.context)
                radioButton.text = it
                itemView.radioGroup.addView(radioButton)
            }
            itemView.radioGroup.setOnCheckedChangeListener(null)
            locusDataModel?.get(position)?.selectPosition?.let {
                (itemView.radioGroup.getChildAt(it) as RadioButton).isChecked = true
            }
            itemView.radioGroup.setOnCheckedChangeListener { radioGroup, i ->
                    locusDataModel?.get(position)?.selectPosition =
                        radioGroup.indexOfChild(radioGroup.findViewById(i))
            }
        }

    }

    inner class CommentViewHolder(view: View) : BaseViewHolder(view) {

        fun bindData(position: Int) {
            locusDataModel?.get(position)?.dataResponse?.title?.let {
                itemView.titleComment.text = it
            }
            locusDataModel?.get(position)?.isCommentChecked?.let {
                if (it) {
                    itemView.commentView.visibility = View.VISIBLE
                } else {
                    itemView.commentView.visibility = View.GONE
                }
            }
            itemView.switchComment.setOnCheckedChangeListener { _, boolean ->
                if (boolean) {
                    itemView.commentView.visibility = View.VISIBLE
                    locusDataModel?.get(position)?.isCommentChecked = true
                } else {
                    itemView.commentView.visibility = View.GONE
                    locusDataModel?.get(position)?.isCommentChecked = false
                }
            }
            locusDataModel?.get(position)?.comment?.let {
                itemView.commentView.setText(it)
            }
            itemView.commentView.addTextChangedListener(object : TextWatcher{
                override fun afterTextChanged(p0: Editable?) {

                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    locusDataModel?.get(position)?.comment = p0.toString()
                }

            })
        }
    }

    inner class BlankViewHolder(view: View) : BaseViewHolder(view) {

    }
}