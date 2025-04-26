package com.yourname.plantgame;

import java.util.Calendar;

public class Player {
    private String name;
    private int totalSteps;
    private int todaySteps;
    private int water;
    private int soil;
    private int cakes;
    private long lastUpdateTime;

    public Player(String name) {
        this.name = name;
        this.water = 3;  // Starting water
        this.soil = 2;   // Starting soil
        this.cakes = 0;   // No cakes initially
        this.lastUpdateTime = System.currentTimeMillis();
    }

    // Step management
    public void addSteps(int steps) {
        if (isNewDay()) resetDailySteps();
        totalSteps = Math.max(0, totalSteps + steps);
        todaySteps = Math.max(0, todaySteps + steps);
        lastUpdateTime = System.currentTimeMillis();
    }

    private boolean isNewDay() {
        Calendar today = Calendar.getInstance();
        Calendar lastUpdate = Calendar.getInstance();
        lastUpdate.setTimeInMillis(lastUpdateTime);
        return today.get(Calendar.DAY_OF_YEAR) != lastUpdate.get(Calendar.DAY_OF_YEAR);
    }

    private void resetDailySteps() {
        todaySteps = 0;
    }

    // Inventory usage
    public boolean useWater() {
        if (water > 0) {
            water--;
            return true;
        }
        return false;
    }

    public boolean useSoil() {
        if (soil > 0) {
            soil--;
            return true;
        }
        return false;
    }

    public boolean useCake() {
        if (cakes > 0) {
            cakes--;
            return true;
        }
        return false;
    }

    // Getters
    public int getTotalSteps() { return totalSteps; }
    public int getTodaySteps() { return todaySteps; }
    public int getWater() { return water; }
    public int getSoil() { return soil; }
    public int getCakes() { return cakes; }

    // Setters for game saving
    public void setWater(int amount) {
        this.water = Math.max(0, amount);
    }

    public void setSoil(int amount) {
        this.soil = Math.max(0, amount);
    }

    public void setCakes(int amount) {
        this.cakes = Math.max(0, amount);
    }

    public void setSteps(int totalSteps) {
        this.totalSteps = Math.max(0, totalSteps);
        this.todaySteps = 0; // Reset daily count when loading
    }
}