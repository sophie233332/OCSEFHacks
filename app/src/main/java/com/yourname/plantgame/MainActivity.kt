// Add this new file: ui/Theme.kt
package com.yourname.plantgame.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun PlantGameTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme.copy(
            primary = Color(0xFF4CAF50),  // Green
            secondary = Color(0xFF8BC34A) // Light green
        ),
        content = content
    )
}