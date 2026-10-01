package net.satisfy.brewery.fabric.core.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "brewery")
@Config.Gui.Background("brewery:textures/block/barrel_top.png")
public class BreweryFabricConfig implements ConfigData {

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
        public int sausageNutrition = 6;
        public float sausageSaturationMod = 0.5f;
        public int pretzelNutrition = 3;
        public float pretzelSaturationMod = 0.4f;
        public int porkKnuckleNutrition = 6;
        public float porkKnuckleSaturationMod = 0.6f;
        public int friedChickenNutrition = 6;
        public float friedChickenSaturationMod = 0.6f;
        public int halfChickenNutrition = 6;
        public float halfChickenSaturationMod = 0.6f;
        public int mashedPotatoesNutrition = 3;
        public float mashedPotatoesSaturationMod = 0.5f;
        public int potatoSaladNutrition = 6;
        public float potatoSaladSaturationMod = 0.7f;
        public int dumplingsNutrition = 6;
        public float dumplingsSaturationMod = 0.5f;
    }
}
