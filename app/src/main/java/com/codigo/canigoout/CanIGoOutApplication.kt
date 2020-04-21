package com.codigo.canigoout

import android.app.Application
import com.codigo.canigoout.di.appModule
import com.codigo.canigoout.di.prefsModule
import com.codigo.canigoout.di.repositoryModule
import com.jakewharton.threetenabp.AndroidThreeTen
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber


class CanIGoOutApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree()) // Logs are only shown in DEBUG mode
        }
        AndroidThreeTen.init(this)

        startKoin {
            androidLogger()
            androidContext(this@CanIGoOutApplication)
            modules(
                listOf(
                    appModule,
                    prefsModule,
                    repositoryModule
                ))
        }
    }
}
