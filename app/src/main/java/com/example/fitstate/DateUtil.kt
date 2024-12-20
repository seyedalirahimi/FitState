package com.example.fitstate

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


fun Date.formatToMonthDay(): String {
    val sdf = SimpleDateFormat("MMM dd", Locale.getDefault())
    return sdf.format(this)
}

