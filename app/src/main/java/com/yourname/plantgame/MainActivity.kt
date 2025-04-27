package com.yourname.plantgame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yourname.plantgame.ui.theme.PlantGameTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlantGameTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
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
    val scope = rememberCoroutineScope()
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

    // Dialog state
    var showDialog by remember { mutableStateOf(true) }
    var dialogText by remember { mutableStateOf("") }
    var dialogAlpha by remember { mutableStateOf(1f) }
    val textBoxLastingMilliseconds = 6000L
    val disappearingMilliseconds = 1000L

    // Initial greeting
    LaunchedEffect(Unit) {
        dialogText = gameManager.plantGirl.getGreeting()
        showDialog = true
        delay(textBoxLastingMilliseconds)
        animate(
            initialValue = 1f,
            targetValue = 0f,
            animationSpec = tween(durationMillis = disappearingMilliseconds.toInt())
        ) { value, _ ->
            dialogAlpha = value
            if (value <= 0f) showDialog = false
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        // Background Layer
        Box(modifier = Modifier.fillMaxSize()) {
            // Background Image
            Image(
                painter = painterResource(id = R.drawable.background1),
                contentDescription = "Background",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Character Image
            Image(
                painter = painterResource(
                    id = when (plantStage) {
                        1 -> R.drawable.plant_stage1
                        2 -> R.drawable.plant_stage2
                        else -> R.drawable.plant_stage3
                    }
                ),
                contentDescription = "Plant Girl",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(800.dp)
                    .offset(y = (-70).dp)
                    .clickable {
                        dialogText = gameManager.plantGirl.getRandomResponse()
                        showDialog = true
                        dialogAlpha = 1f
                        scope.launch {
                            delay(textBoxLastingMilliseconds)
                            animate(
                                initialValue = 1f,
                                targetValue = 0f,
                                animationSpec = tween(durationMillis = disappearingMilliseconds.toInt())
                            ) { value, _ ->
                                dialogAlpha = value
                                if (value <= 0f) showDialog = false
                            }
                        }
                    }
            )
        }

        // Chat Dialog - Positioned above white panel but below character
        if (showDialog) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(0.8f)
                    .padding(bottom = 280.dp) // Adjust this to position above white panel
                    .offset(y=-50.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0x4B000000))
                    .alpha(dialogAlpha),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = dialogText,
                    color = Color.White,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        // White Panel
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .heightIn(min = 250.dp, max = 400.dp)
                .verticalScroll(rememberScrollState())
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .background(Color.White)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Plant Status
            Text(
                text = plantStatus,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            // Step Counter
            Text(
                text = "Steps: $steps",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black
            )

            // Item Selection Dropdown
            Box(modifier = Modifier.fillMaxWidth()) {
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
                        colors = TextFieldDefaults.textFieldColors(
                            unfocusedTextColor = Color.Black,
                            focusedTextColor = Color.Black,
                            containerColor = Color(0xFFF5F5F5),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )

                    ExposedDropdownMenu(
                        expanded = dropdownExpanded,
                        onDismissRequest = { dropdownExpanded = false },
                        modifier = Modifier.background(Color(0xFF3700B3))
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
                                            color = Color.White
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
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6200EE),
                    contentColor = Color.White
                )
            ) {
                Text("Feed Selected Item")
            }

            // Shop Button
            Button(
                onClick = { showShopDialog = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF03DAC6),
                    contentColor = Color.Black
                )
            ) {
                Text("Open Shop")
            }
        }

        // Shop Dialog
        if (showShopDialog) {
            AlertDialog(
                onDismissRequest = { showShopDialog = false },
                title = { Text("Shop - Steps: $steps", color = Color.White) },
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
                    Button(
                        onClick = { showShopDialog = false },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFBB86FC),
                            contentColor = Color.White
                        )
                    ) {
                        Text("Close Shop")
                    }
                },
                containerColor = Color(0xFF3700B3),
                titleContentColor = Color.White,
                textContentColor = Color.White
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
            containerColor = if (enabled) Color(0xFFBB86FC) else Color(0xFF3700B3),
            contentColor = if (enabled) Color.White else Color.White.copy(alpha = 0.5f)
        )
    ) {
        Text(
            "$itemName - $price steps",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = if (enabled) FontWeight.Bold else FontWeight.Normal
            )
        )
    }
}