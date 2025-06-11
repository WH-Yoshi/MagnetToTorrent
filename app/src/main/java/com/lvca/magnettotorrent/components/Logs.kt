package com.lvca.magnettotorrent.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lvca.magnettotorrent.ui.theme.White

@Composable
fun LogsScreen(logsState: List<String>, logsTitle: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = logsTitle,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp),
            color = White
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            items(logsState.size) { index ->
                val log = logsState[index]
                Text(
                    text = "- $log",
                    fontSize = 12.sp,
                    modifier = Modifier
                        .fillMaxWidth(),
                    color = White
                )
            }
        }
    }
}