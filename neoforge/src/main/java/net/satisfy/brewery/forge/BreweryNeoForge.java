package net.satisfy.brewery.forge;

import dev.architectury.platform.hooks.EventBusesHooks;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackSelectionConfig;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;
import net.satisfy.brewery.Brewery;
import net.satisfy.brewery.core.registry.CompostablesRegistry;
import net.satisfy.brewery.core.registry.ObjectRegistry;
import net.satisfy.brewery.forge.core.config.BreweryNeoForgeConfig;
import net.satisfy.brewery.platform.neoforge.PlatformHelperImpl;
import org.jetbrains.annotations.Nullable;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Mod(Brewery.MOD_ID)
public class BreweryNeoForge {

    public BreweryNeoForge(final IEventBus modEventBus, final ModContainer modContainer) {
        EventBusesHooks.whenAvailable(Brewery.MOD_ID, IEventBus::start);
        Brewery.init();
        modContainer.registerConfig(ModConfig.Type.COMMON, BreweryNeoForgeConfig.COMMON_CONFIG);
        modEventBus.addListener(BreweryNeoForgeConfig::onLoad);
        modEventBus.addListener(BreweryNeoForgeConfig::onReload);
        modEventBus.addListener(BreweryNeoForge::reapplyFoodConfig);
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(BreweryNeoForge::addBuiltinPacks);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(CompostablesRegistry::init);
    }

    private static void addBuiltinPacks(AddPackFindersEvent event) {
        if (event.getPackType() != PackType.CLIENT_RESOURCES) return;
        Path root = findBuiltinPack("vanilla_blend");
        if (root == null) return;
        PackLocationInfo info = new PackLocationInfo("mod/" + Brewery.MOD_ID + ":vanilla_blend",
                Component.translatable("pack.brewery.vanilla_blend"), PackSource.BUILT_IN, Optional.empty());
        Pack pack = Pack.readMetaAndCreate(info, new PathPackResources.PathResourcesSupplier(root),
                PackType.CLIENT_RESOURCES, new PackSelectionConfig(false, Pack.Position.TOP, false));
        if (pack != null) {
            event.addRepositorySource(consumer -> consumer.accept(pack));
        }
    }

    /**
     * Built-in packs live in the common module. In production they are shadowed into the mod jar, but in
     * the dev environment the common resources are a separate classpath entry, so fall back to the classpath.
     */
    private static @Nullable Path findBuiltinPack(String name) {
        Path modPath = ModList.get().getModFileById(Brewery.MOD_ID).getFile().findResource("resourcepacks", name);
        if (Files.exists(modPath.resolve("pack.mcmeta"))) return modPath;
        try {
            URL url = BreweryNeoForge.class.getResource("/resourcepacks/" + name + "/pack.mcmeta");
            if (url != null && "file".equals(url.getProtocol())) return Path.of(url.toURI()).getParent();
        } catch (URISyntaxException ignored) {
        }
        return null;
    }

    private static void reapplyFoodConfig(ModifyDefaultComponentsEvent event) {
        patchFood(event, ObjectRegistry.SAUSAGE, "sausage");
        patchFood(event, ObjectRegistry.PRETZEL, "pretzel");
        patchFood(event, ObjectRegistry.PORK_KNUCKLE, "pork_knuckle");
        patchFood(event, ObjectRegistry.FRIED_CHICKEN, "fried_chicken");
        patchFood(event, ObjectRegistry.HALF_CHICKEN, "half_chicken");
        patchFood(event, ObjectRegistry.MASHED_POTATOES, "mashed_potatoes");
        patchFood(event, ObjectRegistry.POTATO_SALAD, "potato_salad");
        patchFood(event, ObjectRegistry.DUMPLINGS, "dumplings");
    }

    private static void patchFood(ModifyDefaultComponentsEvent event, RegistrySupplier<Item> itemSupplier, String key) {
        Item item = itemSupplier.get();
        FoodProperties current = item.components().get(DataComponents.FOOD);
        if (current == null) return;

        FoodProperties updated = new FoodProperties(
                PlatformHelperImpl.getNutrition(key),
                PlatformHelperImpl.getSaturationMod(key),
                current.canAlwaysEat(),
                current.eatSeconds(),
                current.usingConvertsTo(),
                current.effects()
        );
        event.modify(item, builder -> builder.set(DataComponents.FOOD, updated));
    }
}
