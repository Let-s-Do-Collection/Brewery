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

import static net.satisfy.brewery.core.registry.ObjectRegistry.*;

public class Brewery {
    public static final String MOD_ID = "brewery";

    public static ResourceLocation identifier(String name) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
    }

    public static void init() {
        MobEffectRegistry.init();
        ObjectRegistry.init();
        ArmorSetRegistry.init();
        EntityTypeRegistry.init();
        SoundEventRegistry.init();
        RecipeTypeRegistry.init();
        TabRegistry.init();

        LifecycleEvent.SETUP.register(Brewery::registerFuels);

        BrewEvents.loadClass();
        CommonEvents.init();
        BreweryNetworking.init();
        registerEvents();
    }

    private static void registerEvents() {
        PartyStarterEvent partyStarterEvent = new PartyStarterEvent();
        PlayerEvent.ATTACK_ENTITY.register(partyStarterEvent);
    }

    public static void registerFuels() {
        FuelRegistry.register(300, BEER_MUG.get(), BENCH.get(), TABLE.get(), BAR_COUNTER.get(), WOODEN_BREWINGSTATION.get());
        FuelRegistry.register(100, HOPS.get());
        FuelRegistry.register(75, PATTERNED_WOOL.get(), PATTERNED_CARPET.get());
        FuelRegistry.register(50, BREWFEST_BOOTS.get(), BREWFEST_HAT.get(), BREWFEST_DRESS.get(), BREWFEST_REGALIA.get(), BREWFEST_TROUSERS.get());
    }
}
