package com.codigo.canigoout.location

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.codigo.canigoout.common.CanIGoOutScheduler
import com.codigo.canigoout.common.LocalRepository
import com.codigo.canigoout.common.Outcome
import com.codigo.canigoout.common.model.LocationResult
import io.reactivex.disposables.CompositeDisposable
import java.util.*


class LocationViewModel(
    private val localRepository: LocalRepository,
    private val scheduler: CanIGoOutScheduler,
    private val compositeDisposable: CompositeDisposable
) : ViewModel() {

    val locationOutcome = MutableLiveData<Outcome<Boolean>>()
    val backOutcome = MutableLiveData<Outcome<Boolean>>()

    fun canIGoOut() {
        locationOutcome.postValue(Outcome.loading(true))

        val calendar = Calendar.getInstance(TimeZone.getTimeZone("Singapore"))
        val date = calendar.get(Calendar.DATE)
        val isDateOdd = date % 2 == 1
        val isLastCharacterOdd = localRepository.isLastCharacterOdd()
        val canGoOut = (isLastCharacterOdd == isDateOdd)

        locationOutcome.postValue(Outcome.loading(false))
        locationOutcome.postValue(Outcome.success(canGoOut))
    }

    fun goBack() {
        localRepository.clearNric()
        backOutcome.postValue(Outcome.success(true))
    }

}