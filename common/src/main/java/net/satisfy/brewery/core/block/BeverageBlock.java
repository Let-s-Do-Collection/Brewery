package net.satisfy.brewery.core.block;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.satisfy.brewery.core.registry.EntityTypeRegistry;
import net.satisfy.foundation.storage.StorageBlock;
import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.brewery.Brewery;
import net.satisfy.foundation.storage.StorageBlockEntity;
import net.satisfy.brewery.core.item.DrinkBlockItem;
import net.satisfy.brewery.core.registry.StorageTypeRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BeverageBlock extends StorageBlock {
    @Override
    public BlockEntityType<?> blockEntityType() {
        return EntityTypeRegistry.STORAGE_ENTITY.get();
    }

    private static final VoxelShape SHAPE = Shapes.box(0.125, 0, 0.125, 0.875, 0.875, 0.875);
    public static final TagKey<Item> SMALL_BOTTLE = TagKey.create(Registries.ITEM, Brewery.identifier("small_bottle"));
    public static final BooleanProperty FAKE_MODEL = BooleanProperty.create("fake_model");

    private final int maxCount;

    public BeverageBlock(Properties settings, int maxCount) {
        super(settings);
        this.maxCount = maxCount;
        this.registerDefaultState(this.defaultBlockState().setValue(FAKE_MODEL, true));
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!(world.getBlockEntity(pos) instanceof StorageBlockEntity beerEntity) || !canInsertStack(stack)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        NonNullList<ItemStack> inventory = beerEntity.getInventory();
        int posInE = getFirstEmptySlot(inventory);
        if (!willFitStack(stack, inventory) || posInE == Integer.MIN_VALUE) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        if (!world.isClientSide()) {
            beerEntity.setStack(posInE, stack.split(1));
            if (player.isCreative()) {
                stack.grow(1);
            }
            world.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
        return ItemInteractionResult.sidedSuccess(world.isClientSide());
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        if (!(world.getBlockEntity(pos) instanceof StorageBlockEntity beerEntity)) {
            return InteractionResult.PASS;
        }
        NonNullList<ItemStack> inventory = beerEntity.getInventory();
        int posInE = getLastFullSlot(inventory);
        if (posInE == Integer.MIN_VALUE) {
            return InteractionResult.PASS;
        }
        if (!world.isClientSide()) {
            ItemStack beer = beerEntity.removeStack(posInE);
            if (!player.getInventory().add(beer)) {
                player.drop(beer, false);
            }
            if (isEmpty(inventory)) {
                world.destroyBlock(pos, false);
            }
            world.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
        return InteractionResult.sidedSuccess(world.isClientSide());
    }

    public boolean isEmpty(NonNullList<ItemStack> inventory){
        for(ItemStack stack : inventory){
            if(!stack.isEmpty()) return false;
        }
        return true;
    }

    public int getFirstEmptySlot(NonNullList<ItemStack> inventory){
        for(ItemStack stack : inventory){
            if(stack.isEmpty()) return inventory.indexOf(stack);
        }
        return Integer.MIN_VALUE;
    }

    public int getLastFullSlot(NonNullList<ItemStack> inventory){
        for(int i = inventory.size() - 1; i >=0; i--){
            if(!inventory.get(i).isEmpty()) return i;
        }
        return Integer.MIN_VALUE;
    }


    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FAKE_MODEL);
    }

    @Override
    public @NotNull BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2) {
        if (direction == Direction.DOWN && !blockState.canSurvive(levelAccessor, blockPos)) {
            levelAccessor.destroyBlock(blockPos, true);
        }
        return super.updateShape(blockState, direction, blockState2, levelAccessor, blockPos, blockPos2);
    }
    @Override
    public int size() {
        return maxCount;
    }
    @Override
    public ResourceLocation type() {
        return StorageTypeRegistry.BEVERAGE;
    }

    @Override
    public boolean canInsertStack(ItemStack stack) {
        return stack.is(SMALL_BOTTLE);
    }


    public boolean willFitStack(ItemStack itemStack, NonNullList<ItemStack> inventory) {
        Pair<Integer, Integer> p = getFilledAmountAndBiggest(inventory);
        int biggest = p.getSecond();
        int count = p.getFirst();
        int stackCount = getCount(itemStack);
        if(biggest == Integer.MAX_VALUE) return true;

        return stackCount > count && count < biggest;
    }

    public static Pair<Integer, Integer> getFilledAmountAndBiggest(NonNullList<ItemStack> inventory){
        int count = 0;
        int biggest = Integer.MAX_VALUE;
        for(ItemStack stack : inventory){
            if(!stack.isEmpty()){
                count++;
                if(stack.getItem() instanceof DrinkBlockItem item && item.getBlock() instanceof BeverageBlock beer && beer.maxCount < biggest){
                    biggest = beer.maxCount;
                }
            }
        }
        return new Pair<>(count, biggest);
    }

    public static int getCount(ItemStack itemStack){
        if(itemStack.getItem() instanceof DrinkBlockItem item && item.getBlock() instanceof BeverageBlock beer){
            return beer.maxCount;
        }
        return Integer.MIN_VALUE;
    }

    @Override
    public int getSection(Float aFloat, Float aFloat1) {
        return 0;
    }

    @Override
    public Direction[] unAllowedDirections() {
        return new Direction[0];
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
        list.add(Component.translatable("tooltip.farm_and_charm.canbeplaced").withStyle(ChatFormatting.GRAY));
    }
}
