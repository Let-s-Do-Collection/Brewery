package net.satisfy.brewery.platform.neoforge;

import net.satisfy.brewery.forge.core.config.BreweryNeoForgeConfig;

public class PlatformHelperImpl {
    public static int getBrewTime() {
        return BreweryNeoForgeConfig.brewTime;
    }

    public static boolean isBrewEventsEnabled() {
        return BreweryNeoForgeConfig.enableBrewEvents;
    }

    public static int getMinBrewEventInterval() {
        return BreweryNeoForgeConfig.minBrewEventInterval;
    }

    public static int getMaxBrewEventInterval() {
        return BreweryNeoForgeConfig.maxBrewEventInterval;
    }

    public static boolean isBeerElementalsEnabled() {
        return BreweryNeoForgeConfig.enableBeerElementals;
    }

    public static boolean isDrunkennessEnabled() {
        return BreweryNeoForgeConfig.enableDrunkenness;
    }

    public static boolean isDrunkSwayEnabled() {
        return BreweryNeoForgeConfig.enableDrunkSway;
    }

    public static boolean isDrunkSlownessEnabled() {
        return BreweryNeoForgeConfig.enableDrunkSlowness;
    }

    public static boolean isBlackoutEnabled() {
        return BreweryNeoForgeConfig.enableBlackout;
    }

    public static double getBlackoutChance() {
        return BreweryNeoForgeConfig.blackoutChance;
    }

    public static boolean isBlackoutTeleportEnabled() {
        return BreweryNeoForgeConfig.enableBlackoutTeleport;
    }

    public static int getBlackoutTeleportRange() {
        return BreweryNeoForgeConfig.blackoutTeleportRange;
    }

    public static int getCombustionEffectRadius() {
        return BreweryNeoForgeConfig.combustionEffectRadius;
    }

    public static int getCombustionEffectIgniteChance() {
        return BreweryNeoForgeConfig.combustionEffectIgniteChance;
    }

    public static int getRepulsionEffectRadius() {
        return BreweryNeoForgeConfig.repulsionEffectRadius;
    }

    public static double getRepulsionEffectStrength() {
        return BreweryNeoForgeConfig.repulsionEffectStrength;
    }

    public static int getStoutHeartEffectHealthCap() {
        return BreweryNeoForgeConfig.stoutHeartEffectHealthCap;
    }

    public static double getStoutHeartEffectHealAmount() {
        return BreweryNeoForgeConfig.stoutHeartEffectHealAmount;
    }

    public static int getMiningEffectHasteOneHeight() {
        return BreweryNeoForgeConfig.miningEffectHasteOneHeight;
    }

    public static int getMiningEffectHasteTwoHeight() {
        return BreweryNeoForgeConfig.miningEffectHasteTwoHeight;
    }

    public static int getMiningEffectHasteThreeHeight() {
        return BreweryNeoForgeConfig.miningEffectHasteThreeHeight;
    }

    public static int getMiningEffectHasteFourHeight() {
        return BreweryNeoForgeConfig.miningEffectHasteFourHeight;
    }

    public static int getPacifyEffectRange() {
        return BreweryNeoForgeConfig.pacifyEffectRange;
    }

    public static boolean isHaleyEffectFlightEnabled() {
        return BreweryNeoForgeConfig.haleyEffectFlight;
    }

    public static boolean showBrewingstationInfo() {
        return BreweryNeoForgeConfig.showBrewingstationInfo;
    }

    public static boolean infoTooltipsNeedDungarees() {
        return BreweryNeoForgeConfig.infoTooltipsNeedDungarees;
    }

    public static int getNutrition(String itemName) {
        return switch (itemName) {
            case "sausage" -> BreweryNeoForgeConfig.sausageNutrition;
            case "pretzel" -> BreweryNeoForgeConfig.pretzelNutrition;
            case "pork_knuckle" -> BreweryNeoForgeConfig.porkKnuckleNutrition;
            case "fried_chicken" -> BreweryNeoForgeConfig.friedChickenNutrition;
            case "half_chicken" -> BreweryNeoForgeConfig.halfChickenNutrition;
            case "mashed_potatoes" -> BreweryNeoForgeConfig.mashedPotatoesNutrition;
            case "potato_salad" -> BreweryNeoForgeConfig.potatoSaladNutrition;
            case "dumplings" -> BreweryNeoForgeConfig.dumplingsNutrition;
            default -> 0;
        };
    }

    public static float getSaturationMod(String itemName) {
        return switch (itemName) {
            case "sausage" -> (float) BreweryNeoForgeConfig.sausageSaturationMod;
            case "pretzel" -> (float) BreweryNeoForgeConfig.pretzelSaturationMod;
            case "pork_knuckle" -> (float) BreweryNeoForgeConfig.porkKnuckleSaturationMod;
            case "fried_chicken" -> (float) BreweryNeoForgeConfig.friedChickenSaturationMod;
            case "half_chicken" -> (float) BreweryNeoForgeConfig.halfChickenSaturationMod;
            case "mashed_potatoes" -> (float) BreweryNeoForgeConfig.mashedPotatoesSaturationMod;
            case "potato_salad" -> (float) BreweryNeoForgeConfig.potatoSaladSaturationMod;
            case "dumplings" -> (float) BreweryNeoForgeConfig.dumplingsSaturationMod;
            default -> 0;
        };
    }
}
