package net.satisfy.brewery.core.registry;

import net.satisfy.foundation.banner.CompletionistBannerEntity;
import net.satisfy.foundation.block.WallDecorationBlockEntity;
import net.satisfy.foundation.block.CabinetBlockEntity;
import net.satisfy.foundation.storage.StorageBlockEntity;
import dev.architectury.registry.level.entity.EntityAttributeRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.satisfy.brewery.Brewery;
import net.satisfy.brewery.core.block.entity.*;
import net.satisfy.brewery.core.entity.BeerElementalAttackEntity;
import net.satisfy.brewery.core.entity.BeerElementalEntity;
import net.satisfy.brewery.core.entity.DarkBrewEntity;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class EntityTypeRegistry {
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Brewery.MOD_ID, Registries.BLOCK_ENTITY_TYPE);
    private static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Brewery.MOD_ID, Registries.ENTITY_TYPE);

    public static final RegistrySupplier<EntityType<DarkBrewEntity>> DARK_BREW = registerEntityType("dark_brew", () -> EntityType.Builder.<DarkBrewEntity>of(DarkBrewEntity::new, MobCategory.MISC).sized(0.5f, 0.5f).build(Brewery.identifier("dark_brew").toString()));
    public static final RegistrySupplier<EntityType<BeerElementalEntity>> BEER_ELEMENTAL = registerEntityType("beer_elemental", () -> EntityType.Builder.of(BeerElementalEntity::new, MobCategory.MONSTER).sized(1.0F, 1.6F).clientTrackingRange(80).updateInterval(3).build(Brewery.identifier("beer_elemental").toString()));
    public static final RegistrySupplier<EntityType<BeerElementalAttackEntity>> BEER_ELEMENTAL_ATTACK = registerEntityType("beer_elemental_attack", () -> EntityType.Builder.<BeerElementalAttackEntity>of(BeerElementalAttackEntity::new, MobCategory.MISC).sized(0.3125F, 0.3125F).clientTrackingRange(4).updateInterval(10).build(Brewery.identifier("beer_elemental_attack").toString()));

    public static final RegistrySupplier<BlockEntityType<BrewstationBlockEntity>> BREWINGSTATION_BLOCK_ENTITY = registerBlockEntity("brewingstation", () -> BlockEntityType.Builder.of(BrewstationBlockEntity::new, ObjectRegistry.WOODEN_BREWINGSTATION.get(), ObjectRegistry.COPPER_BREWINGSTATION.get(), ObjectRegistry.NETHERITE_BREWINGSTATION.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<BeerMugBlockEntity>> BEER_MUG_BLOCK_ENTITY = registerBlockEntity("beer_mug", () -> BlockEntityType.Builder.of(BeerMugBlockEntity::new, ObjectRegistry.BEER_MUG.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<StorageBlockEntity>> STORAGE_ENTITY = registerBlockEntity("storage", () -> BlockEntityType.Builder.of((pos, state) -> new StorageBlockEntity(EntityTypeRegistry.STORAGE_ENTITY.get(), pos, state), StorageTypeRegistry.registerBlocks(new HashSet<>()).toArray(new Block[0])).build(null));
    public static final RegistrySupplier<BlockEntityType<CabinetBlockEntity>> CABINET_BLOCK_ENTITY = registerBlockEntity("cabinet", () -> BlockEntityType.Builder.of((pos, state) -> new CabinetBlockEntity(EntityTypeRegistry.CABINET_BLOCK_ENTITY.get(), pos, state), addCabinet(new HashSet<>()).toArray(new Block[0])).build(null));
    public static final RegistrySupplier<BlockEntityType<CompletionistBannerEntity>> BREWERY_BANNER = registerBlockEntity("brewery_banner", () -> BlockEntityType.Builder.of(CompletionistBannerEntity::new, ObjectRegistry.BREWERY_BANNER.get(), ObjectRegistry.BREWERY_WALL_BANNER.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<WallDecorationBlockEntity>> WALL_DECORATION = registerBlockEntity("wall_decoration", () -> BlockEntityType.Builder.of((pos, state) -> new WallDecorationBlockEntity(EntityTypeRegistry.WALL_DECORATION.get(), pos, state), ObjectRegistry.GINGERBREAD.get()).build(null));

    public static Set<Block> addCabinet(Set<Block> blocks) {
        blocks.add(ObjectRegistry.CABINET.get());
        blocks.add(ObjectRegistry.DRAWER.get());
        blocks.add(ObjectRegistry.SIDEBOARD.get());
        blocks.add(ObjectRegistry.WALL_CABINET.get());
        return blocks;
    }

    public static void registerBeerElemental(Supplier<? extends EntityType<? extends Monster>> typeSupplier) {
        EntityAttributeRegistry.register(typeSupplier, BeerElementalEntity::createAttributes);
    }

    private static <T extends BlockEntityType<?>> RegistrySupplier<T> registerBlockEntity(final String path, final Supplier<T> type) {
        return BLOCK_ENTITY_TYPES.register(Brewery.identifier(path), type);
    }

    private static <T extends EntityType<?>> RegistrySupplier<T> registerEntityType(final String path, final Supplier<T> type) {
        return ENTITY_TYPES.register(Brewery.identifier(path), type);
    }

    public static void init() {
        ENTITY_TYPES.register();
        BLOCK_ENTITY_TYPES.register();
        registerBeerElemental(BEER_ELEMENTAL);
    }
}
