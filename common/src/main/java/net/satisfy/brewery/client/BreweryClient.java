package net.satisfy.brewery.client;

import net.satisfy.foundation.client.render.WallDecorationRenderer;
import net.satisfy.foundation.banner.CompletionistBannerRenderer;
import dev.architectury.registry.client.level.entity.EntityModelLayerRegistry;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import dev.architectury.registry.client.rendering.ColorHandlerRegistry;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.satisfy.brewery.client.gui.overlay.BrewingstationInfoProvider;
import net.satisfy.brewery.client.model.BeerElementalModel;
import net.satisfy.brewery.client.model.BrewfestBootsModel;
import net.satisfy.brewery.client.model.BrewfestChestplateModel;
import net.satisfy.brewery.client.model.BrewfestHatModel;
import net.satisfy.brewery.client.model.BrewfestLeggingsModel;
import net.satisfy.brewery.client.renderer.block.BeerMugRenderer;
import net.satisfy.brewery.client.renderer.block.BeverageRenderer;
import net.satisfy.brewery.client.renderer.block.BrewingstationRenderer;
import net.satisfy.foundation.storage.StorageBlockEntityRenderer;
import net.satisfy.brewery.client.renderer.entity.BeerElementalAttackRenderer;
import net.satisfy.brewery.client.renderer.entity.BeerElementalRenderer;
import net.satisfy.brewery.core.block.PatternedWoolBlock;
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
                WHISKEY_JAMESONS_MALT.get(), WHISKEY_SMOKEY_REVERIE.get(), BREWERY_BANNER.get(), BREWERY_WALL_BANNER.get(),
                PATTERNED_WOOL.get(), PATTERNED_CARPET_BLOCK.get(), TABLE.get()
        );

        ColorHandlerRegistry.registerBlockColors((state, world, pos, tintIndex) -> {
            if (world == null || pos == null) {
                return -1;
            }
            return BiomeColors.getAverageWaterColor(world, pos);
        }, WOODEN_BREWINGSTATION, COPPER_BREWINGSTATION, NETHERITE_BREWINGSTATION);

        ColorHandlerRegistry.registerBlockColors((state, world, pos, tintIndex) ->
                tintIndex == 0 ? state.getValue(PatternedWoolBlock.COLOR).getTextureDiffuseColor() : -1,
                PATTERNED_WOOL, PATTERNED_CARPET_BLOCK, TABLE);
        ColorHandlerRegistry.registerItemColors((stack, tintIndex) -> {
            if (tintIndex != 0) {
                return -1;
            }
            return PatternedWoolBlock.getColor(stack).getTextureDiffuseColor();
        }, PATTERNED_WOOL, PATTERNED_CARPET_BLOCK, PATTERNED_CARPET);

        BlockEntityRendererRegistry.register(EntityTypeRegistry.BREWERY_BANNER.get(), CompletionistBannerRenderer::new);
        BlockEntityRendererRegistry.register(EntityTypeRegistry.STORAGE_ENTITY.get(), context -> new StorageBlockEntityRenderer());
        BlockEntityRendererRegistry.register(EntityTypeRegistry.BEER_MUG_BLOCK_ENTITY.get(), BeerMugRenderer::new);
        BlockEntityRendererRegistry.register(EntityTypeRegistry.BREWINGSTATION_BLOCK_ENTITY.get(), BrewingstationRenderer::new);
        BlockEntityRendererRegistry.register(EntityTypeRegistry.WALL_DECORATION.get(), WallDecorationRenderer::new);

        StorageBlockEntityRenderer.registerStorageType(StorageTypeRegistry.BEVERAGE, new BeverageRenderer());
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
    }
}