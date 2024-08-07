package com.swissborg.challenge.presentation

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.swissborg.challenge.di.AppComponent
import com.swissborg.challenge.presentation.main.MainScreen
import org.koin.compose.KoinApplication

@Composable
fun ChallengeApp() = KoinApplication(application = { modules(AppComponent) }) {
    MaterialTheme {
        MainScreen()
    }
}
