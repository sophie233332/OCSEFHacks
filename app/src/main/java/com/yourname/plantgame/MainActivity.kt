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
    var showShopDialog by remember { mutableStateOf(false) } // Shop dialog state

    // State variables
    var waterCount by remember { mutableStateOf(gameManager.player.getWater()) }
    var soilCount by remember { mutableStateOf(gameManager.player.getSoil()) }
    var plantStage by remember { mutableStateOf(gameManager.plantGirl.getGrowthStage()) }
    var plantStatus by remember { mutableStateOf(gameManager.plantGirl.getStatus()) }
    var steps by remember { mutableStateOf(gameManager.player.getTotalSteps()) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Plant Image
        Image(
            painter = painterResource(
                id = when (plantStage) {
                    1 -> R.drawable.plant_stage1
                    2 -> R.drawable.plant_stage2
                    else -> R.drawable.plant_stage1
                }
            ),
            contentDescription = "Plant Girl",
            modifier = Modifier.size(300.dp)
        )

        // Plant Status
        Text(
            text = plantStatus,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Water Button
        Button(
            onClick = {
                if (gameManager.player.useWater()) {
                    gameManager.plantGirl.giveWater()
                    // Update states
                    waterCount = gameManager.player.getWater()
                    plantStage = gameManager.plantGirl.getGrowthStage()
                    plantStatus = gameManager.plantGirl.getStatus()
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ),
            modifier = Modifier.width(200.dp)
        ) {
            Text("Water ($waterCount)")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Shop Button
        Button(
            onClick = { showShopDialog = true },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = Color.White
            ),
            modifier = Modifier.width(200.dp)
        ) {
            Text("Shop")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Step Counter
        Text(
            text = "Steps: $steps",
            style = MaterialTheme.typography.bodyLarge
        )

        // Shop Dialog
        if (showShopDialog) {
            AlertDialog(
                onDismissRequest = { showShopDialog = false },
                title = { Text("Shop - Steps: $steps") },
                text = {
                    Column {
                        ShopItem(
                            item = Item.water,
                            price = gameManager.shop.getPrice(Item.water),
                            currentSteps = steps,
                            onClick = {
                                if (gameManager.shop.buyItem(gameManager.player, Item.water)) {
                                    waterCount = gameManager.player.getWater()
                                    steps = gameManager.player.getTotalSteps()
                                }
                            }
                        )
                        ShopItem(
                            item = Item.soil,
                            price = gameManager.shop.getPrice(Item.soil),
                            currentSteps = steps,
                            onClick = {
                                if (gameManager.shop.buyItem(gameManager.player, Item.soil)) {
                                    soilCount = gameManager.player.getSoil()
                                    steps = gameManager.player.getTotalSteps()
                                }
                            }
                        )
                        ShopItem(
                            item = Item.cake,
                            price = gameManager.shop.getPrice(Item.cake),
                            currentSteps = steps,
                            onClick = {
                                if (gameManager.shop.buyItem(gameManager.player, Item.cake)) {
                                    steps = gameManager.player.getTotalSteps()
                                }
                            }
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { showShopDialog = false }
                    ) {
                        Text("Close")
                    }
                }
            )
        }
    }
}

@Composable
fun ShopItem(item: Item, price: Int, currentSteps: Int, onClick: () -> Unit) {
    val enabled = currentSteps >= price
    val itemName = when (item) {
        Item.water -> "Water (+10 affection)"
        Item.soil -> "Soil (+15 affection)"
        Item.cake -> "Cake (+25 affection)"
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Button(
            onClick = onClick,
            enabled = enabled,
            modifier = Modifier.weight(1f)
        ) {
            Text("$itemName - $price steps")
        }
    }
}