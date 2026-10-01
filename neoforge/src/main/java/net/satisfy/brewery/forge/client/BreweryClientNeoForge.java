package net.satisfy.brewery.forge.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.satisfy.brewery.Brewery;
import net.satisfy.brewery.client.BreweryClient;
import net.satisfy.brewery.core.item.BreathalyzerItem;
import net.satisfy.brewery.core.registry.ObjectRegistry;
import net.satisfy.brewery.forge.client.extensions.BrewfestBootsExtensions;
import net.satisfy.brewery.forge.client.extensions.BrewfestChestplateExtensions;
import net.satisfy.brewery.forge.client.extensions.BrewfestHatExtensions;
import net.satisfy.brewery.forge.client.extensions.BrewfestLeggingsExtensions;

@EventBusSubscriber(modid = Brewery.MOD_ID, value = Dist.CLIENT)
public class BreweryClientNeoForge {

    @SubscribeEvent
    public static void beforeClientSetup(RegisterEvent event) {
        BreweryClient.preInitClient();
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            BreathalyzerItem.init();
            BreweryClient.onInitializeClient();
        });
    }

    @SubscribeEvent
    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerItem(new BrewfestHatExtensions(), ObjectRegistry.BREWFEST_HAT.get(), ObjectRegistry.BREWFEST_HAT_RED.get());
        event.registerItem(new BrewfestChestplateExtensions(), ObjectRegistry.BREWFEST_REGALIA.get(), ObjectRegistry.BREWFEST_BLOUSE.get());
        event.registerItem(new BrewfestLeggingsExtensions(), ObjectRegistry.BREWFEST_TROUSERS.get(), ObjectRegistry.BREWFEST_DRESS.get());
        event.registerItem(new BrewfestBootsExtensions(), ObjectRegistry.BREWFEST_BOOTS.get(), ObjectRegistry.BREWFEST_SHOES.get());
    }
}