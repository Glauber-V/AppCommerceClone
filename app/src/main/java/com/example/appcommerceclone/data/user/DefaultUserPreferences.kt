package com.example.appcommerceclone.data.user

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.appcommerceclone.data.dispatcher.DispatcherProvider
import com.example.appcommerceclone.data.user.UserPreferencesKeys.USER_PREFERENCES_DATASTORE_NAME
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class DefaultUserPreferences(
    private val context: Context,
    private val dispatcher: DispatcherProvider
) : UserPreferences {

    private val Context.userPreferencesDataStore by preferencesDataStore(
        name = USER_PREFERENCES_DATASTORE_NAME
    )


    override suspend fun getIntValueFromKey(key: Preferences.Key<Int>): Int {
        return runCatching {
            withContext(dispatcher.io) {
                val data = context.userPreferencesDataStore.data.map { preferences ->
                    preferences[key] ?: 0
                }
                data.first()
            }
        }.getOrElse { 0 }
    }

    override suspend fun saveIntValueToKey(key: Preferences.Key<Int>, value: Int) {
        runCatching {
            withContext(dispatcher.io) {
                context.userPreferencesDataStore.edit { mutablePreferences ->
                    mutablePreferences[key] = value
                }
            }
        }
    }
}