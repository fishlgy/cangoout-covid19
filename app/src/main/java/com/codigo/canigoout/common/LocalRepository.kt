package com.codigo.canigoout.common


interface LocalRepository {
    fun saveNric(nric: String)
    fun getNric(): String
    fun clearNric()
    fun saveLastCharacter(lastCharacter: Int)
    fun getLastCharacter(): Int
    fun isLastCharacterOdd(): Boolean
}

class LocalRepositoryImpl(
    private val prefs: CanIGoOutPreference
) : LocalRepository {
    override fun saveNric(nric: String) {
        prefs.saveNric(nric)
    }

    override fun getNric(): String {
        return prefs.getNric()
    }

    override fun clearNric() {
        prefs.clearNric()
    }

    override fun saveLastCharacter(lastCharacter: Int) {
        prefs.saveLastCharacter(lastCharacter)
    }

    override fun getLastCharacter(): Int {
        return prefs.getLastCharacter()
    }

    override fun isLastCharacterOdd(): Boolean {
        return prefs.isLastCharacterOdd()
    }
}