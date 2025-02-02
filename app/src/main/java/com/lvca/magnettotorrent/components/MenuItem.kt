package com.lvca.magnettotorrent.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lvca.magnettotorrent.ui.theme.UbuntuFontFamily
import com.lvca.magnettotorrent.ui.theme.md_theme_light_onTertiary
import com.lvca.magnettotorrent.ui.theme.md_theme_light_scrim
import com.lvca.magnettotorrent.ui.theme.md_theme_light_tertiary
import com.lvca.magnettotorrent.ui.theme.md_theme_light_tertiaryContainer

@Composable
fun MenuItem(title: String, description: String, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp),
        color = md_theme_light_tertiaryContainer,
        shape = RoundedCornerShape(32.dp),
        shadowElevation = 4.dp,
    ) {
        Row(
            modifier = Modifier
                .clickable { onClick() }
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = title,
                    maxLines = 1,
                    color = md_theme_light_scrim,
                    fontFamily = UbuntuFontFamily,
                    fontSize = 20.sp,
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