package com.codigo.canigoout.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.codigo.canigoout.common.CanIGoOutScheduler
import com.codigo.canigoout.common.LocalRepository
import com.codigo.canigoout.common.Outcome
import io.reactivex.disposables.CompositeDisposable


class SplashViewModel(
    private val localRepository: LocalRepository
) : ViewModel() {

    val checkExistRecordOutcome = MutableLiveData<Outcome<Boolean>>()

    fun hasNricExists() {
        checkExistRecordOutcome.postValue(Outcome.loading(true))

        if(localRepository.getNric().isNotEmpty()) {
            checkExistRecordOutcome.postValue(Outcome.loading(false))
            checkExistRecordOutcome.postValue(Outcome.success(true))
        } else {
            checkExistRecordOutcome.postValue(Outcome.loading(false))
            checkExistRecordOutcome.postValue(Outcome.success(false))
        }
    }

}