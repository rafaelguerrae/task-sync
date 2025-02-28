package com.guerra.tasksync.screen

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.os.LocaleListCompat
import java.util.Locale

@Composable
fun SettingsScreen(){

    val context = LocalContext.current

    Box(
        modifier = Modifier.fillMaxSize()
    ){

        Text(text = "This will be your settings screen", modifier = Modifier.align(Alignment.Center))

            Row {
                Button(
                    onClick = {updateLocale(context, "pt")}
                ) {
                    Text("Portuguese")
                }

                Button(
                    onClick = {updateLocale(context, "en")}
                ) {
                    Text("English")
                }

                Button(
                    onClick = {updateLocale(context, "es")}
                ) {
                    Text("Spanish")
                }
            }

    }
}

fun updateLocale(context: Context, language: String) {
    val locale = Locale(language)
    Locale.setDefault(locale)
    val configuration = Configuration(context.resources.configuration)
    configuration.setLocale(locale)

    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(language))
    } else {
        context.resources.updateConfiguration(configuration, context.resources.displayMetrics)
        if (context is Activity) {
            context.recreate()
        }
    }
}