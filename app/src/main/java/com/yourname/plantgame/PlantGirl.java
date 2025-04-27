package com.yourname.plantgame;

public class PlantGirl {
    private int affection;  // 0-100
    private int growth;
    private int growthStage; // stages of tree growth
    private long lastCareTime;

    public PlantGirl() {
        this.affection = 10;  // Starting affection
        this.growth=0;
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
    public void giveWater() {
        growAction(10);
        careAction(10);
    }
    public void giveSoil() {
        growAction(8);
        careAction(8);
    }
    public void giveCake() {
        growAction(15);
        careAction(15);
    }

    private void growAction(int points){
        growth+=points;
        lastCareTime = System.currentTimeMillis();
        updateGrowthStage();
    }
    private void careAction(int points) {
        affection = Math.min(100, affection + points);
    }

    // Growth stage calculation
    private void updateGrowthStage() {
        if (growth >= 60) growthStage = 3;
        else if (growth >= 30) growthStage = 2;
        else growthStage = 1;
    }

    // Getters
    public int getGrowth(){ return growth; }
    public int getAffection() { return affection; }
    public int getGrowthStage() { return growthStage; }
    public long getLastCareTime() { return lastCareTime; }

    // Setters for game saving
    public void setAffection(int affection) {
        this.affection = Math.max(0, Math.min(100, affection));
    }
    public void setGrowth(int growth){
        this.growth=Math.max(0,growth);
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