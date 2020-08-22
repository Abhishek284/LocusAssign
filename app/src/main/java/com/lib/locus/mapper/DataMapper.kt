package com.lib.locus.mapper

import android.content.Context
import com.google.gson.Gson
import com.lib.locus.model.LocusDataModel
import com.lib.locus.model.LocusResponse
import com.lib.locus.model.ViewType
import java.io.InputStream


class DataMapper(private val context: Context) {

    fun convertJsonToDataClass(fileName: String): MutableList<LocusDataModel> {
        return getResponseFromJson(fileName)
    }


    private fun getResponseFromJson(fileName: String): MutableList<LocusDataModel> {
        val isStream: InputStream = context.assets.open(fileName)
        val size: Int = isStream.available()
        val buffer = ByteArray(size)
        isStream.read(buffer)
        isStream.close()
        val gson = Gson()
        val locusDataResposne: LocusResponse =
            gson.fromJson(String(buffer), LocusResponse::class.java)
        val locusDataModelList = mutableListOf<LocusDataModel>()
        locusDataResposne.forEach {
            val locusDataModel = LocusDataModel()
            if (it.type == "PHOTO") {
                locusDataModel.viewType = ViewType.IMAGE
            } else if (it.type == "SINGLE_CHOICE") {
                locusDataModel.viewType = ViewType.OPTIONS
            } else if (it.type == "COMMENT") {
                locusDataModel.viewType = ViewType.COMMENT
            }
            locusDataModel.dataResponse = it
            locusDataModelList.add(locusDataModel)
        }
        if(locusDataModelList.isNotEmpty()){
            locusDataModelList.add(LocusDataModel(viewType = ViewType.SUBMIT))
        }
        return locusDataModelList
    }
}