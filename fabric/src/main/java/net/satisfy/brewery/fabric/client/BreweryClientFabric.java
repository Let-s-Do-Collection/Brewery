package net.satisfy.brewery.fabric.client;

import net.satisfy.foundation.fabric.client.FoundationArmorRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.satisfy.brewery.client.BreweryClient;
import net.satisfy.brewery.core.item.BreathalyzerItem;
import net.satisfy.brewery.core.registry.ObjectRegistry;

public class BreweryClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BreweryClient.preInitClient();
        BreathalyzerItem.init();
        BreweryClient.onInitializeClient();

        ArmorRenderer.register(FoundationArmorRenderer.INSTANCE, ObjectRegistry.BREWFEST_HAT.get(), ObjectRegistry.BREWFEST_HAT_RED.get(), ObjectRegistry.BREWFEST_REGALIA.get(), ObjectRegistry.BREWFEST_BLOUSE.get(),
                ObjectRegistry.BREWFEST_TROUSERS.get(), ObjectRegistry.BREWFEST_DRESS.get(), ObjectRegistry.BREWFEST_BOOTS.get(), ObjectRegistry.BREWFEST_SHOES.get());
    }
}