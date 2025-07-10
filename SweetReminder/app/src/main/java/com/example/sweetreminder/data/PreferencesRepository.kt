package com.example.sweetreminder.data
import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("settings")

class PreferencesRepository(private val ctx: Context) {
    private val intervalKey = intPreferencesKey("interval_hours")
    private val lastThemeKey = stringPreferencesKey("last_theme")

    suspend fun intervalHours() =
        ctx.dataStore.data.map { it[intervalKey] ?: 3 }.first()

    suspend fun setInterval(h: Int) =
        ctx.dataStore.edit { it[intervalKey] = h }

    suspend fun lastTheme() =
        ctx.dataStore.data.map { it[lastThemeKey] }.first()

    suspend fun saveLastTheme(t: String) =
        ctx.dataStore.edit { it[lastThemeKey] = t }
}
