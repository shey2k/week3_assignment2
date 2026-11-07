package com.example.week3_assignment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DeviceDashboardScreen(viewModel: DeviceManagerViewModel) {
    val state by viewModel.uiState.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            EnergyOverviewBanner(powerWatts = state.totalPowerConsumption)

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = state.searchKeyword,
                onValueChange = { viewModel.onSearchKeywordChanged(it) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Cihaz filtrele...") },
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            RoomFilterBar(
                rooms = state.roomCategories,
                selectedRoom = state.activeRoomFilter,
                onSelectRoom = { viewModel.onRoomFilterSelected(it) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(state.filteredDeviceList, key = { it.id }) { device ->
                    DeviceListItem(
                        device = device,
                        onPowerToggle = { viewModel.toggleDevicePower(device.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun EnergyOverviewBanner(powerWatts: Double) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Güç Tüketimi",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.8f)
                )
                Text(
                    text = "Aktif Cihazlar",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primary
            ) {
                Text(
                    text = "${"%.1f".format(powerWatts)} Watt",
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }
    }
}

@Composable
fun RoomFilterBar(rooms: List<String>, selectedRoom: String, onSelectRoom: (String) -> Unit) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(rooms) { room ->
            FilterChip(
                selected = room == selectedRoom,
                onClick = { onSelectRoom(room) },
                label = { Text(room) },
                shape = RoundedCornerShape(20.dp)
            )
        }
    }
}

@Composable
fun DeviceListItem(device: HomeDevice, onPowerToggle: () -> Unit) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = device.deviceName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(if (device.isConnected) Color(0xFF4CAF50) else Color.Gray)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = if (device.isConnected) "ONLINE" else "OFFLINE",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Konum: ${device.locationRoom} | Tür: ${device.category}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Güç Tüketimi: ${device.energyUsageWatts} W",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Button(
                onClick = onPowerToggle,
                enabled = device.isConnected,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (device.isPoweredOn) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(if (device.isPoweredOn) "Durdur" else "Başlat")
            }
        }
    }
}