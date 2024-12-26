package com.example.fitstate.ui.screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.fitstate.ui.model.BodyState
import com.example.fitstate.ui.screen.component.rememberMarker
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoZoomState
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer
import java.util.Calendar

@Composable
fun WeightChart(
    modifier: Modifier = Modifier, bodyStates: List<BodyState>
) {
    if (bodyStates.isEmpty()) {
        Text(text = "No data available", modifier = modifier)
    } else {
        val modelProducer = remember { CartesianChartModelProducer() }
        LaunchedEffect(bodyStates) {
            modelProducer.runTransaction {
                lineSeries {
                    series(generateInterpolatedBodyStateList(bodyStates).map { it.weight.toDouble() })
                }
            }
        }


        CartesianChartHost(
            chart = rememberCartesianChart(
                layers = arrayOf(
                    rememberLineCartesianLayer(
                        LineCartesianLayer.LineProvider.series(
                            LineCartesianLayer.rememberLine(remember {
                                LineCartesianLayer.LineFill.single(
                                    fill(Color(0xffa485e0))
                                )
                            })
                        )
                    )
                ),
                startAxis = VerticalAxis.rememberStart(
                ),
                bottomAxis = HorizontalAxis.rememberBottom(
                    guideline = null,
                    itemPlacer = remember { HorizontalAxis.ItemPlacer.segmented() },
                    valueFormatter = bottomAxisValueFormatter
                ),
                marker = rememberMarker(),

                ),
            modelProducer = modelProducer,
            modifier = modifier,
            zoomState = rememberVicoZoomState(zoomEnabled = false)
        )
    }
}


private val daysOfWeek = listOf("M", "T", "W", "T", "F", "S", "S")
private val bottomAxisValueFormatter = CartesianValueFormatter { _, x, _ ->
    daysOfWeek[x.toInt() % daysOfWeek.size]
}


fun generateInterpolatedBodyStateList(originalList: List<BodyState>): List<BodyState> {
    if (originalList.isEmpty()) return emptyList()

    val sortedList = originalList.sortedBy { it.date }

    val calendar = Calendar.getInstance()
    val interpolatedList = mutableListOf<BodyState>()

    for (i in 0 until sortedList.size - 1) {
        val current = sortedList[i]
        val next = sortedList[i + 1]

        val currentDate = current.date
        val nextDate = next.date

        val currentWeight = current.weight
        val nextWeight = next.weight

        interpolatedList.add(current)

        calendar.time = currentDate
        while (calendar.time.before(nextDate)) {
            calendar.add(Calendar.DAY_OF_MONTH, 1)
            val interpolatedDate = calendar.time

            if (interpolatedDate.before(nextDate)) {
                val daysBetween =
                    ((nextDate.time - currentDate.time) / (1000 * 60 * 60 * 24)).toInt()
                val dayIndex =
                    ((interpolatedDate.time - currentDate.time) / (1000 * 60 * 60 * 24)).toInt()

                // Calculate interpolated weight
                val interpolatedWeight =
                    currentWeight + (nextWeight - currentWeight) * (dayIndex.toFloat() / daysBetween)

                interpolatedList.add(
                    BodyState(
                        weight = interpolatedWeight, date = interpolatedDate
                    )
                )
            }
        }
    }

    // Add the last BodyState
    interpolatedList.add(sortedList.last())

    return interpolatedList
}


