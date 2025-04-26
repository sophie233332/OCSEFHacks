package com.yourname.plantgame;

public class PlantGirl {
    private int affection;  // 0-100
    private int growthStage; // 0=Seedling, 1=Growing, 2=Blooming
    private long lastCareTime;
    private boolean isWilting;

    public PlantGirl() {
        this.affection = 10;  // Starting affection
        this.lastCareTime = System.currentTimeMillis();
        updateGrowthStage();   // Set initial growth stage
    }

    // Update plant state (call daily)
    public void update() {
        long hoursSinceCare = (System.currentTimeMillis() - lastCareTime) / (1000 * 60 * 60);

        if (hoursSinceCare > 48) {
            isWilting = true;
            affection = Math.max(0, affection - 10); // Rapid decay when wilting
        }
        else if (hoursSinceCare > 24) {
            affection = Math.max(0, affection - 5); // Normal decay
        }

        updateGrowthStage();
    }

    // Care actions
    public void giveWater() { careAction(5); }
    public void giveSoil() { careAction(8); }
    public void giveCake() { careAction(15); }

    private void careAction(int points) {
        affection = Math.min(100, affection + points);
        lastCareTime = System.currentTimeMillis();
        isWilting = false;
        updateGrowthStage();
    }

    // Growth stage calculation
    private void updateGrowthStage() {
        if (affection >= 60) growthStage = 2;
        else if (affection >= 30) growthStage = 1;
        else growthStage = 0;
    }

    // Getters
    public int getAffection() { return affection; }
    public int getGrowthStage() { return growthStage; }
    public boolean isWilting() { return isWilting; }
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

    public void forceWilting(boolean isWilting) {
        this.isWilting = isWilting;
        if (isWilting) {
            affection = Math.min(20, affection); // Cap affection when wilting
        }
    }

    // Status description
    public String getStatus() {
        if (isWilting) return "Wilting - Needs Care!";
        return new String[]{"Seedling", "Growing", "Blooming"}[growthStage];
    }
}