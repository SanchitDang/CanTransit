package com.sanapplications.cantransit.repository

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sanapplications.cantransit.models.CardSettingsModel
import com.sanapplications.cantransit.models.TripHistoryModel

class SettingsRepository(private val sharedPreferences: SharedPreferences) {

    private val gson = Gson()
    private val settingsKey = "card_settings"
    private val tripsHistoryKey = "trips_history"
    private val isFirstLaunchKey = "is_first_time"

    // Change first launch status
    fun changeFirstLaunch(){
        sharedPreferences.edit().putBoolean(isFirstLaunchKey, false).apply()
    }

    // Check if the app is launched first time or not
    fun isFirstLaunch(): Boolean {
        return sharedPreferences.getBoolean(isFirstLaunchKey, true)
    }

    // Save Card Settings to SharedPreferences
    fun saveCardSettings(cardSettings: CardSettingsModel) {
        val json = gson.toJson(cardSettings)
        sharedPreferences.edit().putString(settingsKey, json).apply()
    }

    // Retrieve Card Settings from SharedPreferences
    fun getCardSettings(): CardSettingsModel {
        val json = sharedPreferences.getString(settingsKey, null)
        return if (json != null) {
            gson.fromJson(json, CardSettingsModel::class.java)
        } else {
            CardSettingsModel()
        }
    }

    // Retrieve the trip history list from SharedPreferences
    fun getTripHistory(): List<TripHistoryModel> {
        val json = sharedPreferences.getString(tripsHistoryKey, null)
        return if (json != null) {
            val type = object : TypeToken<List<TripHistoryModel>>() {}.type
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }

    // Add a new trip to the trip history list and save it
    fun addTripHistory(tripHistory: TripHistoryModel) {
        val currentHistory = getTripHistory().toMutableList()
        currentHistory.add(tripHistory)
        saveTripHistory(currentHistory)
    }

    // Save the trip history list to SharedPreferences
    private fun saveTripHistory(tripHistoryList: List<TripHistoryModel>) {
        val json = gson.toJson(tripHistoryList)
        sharedPreferences.edit().putString(tripsHistoryKey, json).apply()
    }
}
