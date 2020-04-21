package com.codigo.canigoout.common

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CanIGoOutSchedulerImpl : CanIGoOutScheduler {
    override fun mainThread(): Scheduler = AndroidSchedulers.mainThread()
    override fun io() = Schedulers.io()
}
