package com.yourname.plantgame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material3.*
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.TextField
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.TextField
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlantGameScreen() {
    val gameManager = remember { GameManager.getInstance() }
    var showShopDialog by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf<Item?>(null) }
    var dropdownExpanded by remember { mutableStateOf(false) }
    val items = listOf(Item.water, Item.soil, Item.cake)

    // State variables
    var waterCount by remember { mutableStateOf(gameManager.player.getWater()) }
    var soilCount by remember { mutableStateOf(gameManager.player.getSoil()) }
    var cakeCount by remember { mutableStateOf(gameManager.player.getCakes()) }
    var plantStage by remember { mutableStateOf(gameManager.plantGirl.getGrowthStage()) }
    var plantStatus by remember { mutableStateOf(gameManager.plantGirl.getStatus()) }
    var steps by remember { mutableStateOf(gameManager.player.getTotalSteps()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
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

        // Step Counter
        Text(
            text = "Steps: $steps",
            style = MaterialTheme.typography.titleLarge
        )

        // Item Selection Dropdown
        Box(modifier = Modifier.fillMaxWidth(0.8f)) {
            ExposedDropdownMenuBox(
                expanded = dropdownExpanded,
                onExpandedChange = { dropdownExpanded = it }
            ) {
                TextField(
                    modifier = Modifier.menuAnchor(),
                    readOnly = true,
                    value = selectedItem?.let {
                        "${it.name} (${when(it) {
                            Item.water -> waterCount
                            Item.soil -> soilCount
                            Item.cake -> cakeCount
                        }})"
                    } ?: "Select item to feed",
                    onValueChange = {},
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = dropdownExpanded
                        )
                    },
                    colors = ExposedDropdownMenuDefaults.textFieldColors()
                )

                ExposedDropdownMenu(
                    expanded = dropdownExpanded,
                    onDismissRequest = { dropdownExpanded = false }
                ) {
                    items.forEach { item ->
                        val count = when (item) {
                            Item.water -> waterCount
                            Item.soil -> soilCount
                            Item.cake -> cakeCount
                        }

                        if (count >= 0) {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = "${item.name.replaceFirstChar { it.uppercase() }} ($count)",
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                },
                                onClick = {
                                    selectedItem = item
                                    dropdownExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        }

        // Feed Button
        Button(
            onClick = {
                selectedItem?.let { item ->
                    when (item) {
                        Item.water -> {
                            if (gameManager.player.useWater()) {
                                gameManager.plantGirl.giveWater()
                                waterCount = gameManager.player.getWater()
                            }
                        }
                        Item.soil -> {
                            if (gameManager.player.useSoil()) {
                                gameManager.plantGirl.giveSoil()
                                soilCount = gameManager.player.getSoil()
                            }
                        }
                        Item.cake -> {
                            if (gameManager.player.useCake()) {
                                gameManager.plantGirl.giveCake()
                                cakeCount = gameManager.player.getCakes()
                            }
                        }
                    }
                    plantStage = gameManager.plantGirl.getGrowthStage()
                    plantStatus = gameManager.plantGirl.getStatus()
                }
            },
            enabled = selectedItem != null,
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Text("Feed Selected Item")
        }

        // Shop Button
        Button(
            onClick = { showShopDialog = true },
            modifier = Modifier.fillMaxWidth(0.8f),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = Color.White
            )
        ) {
            Text("Open Shop")
        }

        // Shop Dialog
        if (showShopDialog) {
            AlertDialog(
                onDismissRequest = { showShopDialog = false },
                title = { Text("Shop - Steps: $steps") },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
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
                                    cakeCount = gameManager.player.getCakes()
                                    steps = gameManager.player.getTotalSteps()
                                }
                            }
                        )
                    }
                },
                confirmButton = {
                    Button(onClick = { showShopDialog = false }) {
                        Text("Close Shop")
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

    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (enabled) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Text("$itemName - $price steps")
    }
}
