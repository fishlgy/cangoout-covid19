package com.codigo.canigoout.common

import io.reactivex.Scheduler

interface CanIGoOutScheduler {
    fun mainThread(): Scheduler
    fun io(): Scheduler
}
