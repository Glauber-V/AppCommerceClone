package com.example.appcommerceclone.data.user

import androidx.datastore.preferences.core.Preferences

interface UserPreferences {

    suspend fun getIntValueFromKey(key: Preferences.Key<Int>): Int

    suspend fun saveIntValueToKey(key: Preferences.Key<Int>, value: Int)
}