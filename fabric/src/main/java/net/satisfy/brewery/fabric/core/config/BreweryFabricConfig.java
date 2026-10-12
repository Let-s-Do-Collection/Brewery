package net.satisfy.brewery.fabric.core.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "brewery")
@Config.Gui.Background("brewery:textures/block/barrel_top.png")
public class BreweryFabricConfig implements ConfigData {
    @Override
    public void validatePostLoad() {
        food.sausageSaturation = clampSaturation(food.sausageSaturation);
        food.pretzelSaturation = clampSaturation(food.pretzelSaturation);
        food.porkKnuckleSaturation = clampSaturation(food.porkKnuckleSaturation);
        food.friedChickenSaturation = clampSaturation(food.friedChickenSaturation);
        food.halfChickenSaturation = clampSaturation(food.halfChickenSaturation);
        food.mashedPotatoesSaturation = clampSaturation(food.mashedPotatoesSaturation);
        food.potatoSaladSaturation = clampSaturation(food.potatoSaladSaturation);
        food.dumplingsSaturation = clampSaturation(food.dumplingsSaturation);
    }

    private static float clampSaturation(float value) {
        return Math.max(0.0f, Math.min(2.0f, value));
    }


    @ConfigEntry.Gui.CollapsibleObject
    public EffectsSettings effects = new EffectsSettings();

    @ConfigEntry.Gui.CollapsibleObject
    public BrewingSettings brewing = new BrewingSettings();

    @ConfigEntry.Gui.CollapsibleObject
    public DrunkennessSettings drunkenness = new DrunkennessSettings();

    @ConfigEntry.Gui.CollapsibleObject
    public InfoTooltipSettings infoTooltips = new InfoTooltipSettings();

    @ConfigEntry.Gui.CollapsibleObject
    public FoodSettings food = new FoodSettings();

    public static class BrewingSettings {
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 5, max = 600)
        public int brewTime = 60;

        @ConfigEntry.Gui.Tooltip
        public boolean enableBrewEvents = true;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 120)
        public int minBrewEventInterval = 5;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 120)
        public int maxBrewEventInterval = 15;

        @ConfigEntry.Gui.Tooltip
        public boolean enableBeerElementals = true;

        @ConfigEntry.Gui.Tooltip
        public boolean animations = true;
    }

    public static class DrunkennessSettings {
        @ConfigEntry.Gui.Tooltip
        public boolean enableDrunkenness = true;

        @ConfigEntry.Gui.Tooltip
        public boolean enableDrunkSway = true;

        @ConfigEntry.Gui.Tooltip
        public boolean enableDrunkSlowness = true;

        @ConfigEntry.Gui.Tooltip
        public boolean enableBlackout = true;

        @ConfigEntry.Gui.Tooltip
        public double blackoutChance = 0.15;

        @ConfigEntry.Gui.Tooltip
        public boolean enableBlackoutTeleport = true;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
        public int blackoutTeleportRange = 30;
    }

    public static class InfoTooltipSettings {
        @ConfigEntry.Gui.Tooltip
        public boolean showBrewingstationInfo = true;

        @ConfigEntry.Gui.Tooltip
        public boolean showBigBarrelInfo = true;

        @ConfigEntry.Gui.Tooltip
        public boolean needDungarees = false;
    }

    public static class EffectsSettings {
        @ConfigEntry.Gui.CollapsibleObject
        public CombustionEffectSettings combustionEffect = new CombustionEffectSettings();

        @ConfigEntry.Gui.CollapsibleObject
        public RepulsionEffectSettings repulsionEffect = new RepulsionEffectSettings();

        @ConfigEntry.Gui.CollapsibleObject
        public StoutHeartEffectSettings stoutHeartEffect = new StoutHeartEffectSettings();

        @ConfigEntry.Gui.CollapsibleObject
        public MiningEffectSettings miningEffect = new MiningEffectSettings();

        @ConfigEntry.Gui.CollapsibleObject
        public PacifyEffectSettings pacifyEffect = new PacifyEffectSettings();

        @ConfigEntry.Gui.CollapsibleObject
        public HaleyEffectSettings haleyEffect = new HaleyEffectSettings();

        public static class CombustionEffectSettings {
            @ConfigEntry.Gui.Tooltip
            @ConfigEntry.BoundedDiscrete(min = 1, max = 16)
            public int combustionEffectRadius = 4;

            @ConfigEntry.Gui.Tooltip
            @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
            public int combustionEffectIgniteChance = 2;
        }

        public static class RepulsionEffectSettings {
            @ConfigEntry.Gui.Tooltip
            @ConfigEntry.BoundedDiscrete(min = 1, max = 16)
            public int repulsionEffectRadius = 4;

            @ConfigEntry.Gui.Tooltip
            public double repulsionEffectStrength = 0.2;
        }

        public static class StoutHeartEffectSettings {
            @ConfigEntry.Gui.Tooltip
            @ConfigEntry.BoundedDiscrete(min = 1, max = 100)
            public int stoutHeartEffectHealthCap = 75;

            @ConfigEntry.Gui.Tooltip
            public double stoutHeartEffectHealAmount = 0.2;
        }

        public static class MiningEffectSettings {
            @ConfigEntry.Gui.Tooltip
            @ConfigEntry.BoundedDiscrete(min = -64, max = 320)
            public int miningEffectHasteOneHeight = 50;

            @ConfigEntry.Gui.Tooltip
            @ConfigEntry.BoundedDiscrete(min = -64, max = 320)
            public int miningEffectHasteTwoHeight = 30;

            @ConfigEntry.Gui.Tooltip
            @ConfigEntry.BoundedDiscrete(min = -64, max = 320)
            public int miningEffectHasteThreeHeight = 0;

            @ConfigEntry.Gui.Tooltip
            @ConfigEntry.BoundedDiscrete(min = -64, max = 320)
            public int miningEffectHasteFourHeight = -20;
        }

        public static class PacifyEffectSettings {
            @ConfigEntry.Gui.Tooltip
            @ConfigEntry.BoundedDiscrete(min = 1, max = 64)
            public int pacifyEffectRange = 32;
        }

        public static class HaleyEffectSettings {
            @ConfigEntry.Gui.Tooltip
            public boolean haleyEffectFlight = true;
        }
    }

    public static class FoodSettings {
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 20)
        public int sausageNutrition = 12;
        @ConfigEntry.Gui.Tooltip
        public float sausageSaturation = 0.9f;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 20)
        public int pretzelNutrition = 3;
        @ConfigEntry.Gui.Tooltip
        public float pretzelSaturation = 0.4f;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 20)
        public int porkKnuckleNutrition = 4;
        @ConfigEntry.Gui.Tooltip
        public float porkKnuckleSaturation = 0.7f;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 20)
        public int friedChickenNutrition = 3;
        @ConfigEntry.Gui.Tooltip
        public float friedChickenSaturation = 0.5f;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 20)
        public int halfChickenNutrition = 2;
        @ConfigEntry.Gui.Tooltip
        public float halfChickenSaturation = 0.6f;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 20)
        public int mashedPotatoesNutrition = 2;
        @ConfigEntry.Gui.Tooltip
        public float mashedPotatoesSaturation = 0.5f;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 20)
        public int potatoSaladNutrition = 2;
        @ConfigEntry.Gui.Tooltip
        public float potatoSaladSaturation = 0.5f;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 20)
        public int dumplingsNutrition = 3;
        @ConfigEntry.Gui.Tooltip
        public float dumplingsSaturation = 0.5f;
    }
}
