package com.yourname.plantgame;

import android.content.Context;  // Needed for Android system operations
import android.content.SharedPreferences;  // Android's simple storage system

public class GameManager {
    private Player player;
    private PlantGirl plantGirl;
    private Shop shop;
    private static GameManager instance;

    // Private constructor for singleton pattern
    private GameManager() {
        this.player = new Player("Player1");
        this.plantGirl = new PlantGirl();
        this.shop = new Shop();
    }

    // Singleton access
    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    // === Save/Load System (Using SharedPreferences) ===

    /**
     * Saves game data to Android's persistent storage
     * @param context - Needed to access storage (pass your Activity/AppContext)
     */
    public void saveGame(Context context) {
        // 1. Get SharedPreferences file handler
        SharedPreferences prefs = context.getSharedPreferences(
                "GameData",  // File name
                Context.MODE_PRIVATE  // Access mode
        );

        // 2. Start editing
        SharedPreferences.Editor editor = prefs.edit();

        // 3. Store values
        editor.putInt("player_steps", player.getTotalSteps());
        editor.putInt("plant_affection", plantGirl.getAffection());
        editor.putInt("water_count", player.getWater());

        // 4. Save changes (async)
        editor.apply();  // Or use commit() for immediate saving
    }

    /**
     * Loads saved game data
     * @param context - Needed to access storage
     */
    public void loadGame(Context context) {
        // 1. Get SharedPreferences file handler
        SharedPreferences prefs = context.getSharedPreferences(
                "GameData",
                Context.MODE_PRIVATE
        );

        // 2. Load values (with defaults if not found)
        player.addSteps(prefs.getInt("player_steps", 0));
        plantGirl.setAffection(prefs.getInt("plant_affection", 10));
        player.setWater(prefs.getInt("water_count", 3));
    }

    // === Getters ===
    public Player getPlayer() { return player; }
    public PlantGirl getPlantGirl() { return plantGirl; }
    public Shop getShop() { return shop; }
}