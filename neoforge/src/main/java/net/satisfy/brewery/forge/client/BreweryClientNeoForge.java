package net.satisfy.brewery.forge.client;

import net.satisfy.foundation.neoforge.client.FoundationArmorExtensions;
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
        event.registerItem(FoundationArmorExtensions.INSTANCE, ObjectRegistry.BREWFEST_HAT.get(), ObjectRegistry.BREWFEST_HAT_RED.get(), ObjectRegistry.BREWFEST_REGALIA.get(), ObjectRegistry.BREWFEST_BLOUSE.get(),
                ObjectRegistry.BREWFEST_TROUSERS.get(), ObjectRegistry.BREWFEST_DRESS.get(), ObjectRegistry.BREWFEST_BOOTS.get(), ObjectRegistry.BREWFEST_SHOES.get());
    }
}