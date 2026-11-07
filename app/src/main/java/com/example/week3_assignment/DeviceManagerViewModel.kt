package com.example.week3_assignment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DeviceDashboardUiState(
    val masterDeviceList: List<HomeDevice> = emptyList(),
    val filteredDeviceList: List<HomeDevice> = emptyList(),
    val searchKeyword: String = "",
    val activeRoomFilter: String = "Tüm Odalar",
    val roomCategories: List<String> = emptyList(),
    val totalPowerConsumption: Double = 0.0
)

class DeviceManagerViewModel : ViewModel() {
    private val repository = HomeDeviceRepository()

    private val _uiState = MutableStateFlow(DeviceDashboardUiState())
    val uiState: StateFlow<DeviceDashboardUiState> = _uiState.asStateFlow()

    init {
        fetchDevices()
    }

    private fun fetchDevices() {
        viewModelScope.launch(Dispatchers.Default) {
            val devices = repository.loadInitialData()
            val rooms = listOf("Tüm Odalar") + devices.map { it.locationRoom }.distinct()

            _uiState.update {
                it.copy(
                    masterDeviceList = devices,
                    filteredDeviceList = devices,
                    roomCategories = rooms,
                    totalPowerConsumption = calculateActivePower(devices)
                )
            }
        }
    }

    fun onSearchKeywordChanged(newKeyword: String) {
        _uiState.update { it.copy(searchKeyword = newKeyword) }
        reapplyFilters()
    }

    fun onRoomFilterSelected(room: String) {
        _uiState.update { it.copy(activeRoomFilter = room) }
        reapplyFilters()
    }

    fun toggleDevicePower(deviceId: Int) {
        viewModelScope.launch(Dispatchers.Default) {
            val updated = _uiState.value.masterDeviceList.map { item ->
                if (item.id == deviceId) item.copy(isPoweredOn = !item.isPoweredOn) else item
            }
            _uiState.update { it.copy(masterDeviceList = updated) }
            reapplyFilters()
        }
    }

    private fun reapplyFilters() {
        viewModelScope.launch(Dispatchers.Default) {
            val current = _uiState.value
            val query = current.searchKeyword.trim().lowercase()
            val selectedRoom = current.activeRoomFilter

            val filtered = current.masterDeviceList.filter { device ->
                val matchesQuery = device.deviceName.lowercase().contains(query)
                val matchesRoom = selectedRoom == "Tüm Odalar" || device.locationRoom == selectedRoom
                matchesQuery && matchesRoom
            }

            val totalWatts = calculateActivePower(current.masterDeviceList)

            _uiState.update {
                it.copy(
                    filteredDeviceList = filtered,
                    totalPowerConsumption = totalWatts
                )
            }
        }
    }

    private fun calculateActivePower(devices: List<HomeDevice>): Double {
        return devices.filter { it.isConnected && it.isPoweredOn }.sumOf { it.energyUsageWatts }
    }
}