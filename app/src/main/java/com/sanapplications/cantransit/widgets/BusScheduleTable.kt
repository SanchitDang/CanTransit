package com.sanapplications.cantransit.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sanapplications.cantransit.models.BusInfo

@Composable
fun BusScheduleTable(busList: List<BusInfo>, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(16.dp)) {
        // Heading row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(8.dp))
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Bus", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(text = "Destination", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(text = "Arrival", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(text = "ETA", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(busList.size) { index ->
                BusScheduleRow(busInfo = busList[index])
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}
