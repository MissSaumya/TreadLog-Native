package com.treadlog.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// At the top level of your kotlin file:
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SharedPrefsRepository(private val context: Context) {
    private val LAST_OILED_DATE = stringPreferencesKey("last_oiled_date")

    val lastOiledDateFlow: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[LAST_OILED_DATE]
    }

    suspend fun saveLastOiledDate(dateString: String) {
        context.dataStore.edit { preferences ->
            preferences[LAST_OILED_DATE] = dateString
        }
    }
}
