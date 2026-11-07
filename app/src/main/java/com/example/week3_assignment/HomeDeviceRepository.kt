package com.example.week3_assignment

class HomeDeviceRepository {
    fun loadInitialData(): List<HomeDevice> {
        return listOf(
            HomeDevice(1, "Köşe Lambası", DeviceType.LIGHT, "Salon", true, true, 15.0),
            HomeDevice(2, "Tavan Avizesi", DeviceType.LIGHT, "Salon", true, false, 60.0),
            HomeDevice(3, "Bahçe Kamerası", DeviceType.CAMERA, "Bahçe", true, true, 8.5),
            HomeDevice(4, "Giriş Kamerası", DeviceType.CAMERA, "Antre", true, true, 8.5),
            HomeDevice(5, "Merkezi Termostat", DeviceType.THERMOSTAT, "Salon", true, true, 12.0),
            HomeDevice(6, "Yatak Odası Kliması", DeviceType.THERMOSTAT, "Yatak Odası", true, false, 1200.0),
            HomeDevice(7, "Mutfak Spotları", DeviceType.LIGHT, "Mutfak", true, false, 45.0),
            HomeDevice(8, "Roborock S7", DeviceType.VACUUM, "Koridor", true, true, 50.0),
            HomeDevice(9, "Balkon Aydınlatması", DeviceType.LIGHT, "Balkon", false, false, 25.0),
            HomeDevice(10, "Çalışma Odası Lambası", DeviceType.LIGHT, "Ofis", true, true, 12.0)
        )
    }
}