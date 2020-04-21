package com.codigo.canigoout.main

import androidx.core.text.isDigitsOnly
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

    val checkExistRecordOutcome = MutableLiveData<Outcome<Boolean>>()
    val checkNricOutcome = MutableLiveData<Outcome<Boolean>>()

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

    fun validateNRIC(nric: String) {
        checkNricOutcome.postValue(Outcome.loading(true))

        if(nric.isEmpty()) {
            checkNricOutcome.postValue(Outcome.loading(false))
            checkNricOutcome.postValue(Outcome.failure(
                Throwable("NRIC/Fin cannot be empty.")))
        } else if(nric.length != 9) {
            checkNricOutcome.postValue(Outcome.loading(false))
            checkNricOutcome.postValue(Outcome.failure(
                Throwable("Invalid NRIC/ FIN.")))
        } else if(nric[0].isDigit()) {
            checkNricOutcome.postValue(Outcome.loading(false))
            checkNricOutcome.postValue(Outcome.failure(
                Throwable("Invalid NRIC/ FIN.")))
        } else if(nric.last().isDigit()) {
            checkNricOutcome.postValue(Outcome.loading(false))
            checkNricOutcome.postValue(Outcome.failure(
                Throwable("Invalid NRIC/ FIN.")))
        } else {
            var validateThis = nric.removeRange(0,1)
            validateThis = validateThis.removeRange(validateThis.length-1, validateThis.length)

            if(validateThis.isDigitsOnly()) {
                checkNricOutcome.postValue(Outcome.loading(false))
                checkNricOutcome.postValue(Outcome.Success(true))
                localRepository.saveNric(nric)
                localRepository.saveLastCharacter(validateThis.last().toInt())
            } else {
                checkNricOutcome.postValue(Outcome.loading(false))
                checkNricOutcome.postValue(Outcome.failure(
                    Throwable("Invalid NRIC/ FIN.")))
            }
        }
    }

}