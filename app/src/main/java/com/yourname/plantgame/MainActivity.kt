package com.yourname.plantgame


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.yourname.plantgame.ui.theme.PlantGameTheme
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PlantGameTheme {
                Surface {
                    PlantGameScreen()
                }
            }
        }
    }
}

@Composable
fun PlantGameScreen() {
    val gameManager = remember { GameManager.getInstance() }

    // Add state variables to trigger recomposition
    var waterCount by remember { mutableStateOf(gameManager.player.getWater()) }
    var plantStage by remember { mutableStateOf(gameManager.plantGirl.getGrowthStage()) }
    var plantStatus by remember { mutableStateOf(gameManager.plantGirl.getStatus()) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(
                id = when (plantStage) {
                    1 -> R.drawable.plant_stage1
                    2 -> R.drawable.plant_stage2
                    //3 -> R.drawable.plant_stage3
                    else -> R.drawable.plant_stage1
                }
            ),
            contentDescription = "Plant Girl",
            modifier = Modifier.size(600.dp)
        )

        Text(
            text = plantStatus,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (gameManager.player.useWater()) {
                    gameManager.plantGirl.giveWater()
                    // Update all state variables
                    waterCount = gameManager.player.getWater()
                    plantStage = gameManager.plantGirl.getGrowthStage()
                    plantStatus = gameManager.plantGirl.getStatus()
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            )
        ) {
            Text("Water ($waterCount)")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Steps: ${gameManager.player.getTotalSteps()}",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}