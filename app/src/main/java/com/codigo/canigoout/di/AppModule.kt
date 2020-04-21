package com.codigo.canigoout.di

import com.codigo.canigoout.common.CanIGoOutScheduler
import com.codigo.canigoout.common.CanIGoOutSchedulerImpl
import com.codigo.canigoout.location.LocationViewModel
import com.codigo.canigoout.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single<CanIGoOutScheduler> { CanIGoOutSchedulerImpl() }

    viewModel { MainViewModel(get(), get(), get()) }
    viewModel { LocationViewModel(get(), get(), get()) }
}
