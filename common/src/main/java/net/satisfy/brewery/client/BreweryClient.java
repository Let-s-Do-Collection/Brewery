package net.satisfy.brewery.client;

import net.satisfy.brewery.client.renderer.block.BrewTimerRenderer;
import net.satisfy.brewery.client.renderer.block.BrewWhistleRenderer;
import net.satisfy.brewery.client.gui.overlay.BigBarrelInfoProvider;
import net.satisfy.foundation.client.armor.ArmorModels;
import net.satisfy.foundation.client.render.WallDecorationRenderer;
import net.satisfy.foundation.banner.CompletionistBannerRenderer;
import dev.architectury.registry.client.level.entity.EntityModelLayerRegistry;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import dev.architectury.registry.client.rendering.ColorHandlerRegistry;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.satisfy.brewery.client.gui.overlay.BrewingstationInfoProvider;
import net.satisfy.brewery.client.model.BeerElementalModel;
import net.satisfy.brewery.client.model.BrewfestBootsModel;
import net.satisfy.brewery.client.model.BrewfestChestplateModel;
import net.satisfy.brewery.client.model.BrewfestHatModel;
import net.satisfy.brewery.client.model.BrewfestLeggingsModel;
import net.satisfy.brewery.client.renderer.block.BeerMugRenderer;
import net.satisfy.foundation.storage.BottleGroupRenderer;
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
        registerArmorModels();
        RenderTypeRegistry.register(RenderType.cutout(),
                WILD_HOPS.get(), BEER_MUG.get(), BEER_WHEAT.get(), BEER_HOPS.get(), BEER_BARLEY.get(), BEER_HALEY.get(), BEER_OAT.get(), BEER_NETTLE.get(),
                HOPS_CROP_BODY.get(), HOPS_CROP.get(), WHISKEY_MAGGOALLAN.get(), WHISKEY_CARRASCONLABEL.get(), WHISKEY_LILITUSINGLEMALT.get(),
                WHISKEY_JOJANNIK.get(), WHISKEY_MAGGOALLAN.get(), WHISKEY_CRISTELWALKER.get(), WHISKEY_AK.get(), WHISKEY_HIGHLAND_HEARTH.get(),
                WHISKEY_JAMESONS_MALT.get(), WHISKEY_SMOKEY_REVERIE.get(), BREWERY_BANNER.get(), BREWERY_WALL_BANNER.get(),
                PATTERNED_WOOL.get(), PATTERNED_CARPET_BLOCK.get(), TABLE.get()
        );

        ColorHandlerRegistry.registerBlockColors((state, world, pos, tintIndex) ->
                tintIndex == 0 ? PatternedWoolBlock.getTint(state.getValue(PatternedWoolBlock.COLOR)) : -1,
                PATTERNED_WOOL, PATTERNED_CARPET_BLOCK, TABLE);
        ColorHandlerRegistry.registerItemColors((stack, tintIndex) -> {
            if (tintIndex != 0) {
                return -1;
            }
            return PatternedWoolBlock.getTint(PatternedWoolBlock.getColor(stack));
        }, PATTERNED_WOOL, PATTERNED_CARPET_BLOCK, PATTERNED_CARPET);

        BlockEntityRendererRegistry.register(EntityTypeRegistry.BREWERY_BANNER.get(), CompletionistBannerRenderer::new);
        BlockEntityRendererRegistry.register(EntityTypeRegistry.STORAGE_ENTITY.get(), context -> new StorageBlockEntityRenderer());
        BlockEntityRendererRegistry.register(EntityTypeRegistry.BEER_MUG_BLOCK_ENTITY.get(), BeerMugRenderer::new);
        BlockEntityRendererRegistry.register(EntityTypeRegistry.BREW_WHISTLE_BLOCK_ENTITY.get(), BrewWhistleRenderer::new);
        BlockEntityRendererRegistry.register(EntityTypeRegistry.BREW_TIMER_BLOCK_ENTITY.get(), BrewTimerRenderer::new);
        BlockEntityRendererRegistry.register(EntityTypeRegistry.BREWINGSTATION_BLOCK_ENTITY.get(), BrewingstationRenderer::new);
        BlockEntityRendererRegistry.register(EntityTypeRegistry.WALL_DECORATION.get(), WallDecorationRenderer::new);

        StorageBlockEntityRenderer.registerStorageType(StorageTypeRegistry.BEVERAGE, new BottleGroupRenderer());
    }

    public static void preInitClient() {
        registerEntityRenderers();
        registerBlockInfo();
        registerEntityModelLayers();
    }

    private static void registerBlockInfo() {
        BlockInfoOverlay.init();
        BlockInfoOverlay.registerProvider(new BrewingstationInfoProvider());
        BlockInfoOverlay.registerProvider(new BigBarrelInfoProvider());
    }

    private static void registerEntityRenderers() {
        EntityRendererRegistry.register(EntityTypeRegistry.BEER_ELEMENTAL, BeerElementalRenderer::new);
        EntityRendererRegistry.register(EntityTypeRegistry.BEER_ELEMENTAL_ATTACK, BeerElementalAttackRenderer::new);
        EntityRendererRegistry.register(EntityTypeRegistry.DARK_BREW, ThrownItemRenderer::new);
    }

    public static void registerArmorModels() {
        ArmorModels.register(BrewfestHatModel.LAYER_LOCATION, BrewfestHatModel::new, BREWFEST_HAT.get(), BREWFEST_HAT_RED.get());
        ArmorModels.register(BrewfestChestplateModel.LAYER_LOCATION, BrewfestChestplateModel::new, BREWFEST_REGALIA.get(), BREWFEST_BLOUSE.get());
        ArmorModels.register(BrewfestLeggingsModel.LAYER_LOCATION, BrewfestLeggingsModel::new, BREWFEST_TROUSERS.get(), BREWFEST_DRESS.get());
        ArmorModels.register(BrewfestBootsModel.LAYER_LOCATION, BrewfestBootsModel::new, BREWFEST_BOOTS.get(), BREWFEST_SHOES.get());
    }

    public static void registerEntityModelLayers() {
        EntityModelLayerRegistry.register(BrewfestHatModel.LAYER_LOCATION, BrewfestHatModel::createBodyLayer);
        EntityModelLayerRegistry.register(BrewfestChestplateModel.LAYER_LOCATION, BrewfestChestplateModel::createBodyLayer);
        EntityModelLayerRegistry.register(BrewfestLeggingsModel.LAYER_LOCATION, BrewfestLeggingsModel::createBodyLayer);
        EntityModelLayerRegistry.register(BrewfestBootsModel.LAYER_LOCATION, BrewfestBootsModel::createBodyLayer);
        EntityModelLayerRegistry.register(BeerElementalModel.BEER_ELEMENTAL_MODEL_LAYER, BeerElementalModel::createBodyLayer);
    }
}