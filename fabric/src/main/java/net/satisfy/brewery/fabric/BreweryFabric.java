package net.satisfy.brewery.fabric;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.satisfy.brewery.Brewery;
import net.satisfy.brewery.fabric.core.config.BreweryFabricConfig;
import net.satisfy.brewery.fabric.core.world.BreweryBiomeModification;

public class BreweryFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        AutoConfig.register(BreweryFabricConfig.class, GsonConfigSerializer::new);
        Brewery.init();
        BreweryBiomeModification.init();
        FabricLoader.getInstance().getModContainer(Brewery.MOD_ID).ifPresent(container ->
                ResourceManagerHelper.registerBuiltinResourcePack(
                        ResourceLocation.fromNamespaceAndPath(Brewery.MOD_ID, "vanilla_blend"),
                        container,
                        Component.translatable("pack.brewery.vanilla_blend"),
                        ResourcePackActivationType.NORMAL));
    }
}
