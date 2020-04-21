package com.codigo.canigoout.di

import com.codigo.canigoout.common.LocalRepository
import com.codigo.canigoout.common.LocalRepositoryImpl
import io.reactivex.disposables.CompositeDisposable
import org.koin.dsl.module

val repositoryModule = module {
    factory { CompositeDisposable() }
    single<LocalRepository> { LocalRepositoryImpl(get()) }
}
