package com.yourname.plantgame;

public class PlantGirl {
    private int affection;  // 0-100
    private int growthStage; // stages of tree growth
    private long lastCareTime;

    public PlantGirl() {
        this.affection = 10;  // Starting affection
        this.lastCareTime = System.currentTimeMillis();
        updateGrowthStage();   // Set initial growth stage
    }

    // Update plant state (call daily)
    public void update() {
        long hoursSinceCare = (System.currentTimeMillis() - lastCareTime) / (1000 * 60 * 60);

        if (hoursSinceCare > 24) {
            affection = Math.max(0, affection - 5); // Normal decay
        }

        updateGrowthStage();
    }

    // Care actions
    public void giveWater() { careAction(10); }
    public void giveSoil() { careAction(8); }
    public void giveCake() { careAction(15); }

    private void careAction(int points) {
        affection = Math.min(100, affection + points);
        lastCareTime = System.currentTimeMillis();
        updateGrowthStage();
    }

    // Growth stage calculation
    private void updateGrowthStage() {
        if (affection >= 60) growthStage = 3;
        else if (affection >= 30) growthStage = 2;
        else growthStage = 1;
    }

    // Getters
    public int getAffection() { return affection; }
    public int getGrowthStage() { return growthStage; }
    public long getLastCareTime() { return lastCareTime; }

    // Setters for game saving
    public void setAffection(int affection) {
        this.affection = Math.max(0, Math.min(100, affection));
        updateGrowthStage();
    }

    public void setLastCareTime(long timestamp) {
        this.lastCareTime = timestamp;
        update(); // Recalculate state after loading
    }

    // Status description
    public String getStatus() {
        return new String[]{"", "Seedling", "Sapling", "Blooming"}[growthStage];
    }
}