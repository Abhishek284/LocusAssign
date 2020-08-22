package com.lib.locus.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lib.locus.mapper.DataMapper
import com.lib.locus.model.LocusDataModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.schedulers.Schedulers

class MenuListViewModel(private val dataMapper: DataMapper) : ViewModel() {

    private val disposables = CompositeDisposable()

    private val locusDataModel = MutableLiveData<MutableList<LocusDataModel>>()
    val dataModelList: LiveData<MutableList<LocusDataModel>>
        get() = locusDataModel


    fun getProductDataList(fileName: String) {
        val disposable = Observable.fromCallable { dataMapper.convertJsonToDataClass(fileName) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(Consumer {
                locusDataModel.postValue(it)
            })
        disposables.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}