package com.lvca.magnettotorrent.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lvca.magnettotorrent.R
import com.lvca.magnettotorrent.ui.theme.LightGreen
import com.lvca.magnettotorrent.ui.theme.UbuntuFontFamily
import com.lvca.magnettotorrent.ui.theme.md_theme_light_scrim

@Composable
fun SettingItem(title: String, description: String, icon: Painter?, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .padding(vertical = 8.dp),
        color = LightGreen,
        shape = RoundedCornerShape(24.dp),
        shadowElevation = 4.dp,
    ) {
        Row(
            modifier = Modifier
                .clickable { onClick() }
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = icon ?: painterResource(id = R.drawable.ic_info),
                contentDescription = "Arrow right",
                tint = md_theme_light_scrim,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(end = 12.dp)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = title,
                    maxLines = 1,
                    color = md_theme_light_scrim,
                    fontFamily = UbuntuFontFamily,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = description,
                    maxLines = 1,
                    color = md_theme_light_scrim,
                )
            }
        }
    }
}