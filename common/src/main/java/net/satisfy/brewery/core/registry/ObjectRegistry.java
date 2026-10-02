package net.satisfy.brewery.core.registry;

import net.satisfy.foundation.util.RegistryUtil;
import dev.architectury.core.item.ArchitecturySpawnEggItem;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.PushReaction;
import net.satisfy.brewery.platform.PlatformHelper;
import net.satisfy.brewery.Brewery;
import net.satisfy.brewery.core.block.*;
import net.satisfy.brewery.core.block.property.BrewMaterial;
import net.satisfy.brewery.core.item.*;
import net.satisfy.foundation.block.BenchBlock;
import net.satisfy.foundation.block.BonemealableFlowerBlock;
import net.satisfy.foundation.block.BonemealableTallFlowerBlock;
import net.satisfy.foundation.food.FoodBlock;
import net.satisfy.foundation.food.PlaceableEffectFoodItem;
import net.satisfy.foundation.food.EffectFoodItem;
import net.satisfy.farm_and_charm.core.registry.ArmorMaterialRegistry;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ObjectRegistry {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Brewery.MOD_ID, Registries.BLOCK);
    public static final Registrar<Block> BLOCK_REGISTRAR = BLOCKS.getRegistrar();
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Brewery.MOD_ID, Registries.ITEM);
    public static final Registrar<Item> ITEM_REGISTRAR = ITEMS.getRegistrar();

    public static final RegistrySupplier<Item> BREATHALYZER = registerItem("breathalyzer", () -> new BreathalyzerItem(getSettings()));
    public static final RegistrySupplier<Item> DARK_BREW = registerItem("dark_brew", () -> new DarkBrewItem(getSettings()));
    public static final RegistrySupplier<Item> SAUSAGE = registerItem("sausage", () -> new EffectFoodItem(getFoodItemSettings(PlatformHelper.getNutrition("sausage"), PlatformHelper.getSaturationMod("sausage"), MobEffectRegistry.STOUTHEART, 6000), 6000, true));
    public static final RegistrySupplier<Item> PRETZEL = registerItem("pretzel", () -> new EffectFoodItem(getFoodItemSettings(PlatformHelper.getNutrition("pretzel"), PlatformHelper.getSaturationMod("pretzel"), MobEffectRegistry.STOUTHEART, 2000), 2000, false));
    public static final RegistrySupplier<Item> BEER_ELEMENTAL_SPAWN_EGG = registerItem("beer_elemental_spawn_egg", () -> new ArchitecturySpawnEggItem(EntityTypeRegistry.BEER_ELEMENTAL, -1, -1, getSettings()));
    public static final RegistrySupplier<Item> BREWFEST_HAT = registerItem("brewfest_hat", () -> new BrewfestHatItem(ArmorMaterialRegistry.withTextureNoOverlay(ArmorMaterialRegistry.CLOTH, Brewery.identifier("models/armor/brewfest_hat")), ArmorItem.Type.HELMET, getSettings().rarity(Rarity.EPIC), Brewery.identifier("models/armor/brewfest_hat")));
    public static final RegistrySupplier<Item> BREWFEST_HAT_RED = registerItem("brewfest_hat_red", () -> new BrewfestHatItem(ArmorMaterialRegistry.withTextureNoOverlay(ArmorMaterialRegistry.CLOTH, Brewery.identifier("models/armor/brewfest_hat_red")), ArmorItem.Type.HELMET, getSettings().rarity(Rarity.EPIC), Brewery.identifier("models/armor/brewfest_hat_red")));
    public static final RegistrySupplier<Item> BREWFEST_REGALIA = registerItem("brewfest_regalia", () -> new BrewfestChestItem(ArmorMaterialRegistry.withTextureNoOverlay(ArmorMaterialRegistry.CLOTH, Brewery.identifier("models/armor/lederhosen")), ArmorItem.Type.CHESTPLATE, getSettings().rarity(Rarity.EPIC), Brewery.identifier("models/armor/lederhosen")));

    public static final RegistrySupplier<Item> BREWFEST_TROUSERS = registerItem("brewfest_trousers", () -> new BrewfestLegsItem(ArmorMaterialRegistry.withTextureNoOverlay(ArmorMaterialRegistry.CLOTH, Brewery.identifier("models/armor/lederhosen")), ArmorItem.Type.LEGGINGS, getSettings().rarity(Rarity.EPIC), Brewery.identifier("models/armor/lederhosen")));
    public static final RegistrySupplier<Item> BREWFEST_BOOTS = registerItem("brewfest_boots", () -> new BrewfestBootsItem(ArmorMaterialRegistry.withTextureNoOverlay(ArmorMaterialRegistry.CLOTH, Brewery.identifier("models/armor/lederhosen")), ArmorItem.Type.BOOTS, getSettings().rarity(Rarity.RARE), Brewery.identifier("models/armor/lederhosen")));
    public static final RegistrySupplier<Item> BREWFEST_DRESS = registerItem("brewfest_dress", () -> new BrewfestLegsItem(ArmorMaterialRegistry.withTextureNoOverlay(ArmorMaterialRegistry.CLOTH, Brewery.identifier("models/armor/dirndl")), ArmorItem.Type.LEGGINGS, getSettings().rarity(Rarity.RARE), Brewery.identifier("models/armor/dirndl")));
    public static final RegistrySupplier<Item> BREWFEST_BLOUSE = registerItem("brewfest_blouse", () -> new BrewfestChestItem(ArmorMaterialRegistry.withTextureNoOverlay(ArmorMaterialRegistry.CLOTH, Brewery.identifier("models/armor/dirndl")), ArmorItem.Type.CHESTPLATE, getSettings().rarity(Rarity.EPIC), Brewery.identifier("models/armor/dirndl")));
    public static final RegistrySupplier<Item> BREWFEST_SHOES = registerItem("brewfest_shoes", () -> new BrewfestBootsItem(ArmorMaterialRegistry.withTextureNoOverlay(ArmorMaterialRegistry.CLOTH, Brewery.identifier("models/armor/dirndl")), ArmorItem.Type.BOOTS, getSettings().rarity(Rarity.RARE), Brewery.identifier("models/armor/dirndl")));
    public static final RegistrySupplier<Block> WILD_HOPS = registerWithItem("wild_hops", () -> new BonemealableTallFlowerBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.ROSE_BUSH)));
    public static final RegistrySupplier<Block> HOPS_CROP = registerWithoutItem("hops_crop", () -> new HopsCropHeadBlock(getBushSettings().randomTicks()));
    public static final RegistrySupplier<Block> HOPS_CROP_BODY = registerWithoutItem("hops_crop_body", () -> new HopsCropBodyBlock(getBushSettings().randomTicks()));
    public static final RegistrySupplier<Item> HOPS = registerItem("hops", () -> new ItemNameBlockItem(HOPS_CROP.get(), getSettings()));

    public static final RegistrySupplier<Block> DRIED_WHEAT = registerWithItem("dried_wheat", () -> new BagBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.RED_WOOL)));
    public static final RegistrySupplier<Block> DRIED_BARLEY = registerWithItem("dried_barley", () -> new BagBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.RED_WOOL)));
    public static final RegistrySupplier<Block> DRIED_CORN = registerWithItem("dried_corn", () -> new BagBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.RED_WOOL)));
    public static final RegistrySupplier<Block> DRIED_OAT = registerWithItem("dried_oat", () -> new BagBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.RED_WOOL)));
    public static final RegistrySupplier<Block> BENCH = registerWithItem("bench", () -> new BenchBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)));
    public static final RegistrySupplier<Block> TABLE = registerWithItem("table", () -> new TableBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)));
    public static final RegistrySupplier<Block> PATTERNED_WOOL = registerWithItem("patterned_wool", () -> new PatternedWoolBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BLACK_WOOL)));
    public static final RegistrySupplier<Block> PATTERNED_CARPET_BLOCK = registerWithItem("patterned_carpet_block", () -> new PatternedCarpetBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BLACK_CARPET)));
    public static final RegistrySupplier<Item> PATTERNED_CARPET = registerItem("patterned_carpet", () -> new BlockItem(PATTERNED_CARPET_BLOCK.get(), getSettings()));
    public static final RegistrySupplier<Block> CABINET = registerWithItem("cabinet", () -> new CabinetBlock(BlockBehaviour.Properties.of().strength(2.0F, 3.0F).sound(SoundType.WOOD), SoundEventRegistry.CABINET_OPEN.get(), SoundEventRegistry.CABINET_CLOSE.get()));
    public static final RegistrySupplier<Block> DRAWER = registerWithItem("drawer", () -> new CabinetBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD), SoundEventRegistry.DRAWER_OPEN.get(), SoundEventRegistry.DRAWER_CLOSE.get()));
    public static final RegistrySupplier<Block> BAR_COUNTER = registerWithItem("bar_counter", () -> new BarCounterBlock(BlockBehaviour.Properties.of().strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
    public static final RegistrySupplier<Block> SIDEBOARD = registerWithItem("sideboard", () -> new SideBoardBlock(BlockBehaviour.Properties.of().strength(2.0F, 3.0F).sound(SoundType.WOOD), SoundEventRegistry.CABINET_OPEN, SoundEventRegistry.CABINET_CLOSE));
    public static final RegistrySupplier<Block> WALL_CABINET = registerWithItem("wall_cabinet", () -> new CabinetWallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD), SoundEventRegistry.CABINET_OPEN.get(), SoundEventRegistry.CABINET_CLOSE.get()));
    public static final RegistrySupplier<Block> WOODEN_BREWINGSTATION = registerWithItem("wooden_brewingstation", () -> new BrewKettleBlock(BrewMaterial.WOOD, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).noOcclusion().pushReaction(PushReaction.BLOCK)));
    public static final RegistrySupplier<Block> COPPER_BREWINGSTATION = registerWithItem("copper_brewingstation", () -> new BrewKettleBlock(BrewMaterial.COPPER, BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_BLOCK).noOcclusion().pushReaction(PushReaction.BLOCK)));
    public static final RegistrySupplier<Block> NETHERITE_BREWINGSTATION = registerWithItem("netherite_brewingstation", () -> new BrewKettleBlock(BrewMaterial.NETHERITE, BlockBehaviour.Properties.ofFullCopy(Blocks.NETHERITE_BLOCK).noOcclusion().pushReaction(PushReaction.BLOCK)));
    public static final RegistrySupplier<Block> BREW_WHISTLE = registerWithoutItem("brew_whistle", () -> new BrewWhistleBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_BLOCK).pushReaction(PushReaction.BLOCK)));
    public static final RegistrySupplier<Block> BREW_OVEN = registerWithoutItem("brew_oven", () -> new BrewOvenBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_BLOCK).pushReaction(PushReaction.BLOCK)));
    public static final RegistrySupplier<Block> BREW_KETTLE_TOP = registerWithoutItem("brew_kettle_top", () -> new BrewKettleTopBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_BLOCK).noOcclusion().pushReaction(PushReaction.BLOCK)));
    public static final RegistrySupplier<Block> BREW_TIMER = registerWithoutItem("brew_timer", () -> new BrewTimerBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_BLOCK).pushReaction(PushReaction.BLOCK)));
    public static final RegistrySupplier<Block> BARREL_MAIN = registerWithItem("barrel_main", () -> new BigBarrelMainBlock(BlockBehaviour.Properties.of().strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion().pushReaction(PushReaction.IGNORE)));
    public static final RegistrySupplier<Block> BARREL_MAIN_HEAD = registerWithoutItem("barrel_main_head", () -> new BigBarrelMainHeadBlock(BlockBehaviour.Properties.of().strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion().pushReaction(PushReaction.IGNORE).noLootTable()));
    public static final RegistrySupplier<Block> BARREL_RIGHT = registerWithoutItem("barrel_right", () -> new BigBarrelRightBlock(BlockBehaviour.Properties.of().strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion().pushReaction(PushReaction.IGNORE).noLootTable()));
    public static final RegistrySupplier<Block> BARREL_HEAD_RIGHT = registerWithoutItem("barrel_head_right", () -> new BigBarrelRightHeadBlock(BlockBehaviour.Properties.of().strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion().pushReaction(PushReaction.IGNORE).noLootTable()));
    public static final RegistrySupplier<Block> BEER_MUG = registerWithItem("beer_mug", () -> new BeerMugFlowerPotBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD)));
    public static final RegistrySupplier<Block> BEER_WHEAT = registerWithItemeverage("beer_wheat", () -> new BeverageBlock(getMugSettings(), 2), MobEffectRegistry.SNOWWHITE);
    public static final RegistrySupplier<Block> BEER_HOPS = registerWithItemeverage("beer_hops", () -> new BeverageBlock(getMugSettings(), 2), MobEffectRegistry.PARTYSTARTER);
    public static final RegistrySupplier<Block> BEER_BARLEY = registerWithItemeverage("beer_barley", () -> new BeverageBlock(getMugSettings(), 2), MobEffectRegistry.PINTCHARISMA);
    public static final RegistrySupplier<Block> BEER_OAT = registerWithItemeverage("beer_oat", () -> new BeverageBlock(getMugSettings(), 2), MobEffectRegistry.MINING);
    public static final RegistrySupplier<Block> BEER_NETTLE = registerWithItemeverage("beer_nettle", () -> new BeverageBlock(getMugSettings(), 2), MobEffectRegistry.PACIFY);
    public static final RegistrySupplier<Block> BEER_HALEY = registerWithItemeverage("beer_haley", () -> new BeverageBlock(getMugSettings(), 2), MobEffectRegistry.HALEY);
    public static final RegistrySupplier<Block> WHISKEY_MAGGOALLAN = registerWithItemeverage("whiskey_maggoallan", () -> new BeverageBlock(getBeverageSettings(), 1), MobEffectRegistry.HEALINGTOUCH);
    public static final RegistrySupplier<Block> WHISKEY_CARRASCONLABEL = registerWithItemeverage("whiskey_carrasconlabel", () -> new BeverageBlock(getBeverageSettings(), 1), MobEffectRegistry.RENEWINGTOUCH);
    public static final RegistrySupplier<Block> WHISKEY_LILITUSINGLEMALT = registerWithItemeverage("whiskey_lilitusinglemalt", () -> new BeverageBlock(getBeverageSettings(), 1), MobEffectRegistry.PARTYSTARTER);
    public static final RegistrySupplier<Block> WHISKEY_JOJANNIK = registerWithItemeverage("whiskey_jojannik", () -> new BeverageBlock(getBeverageSettings(), 1), MobEffectRegistry.TOXICTOUCH);
    public static final RegistrySupplier<Block> WHISKEY_CRISTELWALKER = registerWithItemeverage("whiskey_cristelwalker", () -> new BeverageBlock(getBeverageSettings(), 3), MobEffectRegistry.PROTECTIVETOUCH);
    public static final RegistrySupplier<Block> WHISKEY_AK = registerWithItemeverage("whiskey_ak", () -> new BeverageBlock(getBeverageSettings(), 3), MobEffectRegistry.LIGHTNING_STRIKE);
    public static final RegistrySupplier<Block> WHISKEY_HIGHLAND_HEARTH = registerWithItemeverage("whiskey_highland_hearth", () -> new BeverageBlock(getBeverageSettings(), 1), MobEffectRegistry.REPULSION);
    public static final RegistrySupplier<Block> WHISKEY_JAMESONS_MALT = registerWithItemeverage("whiskey_jamesons_malt", () -> new BeverageBlock(getBeverageSettings(), 1), MobEffectRegistry.EXPLOSION);
    public static final RegistrySupplier<Block> WHISKEY_SMOKEY_REVERIE = registerWithItemeverage("whiskey_smokey_reverie", () -> new BeverageBlock(getBeverageSettings(), 2), MobEffectRegistry.COMBUSTION);
    public static final RegistrySupplier<Block> PORK_KNUCKLE_BLOCK = registerWithoutItem("pork_knuckle", () -> new FoodBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CAKE), 4, new FoodProperties.Builder().nutrition(9).saturationModifier(0.9F).build()));
    public static final RegistrySupplier<Item> PORK_KNUCKLE = registerItem("pork_knuckle", () -> new PlaceableEffectFoodItem(PORK_KNUCKLE_BLOCK.get(), getFoodItemSettings(PlatformHelper.getNutrition("pork_knuckle"), PlatformHelper.getSaturationMod("pork_knuckle"), MobEffectRegistry.STOUTHEART, 2000)));
    public static final RegistrySupplier<Block> FRIED_CHICKEN_BLOCK = registerWithoutItem("fried_chicken", () -> new FoodBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CAKE), 4, new FoodProperties.Builder().nutrition(7).saturationModifier(0.7F).build()));
    public static final RegistrySupplier<Item> FRIED_CHICKEN = registerItem("fried_chicken", () -> new PlaceableEffectFoodItem(FRIED_CHICKEN_BLOCK.get(), getFoodItemSettings(PlatformHelper.getNutrition("fried_chicken"), PlatformHelper.getSaturationMod("fried_chicken"), MobEffectRegistry.STOUTHEART, 1500)));
    public static final RegistrySupplier<Block> HALF_CHICKEN_BLOCK = registerWithoutItem("half_chicken", () -> new FoodBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CAKE), 4, new FoodProperties.Builder().nutrition(3).saturationModifier(0.4F).build()));
    public static final RegistrySupplier<Item> HALF_CHICKEN = registerItem("half_chicken", () -> new PlaceableEffectFoodItem(HALF_CHICKEN_BLOCK.get(), getFoodItemSettings(PlatformHelper.getNutrition("half_chicken"), PlatformHelper.getSaturationMod("half_chicken"), MobEffectRegistry.STOUTHEART, 900)));
    public static final RegistrySupplier<Block> MASHED_POTATOES_BLOCK = registerWithoutItem("mashed_potatoes", () -> new FoodBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CAKE), 4, new FoodProperties.Builder().nutrition(4).saturationModifier(0.5F).build()));
    public static final RegistrySupplier<Item> MASHED_POTATOES = registerItem("mashed_potatoes", () -> new PlaceableEffectFoodItem(MASHED_POTATOES_BLOCK.get(), getFoodItemSettings(PlatformHelper.getNutrition("mashed_potatoes"), PlatformHelper.getSaturationMod("mashed_potatoes"), MobEffectRegistry.STOUTHEART, 4000)));
    public static final RegistrySupplier<Block> POTATO_SALAD_BLOCK = registerWithoutItem("potato_salad", () -> new FoodBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CAKE), 4, new FoodProperties.Builder().nutrition(8).saturationModifier(0.8F).build()));
    public static final RegistrySupplier<Item> POTATO_SALAD = registerItem("potato_salad", () -> new PlaceableEffectFoodItem(POTATO_SALAD_BLOCK.get(), getFoodItemSettings(PlatformHelper.getNutrition("potato_salad"), PlatformHelper.getSaturationMod("potato_salad"), MobEffectRegistry.STOUTHEART, 6000)));
    public static final RegistrySupplier<Block> DUMPLINGS_BLOCK = registerWithoutItem("dumplings", () -> new FoodBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CAKE), 4, new FoodProperties.Builder().nutrition(7).saturationModifier(0.8F).build()));
    public static final RegistrySupplier<Item> DUMPLINGS = registerItem("dumplings", () -> new PlaceableEffectFoodItem(DUMPLINGS_BLOCK.get(), getFoodItemSettings(PlatformHelper.getNutrition("dumplings"), PlatformHelper.getSaturationMod("dumplings"), MobEffectRegistry.STOUTHEART, 6000)));
    public static final RegistrySupplier<Block> GINGERBREAD = registerWithItem("gingerbread", () -> new WallDecorationBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.FLOWER_POT)));
    public static final RegistrySupplier<Block> BREWERY_BANNER = registerWithItem("brewery_banner", () -> new CompletionistBannerBlock(BlockBehaviour.Properties.of().strength(1F).instrument(NoteBlockInstrument.BASS).noCollission().sound(SoundType.WOOD)));
    public static final RegistrySupplier<Block> BREWERY_WALL_BANNER = registerWithoutItem("brewery_wall_banner", () -> new CompletionistWallBannerBlock(BlockBehaviour.Properties.of().strength(1F).instrument(NoteBlockInstrument.BASS).noCollission().sound(SoundType.WOOD)));

    public static void init() {
        ITEMS.register();
        BLOCKS.register();
    }

    public static BlockBehaviour.Properties properties(float strength) {
        return properties(strength, strength);
    }

    public static BlockBehaviour.Properties properties(float breakSpeed, float explosionResist) {
        return BlockBehaviour.Properties.of().strength(breakSpeed, explosionResist);
    }

    private static Item.Properties getSettings(Consumer<Item.Properties> consumer) {
        Item.Properties settings = new Item.Properties();
        consumer.accept(settings);
        return settings;
    }

    static Item.Properties getSettings() {
        return getSettings(s -> {});
    }

    private static Item.Properties getFoodItemSettings(int nutrition, float saturationMod, RegistrySupplier<MobEffect> effect, int duration) {
        return getFoodItemSettings(nutrition, saturationMod, effect, duration, true, false);
    }

    private static Item.Properties getFoodItemSettings(int nutrition, float saturationMod, RegistrySupplier<MobEffect> effect, int duration, boolean alwaysEat, boolean fast) {
        return getSettings().food(createFood(nutrition, saturationMod, BuiltInRegistries.MOB_EFFECT.wrapAsHolder(effect.get()), duration, alwaysEat, fast));
    }

    private static FoodProperties createFood(int nutrition, float saturationMod, Holder<MobEffect> effect, int duration, boolean alwaysEat, boolean fast) {
        FoodProperties.Builder food = new FoodProperties.Builder().nutrition(nutrition).saturationModifier(saturationMod);
        if (alwaysEat) food.alwaysEdible();
        if (fast) food.fast();
        if (effect != null) {
            food.effect(new MobEffectInstance(effect, duration), 1.0F);
        }
        return food.build();
    }

    private static BlockBehaviour.Properties getBeverageSettings() {
        return BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).noOcclusion().instabreak();
    }

    private static BlockBehaviour.Properties getMugSettings() {
        return BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD).noOcclusion().instabreak();
    }

    private static BlockBehaviour.Properties getBushSettings() {
        return BlockBehaviour.Properties.ofFullCopy(Blocks.SWEET_BERRY_BUSH);
    }

    private static <T extends Block> RegistrySupplier<T> registerWithItemeverage(String name, Supplier<T> block, RegistrySupplier<MobEffect> effect) {
        RegistrySupplier<T> toReturn = registerWithoutItem(name, block);
        registerItem(name, () -> new DrinkBlockItem(effect.get(), 600, toReturn.get(), getSettings(settings -> settings.food(beverageFoodComponent()))));
        return toReturn;
    }

    private static FoodProperties beverageFoodComponent() {
        FoodProperties.Builder component = new FoodProperties.Builder().nutrition(2).saturationModifier(1);
        return component.build();
    }

    public static <T extends Block> RegistrySupplier<T> registerWithItem(String name, Supplier<T> block) {
        return RegistryUtil.registerWithItem(BLOCKS, BLOCK_REGISTRAR, ITEMS, ITEM_REGISTRAR, Brewery.identifier(name), block);
    }

    public static <T extends Block> RegistrySupplier<T> registerWithoutItem(String path, Supplier<T> block) {
        return RegistryUtil.registerWithoutItem(BLOCKS, BLOCK_REGISTRAR, Brewery.identifier(path), block);
    }

    public static <T extends Item> RegistrySupplier<T> registerItem(String path, Supplier<T> itemSupplier) {
        return RegistryUtil.registerItem(ITEMS, ITEM_REGISTRAR, Brewery.identifier(path), itemSupplier);
    }
}
