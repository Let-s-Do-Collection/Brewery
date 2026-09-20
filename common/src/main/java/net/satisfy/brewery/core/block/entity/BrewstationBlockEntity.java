package net.satisfy.brewery.core.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.brewery.core.block.property.BrewMaterial;
import net.satisfy.brewery.core.block.property.Heat;
import net.satisfy.brewery.core.block.property.Liquid;
import net.satisfy.brewery.core.entity.BeerElementalEntity;
import net.satisfy.brewery.core.event.brew_event.BrewEvent;
import net.satisfy.brewery.core.event.brew_event.BrewEvents;
import net.satisfy.brewery.core.event.brew_event.BrewHelper;
import net.satisfy.brewery.core.item.DrinkBlockItem;
import net.satisfy.brewery.core.recipe.BrewingRecipe;
import net.satisfy.brewery.core.registry.BlockStateRegistry;
import net.satisfy.brewery.core.registry.EntityTypeRegistry;
import net.satisfy.brewery.core.registry.ObjectRegistry;
import net.satisfy.brewery.core.registry.RecipeTypeRegistry;
import net.satisfy.brewery.core.registry.SoundEventRegistry;
import net.satisfy.farm_and_charm.core.util.GeneralUtil;
import net.satisfy.farm_and_charm.core.world.ImplementedInventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BrewstationBlockEntity extends BlockEntity implements ImplementedInventory, BlockEntityTicker<BrewstationBlockEntity> {
    @NotNull
    private Set<BlockPos> components = new HashSet<>(4);
    private static final int MAX_BREW_TIME = 60 * 20;
    private static final int MIN_TIME_FOR_EVENT = 5 * 20;
    private static final int MAX_TIME_FOR_EVENT = 15 * 20;
    private static final int SOUND_DURATION = 3 * 20;
    private int soundTime;
    private int brewTime;
    private int timeToNextEvent = Integer.MIN_VALUE;
    private final Set<BrewEvent> runningEvents = new HashSet<>();
    private int solved;
    private int totalEvents;
    private int eventQuota = -1;
    private int overflowStarted;
    private int overflowSolved;
    private NonNullList<ItemStack> ingredients;
    private ItemStack beer = ItemStack.EMPTY;
    private final SoundEvent spawnEntitySound = SoundEventRegistry.BREWSTATION_PROCESS_FAILED.get();

    public BrewstationBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(EntityTypeRegistry.BREWINGSTATION_BLOCK_ENTITY.get(), blockPos, blockState);
        ingredients = NonNullList.withSize(3, ItemStack.EMPTY);
    }

    public void setComponents(BlockPos... components) {
        if (components.length != 4) {
            return;
        }
        this.components.addAll(Arrays.asList(components));
    }

    public ItemInteractionResult addIngredient(ItemStack itemStack) {
        for (int i = 0; i < 3; i++) {
            ItemStack stack = this.ingredients.get(i);
            if (stack.isEmpty()) {
                this.setItem(i, itemStack.split(1));
                return ItemInteractionResult.SUCCESS;
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Nullable
    public ItemStack getBeer() {
        if (this.beer.isEmpty()) return null;
        ItemStack beerStack = this.beer.copy();
        beerStack.setCount(1);
        this.beer.shrink(1);
        if (this.beer.isEmpty() && this.level != null) {
            this.level.setBlockAndUpdate(this.getBlockPos(), this.getBlockState().setValue(BlockStateRegistry.LIQUID, Liquid.EMPTY));
        }
        return beerStack;
    }

    @Nullable
    public ItemStack peekBeer() {
        if (this.beer.isEmpty()) return null;
        ItemStack beerStack = this.beer.copy();
        beerStack.setCount(1);
        return beerStack;
    }

    @Nullable
    public ItemStack removeIngredient() {
        for (int i = 0; i < 3; i++) {
            ItemStack itemStack = this.ingredients.get(i);
            if (!itemStack.isEmpty()) {
                this.ingredients.set(i, ItemStack.EMPTY);
                return itemStack;
            }
        }
        return null;
    }

    @Override
    public void tick(Level level, BlockPos blockPos, BlockState blockState, BrewstationBlockEntity blockEntity) {
        if (level.isClientSide) return;
        if (!this.beer.isEmpty()) return;

        RecipeHolder<BrewingRecipe> active = findActiveRecipe(level);
        if (active == null) {
            endBrewing();
            return;
        }

        BrewMaterial material = this.getBlockState().getValue(BlockStateRegistry.MATERIAL);
        boolean isNetherite = material == BrewMaterial.NETHERITE;

        if (isNetherite) {
            if (!this.runningEvents.isEmpty()) {
                BrewHelper.finishEvents(this);
                this.runningEvents.clear();
            }
            this.totalEvents = 0;
            this.timeToNextEvent = Integer.MIN_VALUE;
            this.eventQuota = 0;
        } else {
            if (eventQuota < 0) eventQuota = computeEventQuota();
        }

        if (soundTime >= SOUND_DURATION) {
            level.playSound(null, blockPos, SoundEventRegistry.BREWSTATION_AMBIENT.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
            soundTime = 0;
        }
        soundTime++;

        if (!isNetherite) {
            if (timeToNextEvent == Integer.MIN_VALUE) setTimeToEvent();

            BrewHelper.checkRunningEvents(this);

            int timeLeft = MAX_BREW_TIME - brewTime;

            if (brewTime >= MAX_BREW_TIME) {
                RegistryAccess access = level.registryAccess();
                this.brew(active.value(), access);
            } else if (timeLeft >= MIN_TIME_FOR_EVENT && timeToNextEvent <= 0 && totalEvents < eventQuota && runningEvents.size() < BrewEvents.BREW_EVENTS.size()) {
                BrewEvent event = BrewHelper.getRdmEvent(this);
                if (event != null) {
                    ResourceLocation eventId = BrewEvents.getId(event);
                    if (eventId != null) {
                        if (eventId.equals(BrewEvents.KETTLE_EVENT)) overflowStarted++;
                        event.start(this.components, level);
                        runningEvents.add(event);
                        totalEvents++;
                    }
                }
                setTimeToEvent();
            }

            brewTime++;
            timeToNextEvent--;
            return;
        }

        if (brewTime >= MAX_BREW_TIME) {
            RegistryAccess access = level.registryAccess();
            this.brew(active.value(), access);
            return;
        }

        brewTime++;
    }

    private void setTimeToEvent() {
        if (this.level != null) {
            timeToNextEvent = getRandomHighNumber(this.level.getRandom(), MIN_TIME_FOR_EVENT, MAX_TIME_FOR_EVENT);
        }
    }

    public static int getRandomHighNumber(RandomSource rnd, int lowerBound, int upperBound) {
        int range = upperBound - lowerBound + 1;
        return upperBound - (int) (Math.pow(rnd.nextDouble(), 1.5) * range);
    }

    private boolean canBrew(@Nullable Recipe<?> recipe) {
        if (recipe == null || this.level == null) return false;
        BlockState blockState = this.level.getBlockState(this.getBlockPos());
        return recipe instanceof BrewingRecipe brewingRecipe &&
                blockState.getValue(BlockStateRegistry.MATERIAL).getLevel() >= brewingRecipe.getMaterial().getLevel() &&
                blockState.getValue(BlockStateRegistry.LIQUID) != Liquid.EMPTY &&
                this.level.getBlockState(BrewHelper.getBlock(ObjectRegistry.BREW_OVEN.get(), this.components, this.level)).getValue(BlockStateRegistry.HEAT) != Heat.OFF;
    }

    private void brew(Recipe<?> recipe, RegistryAccess access) {
        ItemStack resultStack = recipe.getResultItem(access);
        if (resultStack.getItem() instanceof DrinkBlockItem drinkItem) {
            assert this.level != null;

            BrewMaterial material = this.level.getBlockState(this.getBlockPos()).getValue(BlockStateRegistry.MATERIAL);
            int solvedEvents = this.solved;
            int totalBrewEvents = this.totalEvents;

            int quality;
            int count;
            if (material == BrewMaterial.NETHERITE) {
                quality = 3;
                count = 3;
            } else {
                if (solvedEvents <= 0) {
                    quality = 0;
                } else if (totalBrewEvents > 0 && solvedEvents >= totalBrewEvents) {
                    quality = 3;
                } else if (solvedEvents >= 2 && solvedEvents <= 4) {
                    quality = 2;
                } else {
                    quality = 1;
                }
                count = solvedEvents == 0 ? 1 : solvedEvents + 1;
            }

            DrinkBlockItem.addQuality(resultStack, quality);
            drinkItem.addCount(resultStack, count);
        }
        this.beer = resultStack;
        spawnElementals();
        endBrewing();
        if (this.level != null) {
            BlockState blockState = this.level.getBlockState(this.getBlockPos());
            this.level.setBlockAndUpdate(this.getBlockPos(), blockState.setValue(BlockStateRegistry.LIQUID, Liquid.BEER));
            BlockPos ovenPos = BrewHelper.getBlock(ObjectRegistry.BREW_OVEN.get(), this.components, level);
            BlockState ovenState = this.level.getBlockState(ovenPos);
            this.level.setBlockAndUpdate(ovenPos, ovenState.setValue(BlockStateRegistry.HEAT, Heat.OFF));
            BlockPos timerPos = BrewHelper.getBlock(ObjectRegistry.BREW_TIMER.get(), this.components, level);
            BlockState timerState = this.level.getBlockState(timerPos);
            this.level.setBlockAndUpdate(timerPos, timerState.setValue(BlockStateRegistry.TIME, false));
        }
        for (Ingredient ingredient : recipe.getIngredients()) {
            for (int i = 0; i < 3; i++) {
                ItemStack itemStack = this.ingredients.get(i);
                if (ingredient.test(itemStack)) {
                    this.removeItem(i, 1);
                    break;
                }
            }
        }
    }

    private void spawnElementals() {
        if (this.level == null) return;
        BlockState state = this.level.getBlockState(this.getBlockPos());
        BrewMaterial material = state.getValue(BlockStateRegistry.MATERIAL);
        boolean overflowUnresolved = overflowStarted > overflowSolved;
        int failed = Math.max(0, this.totalEvents - this.solved);
        boolean highFailRate = this.totalEvents > 0 && (failed * 2) > this.totalEvents;

        int count;
        if (highFailRate) {
            count = this.level.getRandom().nextInt(2, 4);
        } else if (overflowUnresolved) {
            count = material == BrewMaterial.COPPER ? 1 : 2;
        } else {
            count = 0;
        }

        if (count <= 0) return;

        BlockPos base = BrewHelper.getBlock(ObjectRegistry.BREW_OVEN.get(), this.components, level);
        if (base == null) return;

        for (int n = 0; n < count; n++) {
            BeerElementalEntity e = new BeerElementalEntity(EntityTypeRegistry.BEER_ELEMENTAL.get(), this.level);
            double ox = this.level.getRandom().nextInt(-1, 2) + 0.5;
            double oz = this.level.getRandom().nextInt(-1, 2) + 0.5;
            e.setPos(base.getX() + ox, base.getY() + 1, base.getZ() + oz);
            e.setHealth(20.0F);
            this.level.addFreshEntity(e);
            this.level.playSound(null, e.blockPosition(), spawnEntitySound, SoundSource.BLOCKS, 1.0F, 1.0F);
        }

        overflowStarted = 0;
        overflowSolved = 0;
    }

    public void onEventFinished(BrewEvent event, boolean success) {
        ResourceLocation id = BrewEvents.getId(event);
        if (id == null) return;
        if (id.equals(BrewEvents.KETTLE_EVENT)) {
            if (success) overflowSolved++;
        }
    }

    private int computeEventQuota() {
        BrewMaterial material = this.getBlockState().getValue(BlockStateRegistry.MATERIAL);
        assert this.level != null;
        RandomSource rnd = this.level.getRandom();
        return switch (material) {
            case WOOD -> rnd.nextInt(8, 13);
            case COPPER -> rnd.nextInt(4, 7);
            case NETHERITE -> rnd.nextInt(1, 3);
        };
    }

    public void endBrewing() {
        BrewHelper.finishEvents(this);
        this.brewTime = 0;
        this.solved = 0;
        this.totalEvents = 0;
        this.soundTime = SOUND_DURATION;
        this.timeToNextEvent = Integer.MIN_VALUE;
        this.eventQuota = -1;
        this.overflowStarted = 0;
        this.overflowSolved = 0;
    }

    public boolean isPartOf(BlockPos blockPos) {
        return components.contains(blockPos);
    }

    @Override
    public void saveAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        if (!this.components.isEmpty()) {
            GeneralUtil.putBlockPoses(compoundTag, this.components);
        }
        ContainerHelper.saveAllItems(compoundTag, this.ingredients, provider);
        if (!this.beer.isEmpty()) {
            compoundTag.put("beer", this.beer.save(provider, new CompoundTag()));
        }
        compoundTag.putInt("solved", this.solved);
        compoundTag.putInt("brewTime", this.brewTime);
        compoundTag.putInt("totalEvents", this.totalEvents);
        compoundTag.putInt("timeToNextEvent", this.timeToNextEvent);
        compoundTag.putInt("eventQuota", this.eventQuota);
        compoundTag.putInt("overflowStarted", this.overflowStarted);
        compoundTag.putInt("overflowSolved", this.overflowSolved);
        BrewHelper.saveAdditional(this, compoundTag);
    }

    @Override
    public void loadAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        this.components = GeneralUtil.readBlockPoses(compoundTag);
        this.ingredients = NonNullList.withSize(3, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(compoundTag, this.ingredients, provider);
        if (compoundTag.contains("beer")) {
            this.beer = ItemStack.parseOptional(provider, compoundTag.getCompound("beer"));
        }
        this.solved = compoundTag.getInt("solved");
        this.brewTime = compoundTag.getInt("brewTime");
        this.totalEvents = compoundTag.getInt("totalEvents");
        this.timeToNextEvent = compoundTag.getInt("timeToNextEvent");
        this.eventQuota = compoundTag.getInt("eventQuota");
        this.overflowStarted = compoundTag.getInt("overflowStarted");
        this.overflowSolved = compoundTag.getInt("overflowSolved");
        BrewHelper.load(this, compoundTag);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        CompoundTag compoundTag = new CompoundTag();
        this.saveAdditional(compoundTag, provider);
        return compoundTag;
    }

    public void growSolved() {
        this.solved++;
    }

    public Set<BrewEvent> getRunningEvents() {
        return runningEvents;
    }

    public @NotNull Set<BlockPos> getComponents() {
        return components;
    }

    public List<ItemStack> getIngredient() {
        return this.ingredients;
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return ingredients;
    }

    @Override
    public boolean stillValid(Player player) {
        if (this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        } else {
            return player.distanceToSqr((double) this.worldPosition.getX() + 0.5, (double) this.worldPosition.getY() + 0.5, (double) this.worldPosition.getZ() + 0.5) <= 64.0;
        }
    }

    private @Nullable RecipeHolder<BrewingRecipe> findActiveRecipe(Level level) {
        List<RecipeHolder<BrewingRecipe>> recipeHolders = level.getRecipeManager().getAllRecipesFor(RecipeTypeRegistry.BREWING_RECIPE_TYPE.get());
        for (RecipeHolder<BrewingRecipe> holder : recipeHolders) {
            BrewingRecipe r = holder.value();
            if (canBrew(r) && ingredientsMatch(r)) {
                return holder;
            }
        }
        return null;
    }

    private boolean ingredientsMatch(BrewingRecipe recipe) {
        List<Ingredient> req = recipe.getIngredients();
        boolean[] used = new boolean[this.ingredients.size()];
        int matched = 0;
        for (Ingredient ing : req) {
            boolean found = false;
            for (int i = 0; i < this.ingredients.size(); i++) {
                if (!used[i] && ing.test(this.ingredients.get(i))) {
                    used[i] = true;
                    found = true;
                    matched++;
                    break;
                }
            }
            if (!found) return false;
        }
        return matched == req.size();
    }
}
