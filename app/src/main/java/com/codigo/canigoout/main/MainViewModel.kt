package com.codigo.canigoout.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.codigo.canigoout.common.CanIGoOutScheduler
import com.codigo.canigoout.common.LocalRepository
import com.codigo.canigoout.common.Outcome
import io.reactivex.disposables.CompositeDisposable


class MainViewModel(
    private val localRepository: LocalRepository,
    private val scheduler: CanIGoOutScheduler,
    private val compositeDisposable: CompositeDisposable
) : ViewModel() {

    val checkExistRecordOutcome = MutableLiveData<Outcome<String>>()
    val checkNricOutcome = MutableLiveData<Outcome<Boolean>>()

    fun hasNricExists() {
        checkExistRecordOutcome.postValue(Outcome.success(localRepository.getNric()))
    }

    fun validateNRIC(nric: String) {
        checkNricOutcome.postValue(Outcome.loading(true))

        if(nric.isEmpty()) {
            checkNricOutcome.postValue(Outcome.loading(false))
            checkNricOutcome.postValue(Outcome.failure(
                Throwable("NRIC/Fin cannot be empty.")))
        } else if(nric.length != 4) {
            checkNricOutcome.postValue(Outcome.loading(false))
            checkNricOutcome.postValue(Outcome.failure(
                Throwable("Invalid NRIC/ FIN. Please enter last 4 digit (SXXX1234X)")))
        } else if(nric.last().isDigit()) {
            checkNricOutcome.postValue(Outcome.loading(false))
            checkNricOutcome.postValue(Outcome.Success(true))
            localRepository.saveNric(nric)
            localRepository.saveLastCharacter(nric.last().toInt())
        } else {
            checkNricOutcome.postValue(Outcome.loading(false))
            checkNricOutcome.postValue(Outcome.failure(
                Throwable("Invalid NRIC/ FIN.")))
        }
    }

}