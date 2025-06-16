package com.example.steamapp.student

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.steamapp.ui.theme.SteamAppTheme
import kotlin.math.roundToInt

@Composable
fun BarChart(
    data: Map<Float, String>,
    max_value: Int
) {
    val context = LocalContext.current
    val barGraphHeight = 200.dp
    val barGraphWidth = 18.dp
    val scaleYAxisWidth = 40.dp
    val scaleLineWidth = 2.dp

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(barGraphHeight),
            verticalAlignment = Alignment.Bottom,
        ) {
            // Y-axis scale
            Column(
                modifier = Modifier
                    .width(scaleYAxisWidth)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.End
            ) {
                Text(text = max_value.toString(), style = MaterialTheme.typography.labelSmall)
                Text(text = (max_value / 2).toString(), style = MaterialTheme.typography.labelSmall)
                Text(text = "0", style = MaterialTheme.typography.labelSmall)
            }

            // Y-axis line
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(scaleLineWidth)
                    .background(Color.Black)
            )

            // Bars in LazyRow
            LazyRow(
                modifier = Modifier.fillMaxHeight(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp)
            ) {
                items(data.entries.toList().size) { index ->
                    val entry = data.entries.toList()[index]
                    Column(
                        modifier = Modifier
                            .height(barGraphHeight)
                            .width(barGraphWidth),
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val value= entry.key
                        Text(
                            text = value.toInt().toString(),
                            style = MaterialTheme.typography.labelSmall,
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(entry.key/100f)
                                .clip(RoundedCornerShape(6.dp))
                                .background(MaterialTheme.colorScheme.primary)
                                .clickable {
                                    Toast
                                        .makeText(context, entry.key.toString(), Toast.LENGTH_SHORT)
                                        .show()
                                }
                        )
                    }
                    Spacer(Modifier.width(23.dp))
                }
            }
        }

        // X-axis line
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(scaleLineWidth)
                .background(Color.Black)
        )

        // X-axis labels
        LazyRow(
            modifier = Modifier
                .padding(top = 4.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                start = scaleYAxisWidth + scaleLineWidth + 8.dp,
                end = 8.dp
            )
        ) {
            items(data.values.toList()) { label ->
                Text(
                    text = label,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelSmall,
                )
                Spacer(Modifier.width(6.dp))
            }
        }
    }
}

@Preview
@Composable
private fun BarChartPreview() {
    SteamAppTheme {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp).background(MaterialTheme.colorScheme.background)
        ) {
            val dataPoints= mapOf(
                Pair(72f ,"Maths"),
                Pair(65f,"Nepali"),
                Pair(98f ,"Social"),
                Pair(12f ,"English"),
                Pair(45f ,"Science"),
            )
            BarChart(
                data = dataPoints,
                max_value = 100
            )
        }
    }
}
