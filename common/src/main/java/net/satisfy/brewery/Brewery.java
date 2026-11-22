package net.satisfy.brewery;

import net.satisfy.brewery.core.registry.ArmorSetRegistry;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.registry.fuel.FuelRegistry;
import net.minecraft.resources.ResourceLocation;
import net.satisfy.brewery.core.event.CommonEvents;
import net.satisfy.brewery.core.event.PartyStarterEvent;
import net.satisfy.brewery.core.event.brew_event.BrewEvents;
import net.satisfy.brewery.core.network.BreweryNetworking;
import net.satisfy.brewery.core.registry.*;

import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.satisfy.foundation.rarity.FoundationRarities;
import net.satisfy.foundation.rarity.FoundationRarity;


import static net.satisfy.brewery.core.registry.ObjectRegistry.*;
import net.satisfy.foundation.entity.equipment.ZombieEquipment;
import net.minecraft.world.entity.EquipmentSlot;

public class Brewery {
    public static final String MOD_ID = "brewery";

    public static ResourceLocation identifier(String name) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
    }

    public static void init() {
        ZombieEquipment.add(0.03).adultsOnly().slot(EquipmentSlot.HEAD, ObjectRegistry.BREWFEST_HAT_RED).slot(EquipmentSlot.CHEST, ObjectRegistry.BREWFEST_REGALIA).slot(EquipmentSlot.MAINHAND, ObjectRegistry.WHISKEY_AK);
        CompostablesRegistry.init();
        MobEffectRegistry.init();
        ObjectRegistry.init();
        FlammableBlockRegistry.init();
        ArmorSetRegistry.init();
        EntityTypeRegistry.init();
        SoundEventRegistry.init();
        RecipeTypeRegistry.init();
        TabRegistry.init();

        LifecycleEvent.SETUP.register(Brewery::registerFuels);
        LifecycleEvent.SETUP.register(Brewery::registerRarities);

        BrewEvents.loadClass();
        CommonEvents.init();
        BreweryNetworking.init();
        registerEvents();
    }

    private static void registerEvents() {
        PartyStarterEvent partyStarterEvent = new PartyStarterEvent();
        PlayerEvent.ATTACK_ENTITY.register(partyStarterEvent);
    }

    private static void registerRarities() {
        registerRarity(FoundationRarity.COMMON,
                HOPS, DRIED_WHEAT, DRIED_BARLEY, DRIED_CORN, DRIED_OAT,
                BENCH, TABLE, PATTERNED_WOOL, PATTERNED_CARPET, CABINET, DRAWER, BAR_COUNTER, SIDEBOARD, WALL_CABINET, BEER_MUG);
        registerRarity(FoundationRarity.UNCOMMON,
                WILD_HOPS, SAUSAGE, PRETZEL, GINGERBREAD, DARK_BREW,
                BEER_WHEAT, BEER_HOPS, BEER_BARLEY, BEER_OAT,
                WOODEN_BREWINGSTATION, BREW_TIMER, BREW_WHISTLE, BARREL_MAIN, BREATHALYZER);
        registerRarity(FoundationRarity.RARE,
                BEER_NETTLE, BEER_HALEY,
                WHISKEY_MAGGOALLAN, WHISKEY_CARRASCONLABEL, WHISKEY_LILITUSINGLEMALT, WHISKEY_JOJANNIK, WHISKEY_CRISTELWALKER,
                MASHED_POTATOES, POTATO_SALAD, DUMPLINGS, HALF_CHICKEN,
                COPPER_BREWINGSTATION,
                BREWFEST_HAT, BREWFEST_HAT_RED, BREWFEST_REGALIA, BREWFEST_TROUSERS, BREWFEST_BOOTS, BREWFEST_DRESS, BREWFEST_BLOUSE, BREWFEST_SHOES);
        registerRarity(FoundationRarity.EPIC,
                WHISKEY_AK, WHISKEY_HIGHLAND_HEARTH, WHISKEY_JAMESONS_MALT, WHISKEY_SMOKEY_REVERIE,
                PORK_KNUCKLE, FRIED_CHICKEN, NETHERITE_BREWINGSTATION);
        registerRarity(FoundationRarity.LEGENDARY, BREWERY_BANNER);
        for (Item item : BuiltInRegistries.ITEM) {
            if (MOD_ID.equals(BuiltInRegistries.ITEM.getKey(item).getNamespace()) && FoundationRarities.get(new ItemStack(item)) == null) {
                FoundationRarities.register(item, FoundationRarity.COMMON);
            }
        }
    }

    @SafeVarargs
    private static void registerRarity(FoundationRarity rarity, RegistrySupplier<? extends ItemLike>... entries) {
        for (RegistrySupplier<? extends ItemLike> entry : entries) {
            FoundationRarities.register(entry.get(), rarity);
        }
    }

    public static void registerFuels() {
        FuelRegistry.register(300, BEER_MUG.get(), BENCH.get(), TABLE.get(), BAR_COUNTER.get(), WOODEN_BREWINGSTATION.get());
        FuelRegistry.register(100, HOPS.get());
        FuelRegistry.register(75, PATTERNED_WOOL.get(), PATTERNED_CARPET.get());
        FuelRegistry.register(50, BREWFEST_BOOTS.get(), BREWFEST_HAT.get(), BREWFEST_DRESS.get(), BREWFEST_REGALIA.get(), BREWFEST_TROUSERS.get());
    }
}
