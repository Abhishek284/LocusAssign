package com.lib.locus.module

import com.lib.locus.mapper.DataMapper
import com.lib.locus.viewmodel.MenuListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    factory { DataMapper(get()) }
    viewModel { MenuListViewModel(get()) }
}