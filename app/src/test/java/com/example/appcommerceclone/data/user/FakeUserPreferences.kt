package com.example.appcommerceclone.data.user

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStoreFile
import com.example.appcommerceclone.data.dispatcher.DispatcherProvider
import com.example.appcommerceclone.data.user.UserPreferencesKeys.TEST_USER_PREFERENCES_DATASTORE_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class FakeUserPreferences(
    @ApplicationContext private val context: Context,
    dispatcherProvider: DispatcherProvider
) : UserPreferences {

    private val preferenceDataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create(
        scope = CoroutineScope(dispatcherProvider.io + SupervisorJob()),
        produceFile = { context.preferencesDataStoreFile(TEST_USER_PREFERENCES_DATASTORE_NAME) }
    )

    override suspend fun getIntValueFromKey(key: Preferences.Key<Int>): Int {
        val data = preferenceDataStore.data.map { it[key] ?: 0 }
        return data.first()
    }

    override suspend fun saveIntValueToKey(key: Preferences.Key<Int>, value: Int) {
        preferenceDataStore.edit { it[key] = value }
    }
}