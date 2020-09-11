package com.lib.locus.viewholder

import android.view.View
import android.widget.RadioButton
import com.lib.locus.adapter.BaseViewHolder
import com.lib.locus.model.LocusDataModel
import kotlinx.android.synthetic.main.item_choice.view.*

 class ChoiceDataViewHolder(view: View) : BaseViewHolder(view) {

    fun bindData(locusDataModel: LocusDataModel?) {
        itemView.run {
            titleChoice.text =
                locusDataModel?.dataResponse?.title
            radioGroup.removeAllViews()
            locusDataModel?.dataResponse?.dataMap?.options?.forEach {
                val radioButton: RadioButton = RadioButton(context)
                radioButton.text = it
                radioGroup.addView(radioButton)
            }
            radioGroup.setOnCheckedChangeListener(null)
            locusDataModel?.selectPosition?.let {
                (radioGroup.getChildAt(it) as RadioButton).isChecked = true
            }
            radioGroup.setOnCheckedChangeListener { radioGroup, i ->
                locusDataModel?.selectPosition =
                    radioGroup.indexOfChild(radioGroup.findViewById(i))
            }
        }
    }

}
