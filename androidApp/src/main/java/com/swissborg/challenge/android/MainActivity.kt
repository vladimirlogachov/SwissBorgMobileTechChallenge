package com.swissborg.challenge.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.swissborg.challenge.presentation.ChallengeApp
import org.koin.core.context.stopKoin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            ChallengeApp()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopKoin()
    }
}
