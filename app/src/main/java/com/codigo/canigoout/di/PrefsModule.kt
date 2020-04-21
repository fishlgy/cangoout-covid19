package com.codigo.canigoout.di

import android.content.Context
import android.content.SharedPreferences
import com.codigo.canigoout.common.CanIGoOutPreference
import com.codigo.canigoout.common.CanIGoOutPreferenceImpl
import org.koin.dsl.module


val prefsModule = module {

    single { createSharedPreferences(get()) }

    single<CanIGoOutPreference> { CanIGoOutPreferenceImpl(get()) }
}

private fun createSharedPreferences(context: Context): SharedPreferences {
    return context.getSharedPreferences("covid-app", Context.MODE_PRIVATE)
}


