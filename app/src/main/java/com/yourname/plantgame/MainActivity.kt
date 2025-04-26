package com.yourname.plantgame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight

@Composable
fun PlantGameScreen() {
    val gameManager = remember { GameManager.getInstance() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Plant Status
        Text(
            text = gameManager.plantGirl.getStatus(),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Action Buttons
        ActionButton(
            text = "Water (${gameManager.player.getWater()})",
            onClick = {
                if (gameManager.player.useWater()) {
                    gameManager.plantGirl.giveWater()
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Step Counter
        Text(
            text = "Steps: ${gameManager.player.getTotalSteps()}",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun ActionButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = Color.White
        )
    ) {
        Text(text)
    }
}