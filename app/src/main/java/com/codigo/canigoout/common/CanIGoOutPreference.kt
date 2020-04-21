package com.codigo.canigoout.common

import android.content.SharedPreferences


interface CanIGoOutPreference {
    fun saveNric(nric: String)
    fun getNric(): String
    fun clearNric()
    fun saveLastCharacter(lastCharacter: Int)
    fun getLastCharacter():Int
    fun isLastCharacterOdd(): Boolean
}

class CanIGoOutPreferenceImpl(
    private val prefs: SharedPreferences
) : CanIGoOutPreference {
    override fun saveNric(nric: String) {
        prefs.edit().putString("nric", nric).apply()
    }

    override fun getNric(): String {
        return prefs.getString("nric", "")?:""
    }

    override fun clearNric() {
        prefs.edit().putString("nric","").apply()
    }

    override fun saveLastCharacter(lastCharacter: Int) {
        prefs.edit().putInt("lastCharacter", lastCharacter).apply()
    }

    override fun getLastCharacter(): Int {
        return prefs.getInt("lastCharacter", 0)
    }

    override fun isLastCharacterOdd(): Boolean {
        return prefs.getInt("lastCharacter", 1) % 2 == 1
    }

}
