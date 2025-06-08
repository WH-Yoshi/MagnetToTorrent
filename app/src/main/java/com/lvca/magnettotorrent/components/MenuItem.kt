package com.lvca.magnettotorrent.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lvca.magnettotorrent.R
import com.lvca.magnettotorrent.ui.theme.UbuntuFontFamily
import com.lvca.magnettotorrent.ui.theme.md_theme_light_scrim
import com.lvca.magnettotorrent.ui.theme.LightGreen

@Composable
fun MenuItem(title: String, description: String, direction: String = "right", onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp),
        color = LightGreen,
        shape = RoundedCornerShape(24.dp),
        shadowElevation = 4.dp,
    ) {
        if (direction == "left") {
            Row(
                modifier = Modifier
                    .clickable { onClick() }
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_left),
                    contentDescription = "Arrow left",
                    tint = md_theme_light_scrim,
                    modifier = Modifier
                        .align(Alignment.CenterVertically),
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
        } else {
            Row(
                modifier = Modifier
                    .clickable { onClick() }
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
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
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = description,
                        maxLines = 1,
                        color = md_theme_light_scrim,
                    )
                }
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_right),
                    contentDescription = "Arrow right",
                    tint = md_theme_light_scrim,
                    modifier = Modifier
                        .align(Alignment.CenterVertically),
                )
            }
        }
    }
}