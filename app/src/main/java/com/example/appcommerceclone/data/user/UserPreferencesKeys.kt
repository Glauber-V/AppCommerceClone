package com.example.appcommerceclone.data.user

import androidx.datastore.preferences.core.intPreferencesKey

object UserPreferencesKeys {

    /** Use only in production code */
    const val USER_PREFERENCES_DATASTORE_NAME: String = "USER_PREFERENCES_DATASTORE"

    /** Use only in test code */
    const val TEST_USER_PREFERENCES_DATASTORE_NAME: String = "TEST_USER_PREFERENCES_DATASTORE"

    val USER_PREF_ID_KEY = intPreferencesKey("USER_ID_KEY")
}