package net.satisfy.brewery.client;

import dev.architectury.registry.client.level.entity.EntityModelLayerRegistry;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import dev.architectury.registry.client.rendering.ColorHandlerRegistry;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.satisfy.brewery.client.gui.overlay.BrewingstationInfoProvider;
import net.satisfy.brewery.client.gui.WallDecorationEditGui;
import net.satisfy.brewery.client.model.BeerElementalModel;
import net.satisfy.brewery.client.model.BrewfestBootsModel;
import net.satisfy.brewery.client.model.BrewfestChestplateModel;
import net.satisfy.brewery.client.model.BrewfestHatModel;
import net.satisfy.brewery.client.model.BrewfestLeggingsModel;
import net.satisfy.brewery.client.renderer.block.BeerMugRenderer;
import net.satisfy.brewery.client.renderer.block.BeverageRenderer;
import net.satisfy.brewery.client.renderer.block.BrewingstationRenderer;
import net.satisfy.brewery.client.renderer.block.CompletionistBannerRenderer;
import net.satisfy.foundation.storage.StorageBlockEntityRenderer;
import net.satisfy.brewery.client.renderer.block.WallDecorationBlockRenderer;
import net.satisfy.brewery.client.renderer.entity.BeerElementalAttackRenderer;
import net.satisfy.brewery.client.renderer.entity.BeerElementalRenderer;
import net.satisfy.brewery.core.block.entity.WallDecorationBlockEntity;
import net.satisfy.brewery.core.registry.EntityTypeRegistry;
import net.satisfy.brewery.core.registry.StorageTypeRegistry;

import static net.satisfy.brewery.core.registry.ObjectRegistry.*;
import net.satisfy.foundation.overlay.BlockInfoOverlay;

@Environment(EnvType.CLIENT)
public class BreweryClient {

    public static void onInitializeClient() {
        RenderTypeRegistry.register(RenderType.cutout(),
                WILD_HOPS.get(), BEER_MUG.get(), BEER_WHEAT.get(), BEER_HOPS.get(), BEER_BARLEY.get(), BEER_HALEY.get(), BEER_OAT.get(), BEER_NETTLE.get(),
                HOPS_CROP_BODY.get(), HOPS_CROP.get(), WHISKEY_MAGGOALLAN.get(), WHISKEY_CARRASCONLABEL.get(), WHISKEY_LILITUSINGLEMALT.get(),
                WHISKEY_JOJANNIK.get(), WHISKEY_MAGGOALLAN.get(), WHISKEY_CRISTELWALKER.get(), WHISKEY_AK.get(), WHISKEY_HIGHLAND_HEARTH.get(),
                WHISKEY_JAMESONS_MALT.get(), WHISKEY_SMOKEY_REVERIE.get(), BREWERY_BANNER.get(), BREWERY_WALL_BANNER.get()
        );

        ColorHandlerRegistry.registerBlockColors((state, world, pos, tintIndex) -> {
            if (world == null || pos == null) {
                return -1;
            }
            return BiomeColors.getAverageWaterColor(world, pos);
        }, WOODEN_BREWINGSTATION, COPPER_BREWINGSTATION, NETHERITE_BREWINGSTATION);

        BlockEntityRendererRegistry.register(EntityTypeRegistry.BREWERY_BANNER.get(), CompletionistBannerRenderer::new);
        BlockEntityRendererRegistry.register(EntityTypeRegistry.STORAGE_ENTITY.get(), context -> new StorageBlockEntityRenderer());
        BlockEntityRendererRegistry.register(EntityTypeRegistry.BEER_MUG_BLOCK_ENTITY.get(), BeerMugRenderer::new);
        BlockEntityRendererRegistry.register(EntityTypeRegistry.BREWINGSTATION_BLOCK_ENTITY.get(), BrewingstationRenderer::new);
        BlockEntityRendererRegistry.register(EntityTypeRegistry.WALL_DECORATION.get(), context -> new WallDecorationBlockRenderer());

        StorageBlockEntityRenderer.registerStorageType(StorageTypeRegistry.BEVERAGE, new BeverageRenderer());
    }

    public static void openStreetSignScreen(WallDecorationBlockEntity entity) {
        Minecraft.getInstance().setScreen(new WallDecorationEditGui(entity));
    }

    public static void preInitClient() {
        registerEntityRenderers();
        registerBlockInfo();
        registerEntityModelLayers();
    }

    private static void registerBlockInfo() {
        BlockInfoOverlay.init();
        BlockInfoOverlay.registerProvider(new BrewingstationInfoProvider());
    }

    private static void registerEntityRenderers() {
        EntityRendererRegistry.register(EntityTypeRegistry.BEER_ELEMENTAL, BeerElementalRenderer::new);
        EntityRendererRegistry.register(EntityTypeRegistry.BEER_ELEMENTAL_ATTACK, BeerElementalAttackRenderer::new);
        EntityRendererRegistry.register(EntityTypeRegistry.DARK_BREW, ThrownItemRenderer::new);
    }

    public static void registerEntityModelLayers() {
        EntityModelLayerRegistry.register(BrewfestHatModel.LAYER_LOCATION, BrewfestHatModel::createBodyLayer);
        EntityModelLayerRegistry.register(BrewfestChestplateModel.LAYER_LOCATION, BrewfestChestplateModel::createBodyLayer);
        EntityModelLayerRegistry.register(BrewfestLeggingsModel.LAYER_LOCATION, BrewfestLeggingsModel::createBodyLayer);
        EntityModelLayerRegistry.register(BrewfestBootsModel.LAYER_LOCATION, BrewfestBootsModel::createBodyLayer);
        EntityModelLayerRegistry.register(BeerElementalModel.BEER_ELEMENTAL_MODEL_LAYER, BeerElementalModel::createBodyLayer);
        EntityModelLayerRegistry.register(CompletionistBannerRenderer.LAYER_LOCATION, CompletionistBannerRenderer::createBodyLayer);
    }
}