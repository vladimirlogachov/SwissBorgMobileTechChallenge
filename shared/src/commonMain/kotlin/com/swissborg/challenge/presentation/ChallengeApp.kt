package com.swissborg.challenge.presentation

import androidx.compose.runtime.Composable
import com.swissborg.challenge.di.AppComponent
import com.swissborg.challenge.presentation.main.MainScreen
import com.swissborg.challenge.presentation.theme.ChallengeTheme
import org.koin.compose.KoinApplication

@Composable
fun ChallengeApp() = KoinApplication(application = { modules(AppComponent) }) {
    ChallengeTheme {
        MainScreen()
    }
}
