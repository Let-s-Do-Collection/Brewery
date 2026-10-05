package net.satisfy.brewery.platform.fabric;

import me.shedaniel.autoconfig.AutoConfig;
import net.satisfy.brewery.fabric.core.config.BreweryFabricConfig;

public class PlatformHelperImpl {
    private static BreweryFabricConfig config() {
        return AutoConfig.getConfigHolder(BreweryFabricConfig.class).getConfig();
    }

    public static int getBrewTime() {
        return config().brewing.brewTime;
    }

    public static boolean isBrewEventsEnabled() {
        return config().brewing.enableBrewEvents;
    }

    public static int getMinBrewEventInterval() {
        return config().brewing.minBrewEventInterval;
    }

    public static int getMaxBrewEventInterval() {
        return config().brewing.maxBrewEventInterval;
    }

    public static boolean isBeerElementalsEnabled() {
        return config().brewing.enableBeerElementals;
    }

    public static boolean animationsEnabled() {
        return config().brewing.animations;
    }

    public static boolean isDrunkennessEnabled() {
        return config().drunkenness.enableDrunkenness;
    }

    public static boolean isDrunkSwayEnabled() {
        return config().drunkenness.enableDrunkSway;
    }

    public static boolean isDrunkSlownessEnabled() {
        return config().drunkenness.enableDrunkSlowness;
    }

    public static boolean isBlackoutEnabled() {
        return config().drunkenness.enableBlackout;
    }

    public static double getBlackoutChance() {
        return config().drunkenness.blackoutChance;
    }

    public static boolean isBlackoutTeleportEnabled() {
        return config().drunkenness.enableBlackoutTeleport;
    }

    public static int getBlackoutTeleportRange() {
        return config().drunkenness.blackoutTeleportRange;
    }

    public static int getCombustionEffectRadius() {
        return config().effects.combustionEffect.combustionEffectRadius;
    }

    public static int getCombustionEffectIgniteChance() {
        return config().effects.combustionEffect.combustionEffectIgniteChance;
    }

    public static int getRepulsionEffectRadius() {
        return config().effects.repulsionEffect.repulsionEffectRadius;
    }

    public static double getRepulsionEffectStrength() {
        return config().effects.repulsionEffect.repulsionEffectStrength;
    }

    public static int getStoutHeartEffectHealthCap() {
        return config().effects.stoutHeartEffect.stoutHeartEffectHealthCap;
    }

    public static double getStoutHeartEffectHealAmount() {
        return config().effects.stoutHeartEffect.stoutHeartEffectHealAmount;
    }

    public static int getMiningEffectHasteOneHeight() {
        return config().effects.miningEffect.miningEffectHasteOneHeight;
    }

    public static int getMiningEffectHasteTwoHeight() {
        return config().effects.miningEffect.miningEffectHasteTwoHeight;
    }

    public static int getMiningEffectHasteThreeHeight() {
        return config().effects.miningEffect.miningEffectHasteThreeHeight;
    }

    public static int getMiningEffectHasteFourHeight() {
        return config().effects.miningEffect.miningEffectHasteFourHeight;
    }

    public static int getPacifyEffectRange() {
        return config().effects.pacifyEffect.pacifyEffectRange;
    }

    public static boolean isHaleyEffectFlightEnabled() {
        return config().effects.haleyEffect.haleyEffectFlight;
    }

    public static boolean showBrewingstationInfo() {
        return config().infoTooltips.showBrewingstationInfo;
    }

    public static boolean infoTooltipsNeedDungarees() {
        return config().infoTooltips.needDungarees;
    }

    public static int getNutrition(String itemName) {
        BreweryFabricConfig.FoodSettings food = config().food;
        return switch (itemName) {
            case "sausage" -> food.sausageNutrition;
            case "pretzel" -> food.pretzelNutrition;
            case "pork_knuckle" -> food.porkKnuckleNutrition;
            case "fried_chicken" -> food.friedChickenNutrition;
            case "half_chicken" -> food.halfChickenNutrition;
            case "mashed_potatoes" -> food.mashedPotatoesNutrition;
            case "potato_salad" -> food.potatoSaladNutrition;
            case "dumplings" -> food.dumplingsNutrition;
            default -> 0;
        };
    }

    public static float getSaturationMod(String itemName) {
        BreweryFabricConfig.FoodSettings food = config().food;
        return switch (itemName) {
            case "sausage" -> food.sausageSaturationMod;
            case "pretzel" -> food.pretzelSaturationMod;
            case "pork_knuckle" -> food.porkKnuckleSaturationMod;
            case "fried_chicken" -> food.friedChickenSaturationMod;
            case "half_chicken" -> food.halfChickenSaturationMod;
            case "mashed_potatoes" -> food.mashedPotatoesSaturationMod;
            case "potato_salad" -> food.potatoSaladSaturationMod;
            case "dumplings" -> food.dumplingsSaturationMod;
            default -> 0;
        };
    }
}
