package com.example.appcommerceclone.data.user

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.appcommerceclone.data.user.UserPreferencesKeys.TEST_USER_PREFERENCES_DATASTORE_NAME
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

@ExperimentalCoroutinesApi
class FakeUserPreferences(private val context: Context) : UserPreferences {

    private val Context.testUserPreferencesDataStore by preferencesDataStore(
        name = TEST_USER_PREFERENCES_DATASTORE_NAME
    )


    override suspend fun getIntValueFromKey(key: Preferences.Key<Int>): Int {
        val data = context.testUserPreferencesDataStore.data.map { preferences ->
            preferences[key] ?: 0
        }
        return data.first()
    }

    override suspend fun saveIntValueToKey(key: Preferences.Key<Int>, value: Int) {
        context.testUserPreferencesDataStore.edit { mutablePreferences ->
            mutablePreferences[key] = value
        }
    }
}