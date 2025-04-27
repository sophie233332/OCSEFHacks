package com.yourname.plantgame;

import java.util.EnumMap;
import java.util.Map;

public class Shop {
    private static EnumMap<Item, Integer> PRICES=new EnumMap<>(Item.class);
    static {
        PRICES.put(Item.water, 500);
        PRICES.put(Item.soil, 800);
        PRICES.put(Item.cake, 1500);
    }
    public boolean buyItem(Player player, Item item) {
        if (player.getTotalSteps() >= PRICES.get(item)) {
            switch (item) {
                case water: player.useWater(); break;
                case soil: player.useSoil(); break;
                case cake: player.useCake(); break;
            }
            player.addSteps(-PRICES.get(item));
            return true;
        }
        return false;
    }

    public int getPrice(Item itemType) {
        return PRICES.get(itemType);
    }
}