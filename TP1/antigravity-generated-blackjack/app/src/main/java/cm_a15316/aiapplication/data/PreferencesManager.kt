package cm_a15316.aiapplication.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "blackjack_prefs")

class PreferencesManager(private val context: Context) {
    companion object {
        private val CHIP_BALANCE_KEY = intPreferencesKey("chip_balance")
        private const val DEFAULT_BALANCE = 1000
    }

    val chipBalanceFlow: Flow<Int> = context.dataStore.data
        .map { preferences ->
            preferences[CHIP_BALANCE_KEY] ?: DEFAULT_BALANCE
        }

    suspend fun updateChipBalance(newBalance: Int) {
        context.dataStore.edit { preferences ->
            preferences[CHIP_BALANCE_KEY] = newBalance
        }
    }
}
