package com.guerra.tasksync.ui.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.guerra.tasksync.R

val EuclidFamily = FontFamily(
    Font(R.font.euclid_regular, FontWeight.Normal),
    Font(R.font.euclid_bold, FontWeight.Bold),
    Font(R.font.euclid_light, FontWeight.Light),
    Font(R.font.euclid_medium, FontWeight.Medium),
    Font(R.font.euclid_semibold, FontWeight.SemiBold),
    Font(R.font.euclid_semibold_italic, FontWeight.SemiBold, FontStyle.Italic),
    Font(R.font.euclid_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.euclid_light_italic, FontWeight.Light, FontStyle.Italic),
    Font(R.font.euclid_bold_italic, FontWeight.Bold, FontStyle.Italic),
    Font(R.font.euclid_medium_italic, FontWeight.Medium, FontStyle.Italic)
)