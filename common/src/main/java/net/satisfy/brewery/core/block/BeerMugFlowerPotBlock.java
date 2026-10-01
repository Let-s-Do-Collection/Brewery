package net.satisfy.brewery.core.block;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.brewery.core.block.entity.BeerMugBlockEntity;
import net.satisfy.brewery.core.registry.ObjectRegistry;
import net.satisfy.foundation.block.FacingBlock;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public class BeerMugFlowerPotBlock extends FacingBlock implements EntityBlock {
    private static final VoxelShape SHAPE;

    private static final Supplier<VoxelShape> voxelShapeSupplier = () -> {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.joinUnoptimized(shape, Shapes.box(0.3125, 0, 0.3125, 0.6875, 0.5, 0.375), BooleanOp.OR);
        shape = Shapes.joinUnoptimized(shape, Shapes.box(0.3125, 0, 0.625, 0.6875, 0.5, 0.6875), BooleanOp.OR);
        shape = Shapes.joinUnoptimized(shape, Shapes.box(0.625, 0, 0.375, 0.6875, 0.5, 0.625), BooleanOp.OR);
        shape = Shapes.joinUnoptimized(shape, Shapes.box(0.3125, 0, 0.375, 0.375, 0.5, 0.625), BooleanOp.OR);
        shape = Shapes.joinUnoptimized(shape, Shapes.box(0.375, 0, 0.375, 0.625, 0.0625, 0.625), BooleanOp.OR);
        return shape;
    };

    static {
        SHAPE = voxelShapeSupplier.get();
    }

    public BeerMugFlowerPotBlock(Properties settings) {
        super(settings);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        if (context.getPlayer() == null || !context.getPlayer().isCrouching()) return null;
        return super.getStateForPlacement(context);
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (hand == InteractionHand.OFF_HAND) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        BeerMugBlockEntity be = (BeerMugBlockEntity) level.getBlockEntity(pos);
        if (be == null) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        ItemStack handStack = player.getItemInHand(hand);
        Item flower = be.getFlower();

        if (flower == null && handStack.isEmpty() && !player.isShiftKeyDown()) {
            if (!level.isClientSide) {
                player.addItem(new ItemStack(ObjectRegistry.BEER_MUG.get()));
                level.removeBlock(pos, false);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        } else if (player.isShiftKeyDown() && flower != null) {
            if (!level.isClientSide) {
                player.addItem(new ItemStack(flower));
                be.setFlower(null);
                level.sendBlockUpdated(pos, state, state, 3);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        } else if (!player.isShiftKeyDown() && handStack.isEmpty() && flower != null) {
            if (!level.isClientSide) {
                player.addItem(flower.getDefaultInstance());
                be.setFlower(null);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        } else if (!player.isShiftKeyDown() && fitInPot(handStack) && flower == null) {
            if (!level.isClientSide) {
                be.setFlower(handStack.getItem());
                if (!player.isCreative()) {
                    handStack.shrink(1);
                }
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }
        return super.useItemOn(itemStack, state, level, pos, player, hand, hit);
    }

    public boolean fitInPot(ItemStack item) {
        return item.is(ItemTags.SMALL_FLOWERS);
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof BeerMugBlockEntity be) {
                Item flower = be.getFlower();
                if (flower != null) {
                    Containers.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), flower.getDefaultInstance());
                }
                world.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(state, world, pos, newState, moved);
        }
    }

    @Override
    protected boolean isPathfindable(BlockState blockState, PathComputationType pathComputationType) {
        return false;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BeerMugBlockEntity(pos, state);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
        list.add(Component.translatable("tooltip.farm_and_charm.canbeplaced").withStyle(ChatFormatting.GRAY));
    }
}