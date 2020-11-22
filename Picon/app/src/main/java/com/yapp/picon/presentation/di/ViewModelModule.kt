package com.yapp.picon.presentation.di

import com.yapp.picon.presentation.search.SearchViewModel
import com.yapp.picon.presentation.simplejoin.SimpleJoinViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { SimpleJoinViewModel(get()) }
    viewModel { SearchViewModel(get(), get(), get(), get()) }
}