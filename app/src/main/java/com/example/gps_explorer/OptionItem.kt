package com.example.gps_explorer

data class OptionItem(
    val id: String,
    val label: String,
    val iconRes: Int,
    var selected: Boolean = false
)