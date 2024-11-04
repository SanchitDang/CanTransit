package com.sanapplications.cantransit.screens.settings_screen

import androidx.lifecycle.ViewModel
import com.sanapplications.cantransit.models.CardSettingsModel
import com.sanapplications.cantransit.models.TripHistoryModel
import com.sanapplications.cantransit.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SettingsViewModel(private val repository: SettingsRepository) : ViewModel() {

    private val _cardSettings = MutableStateFlow(CardSettingsModel())
    val cardSettings: StateFlow<CardSettingsModel> = _cardSettings

    private val _tripsHistory = MutableStateFlow<List<TripHistoryModel>>(emptyList())
    val tripsHistory: StateFlow<List<TripHistoryModel>> = _tripsHistory

    // Initialize with the value from repository to check if it's the first launch
    private val _isFirstLaunch = MutableStateFlow(repository.isFirstLaunch())
    val isFirstLaunch: StateFlow<Boolean> = _isFirstLaunch

    init {
        loadCardSettings()
        loadTripHistory()
    }

    private fun loadCardSettings() {
        _cardSettings.value = repository.getCardSettings()
    }

    fun updateCardSettings(updatedSettings: CardSettingsModel) {
        _cardSettings.value = updatedSettings
        repository.saveCardSettings(updatedSettings)
    }

    private fun loadTripHistory() {
        _tripsHistory.value = repository.getTripHistory()
    }

    fun addTripToHistory(tripHistory: TripHistoryModel) {
        repository.addTripHistory(tripHistory)
        _tripsHistory.value = repository.getTripHistory() // Refresh the history list
    }

    // Function to change first launch status
    fun setFirstLaunchComplete() {
        repository.changeFirstLaunch()
        _isFirstLaunch.value = false
    }
}
