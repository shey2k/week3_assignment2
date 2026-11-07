package com.example.week3_assignment

enum class DeviceType {
    LIGHT, CAMERA, THERMOSTAT, VACUUM
}

data class HomeDevice(
    val id: Int,
    val deviceName: String,
    val category: DeviceType,
    val locationRoom: String,
    val isConnected: Boolean,
    val isPoweredOn: Boolean,
    val energyUsageWatts: Double
)