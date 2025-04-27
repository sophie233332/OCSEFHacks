package com.yourname.plantgame;

import com.yourname.plantgame.helperclass.Random2DArray;

public class PlantGirl {
    private final static Random2DArray<String> responses = new Random2DArray<>(new String[][]{
        /*facts*/{
            "Did you know my family has been around for over 200 million years? We were hanging out with dinosaurs before it was cool!",
            "My leaves are pretty unique - see these fan shapes? Once you see a Ginkgo leaf, you never forget it! We're pretty memorable that way.",
            "By the ancient roots! Did you know I'm the only one of my kind? There are about 350,000 species of flowering plants, but just one species of Ginkgo.",
            "Let me tell you a secret - my seeds might smell a bit... well, stinky, but they're super nutritious! Humans have been using parts of Ginkgo trees in medicine for centuries.",
            "People use my leaves to make extracts that help with memory and circulation. I'm basically brain food! Pretty leafy, right?",
            "Ginkgo trees are survivors! We've been around since before the dinosaurs and have barely changed in over 200 million years.",
            "In autumn, my relatives' leaves turn a beautiful golden color all at once! We don't drop our leaves one by one - we go out in a grand finale!",
            "There are Ginkgo trees in China that are over 3,000 years old! Can you imagine all the history they've witnessed?",
            "Ginkgo trees are often planted in cities because we're super resilient to pollution and urban conditions. We're tough cookies!",
            "My name 'Ginkgo' comes from the Japanese 'ginkyo', though I originally come from China. I've been traveling the world for centuries!"
        },
        /*jokes*/{
            "Want to hear a joke that's been fermenting for a few million years? What did the Ginkgo say when it fell? 'I'm a little fan of this situation!' Get it? Because our leaves are fan-shaped!",
            "Oh my chlorophyll! Why don't Ginkgo trees ever get lost? Because we've been around for 200 million years—we know our way around!",
            "What's a Ginkgo's favorite type of math? Geome-tree! Though I'm more of a calcu-leaf-tor myself. Sorry, my jokes are a bit seedy sometimes!",
            "Some trees drop apples, some drop chestnuts. My family? We drop stink bombs! But hey, it's all part of our charm.",
            "Why did the Ginkgo leaf go to therapy? It had too many issues with its roots!",
            "I've been around since the dinosaurs, so you could say I'm ex-tree-mely old!",
            "What's a Ginkgo's favorite music? Something with good root-hms!",
            "How do Ginkgo trees communicate? They use tree-mail! Though these days, I prefer in-tree-net!",
            "Why don't Ginkgo trees get invited to parties? Because they're always dropping seeds that raise a stink!",
            "What do you call a Ginkgo that's good at dancing? A tree with all the right mooves!"
        },
        /*wanderlust*/{
            "Sometimes I press my leaves against the glass and wonder what the wind feels like out there. Real wind, not the little fans they have in here to keep my leaves healthy.",
            "Did you know there are forests in China where some of my oldest relatives live? I dream of visiting them someday, exchanging stories of all we've seen.",
            "My ancestors traveled across continents! Their seeds were carried by wind and water and animals. Surely I can find a way to explore beyond these walls too.",
            "The gray cloud keeps me in this shelter. That's what my caretakers call the pollution out there. They say it's not safe for a young Ginkgo like me.",
            "But you know what? Ginkgos are survivors! We lived through the extinction of the dinosaurs, through ice ages and meteor strikes. A little pollution? I bet I could help clean it up if they'd let me try!",
            "Sometimes I sense when the rain is clean or dirty. The sickness affects my distant cousins out there - the trees in cities, the plants struggling to grow.",
            "Humans caused this problem, but I believe humans and plants together can fix it too. We just need to work as a team!",
            "My roots may be here, but my heart wants to wander where other plants grow.",
            "The shelter keeps me safe, but a tree isn't meant to live in a bubble forever.",
            "Sometimes I imagine what it would be like to plant my roots in wild soil, not this protected garden."
        },
    });
    private final static Random2DArray<String> greetings=new Random2DArray<>(new String[][]{
            /*greetings*/{
            "Hello there, seedling! I'm Ginku, the last of the ancient Ginkgo spirits.",
            "Oh! A visitor! It gets lonely in this shelter sometimes. I'm so happy to see you!",
            "Welcome, friend! Care to chat with a tree that's older than your entire civilization?",
            "Greetings, human seedling! I'm Ginku, at your service!",
            "Well hello there! Did the wind blow you my way? I'm Ginku!"
        }
    });

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
    public String getGreeting(){
        return greetings.eliminateRandom();
    }
    public String getRandomResponse(){
        return responses.eliminateRandom();
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