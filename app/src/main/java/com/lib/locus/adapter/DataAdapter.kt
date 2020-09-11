package com.lib.locus.adapter

import android.graphics.Bitmap
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lib.locus.R
import com.lib.locus.listener.OnItemClickListener
import com.lib.locus.model.LocusDataModel
import com.lib.locus.model.ViewType
import com.lib.locus.viewholder.ChoiceDataViewHolder
import com.lib.locus.viewholder.CommentViewHolder
import com.lib.locus.viewholder.PhotoDataViewHolder
import kotlinx.android.synthetic.main.item_camera.view.*
import kotlinx.android.synthetic.main.item_comment.view.*


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
                    ),
                    CustomTextWatcher()
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
                holder.bindData(position, locusDataModel?.get(position),onItemClickListener)
            }
            is ChoiceDataViewHolder -> {
                holder.bindData(locusDataModel?.get(position))
            }
            is CommentViewHolder -> {
                holder.bindData(position, locusDataModel?.get(position))
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

    inner class BlankViewHolder(view: View) : BaseViewHolder(view) {
    }

    inner class CustomTextWatcher : TextWatcher {
        var position: Int = 0

        override fun afterTextChanged(p0: Editable?) {
            // Not required
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            // Not required
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            locusDataModel?.get(position)?.comment = p0.toString()
        }

    }
}