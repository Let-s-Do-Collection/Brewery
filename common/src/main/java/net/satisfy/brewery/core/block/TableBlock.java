package net.satisfy.brewery.core.block;

import net.satisfy.foundation.block.LineConnectingType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.brewery.core.registry.ObjectRegistry;
import net.satisfy.foundation.block.LineConnectingBlock;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class TableBlock extends LineConnectingBlock implements SimpleWaterloggedBlock {
    public static final BooleanProperty WATERLOGGED;
    public static final BooleanProperty HAS_TABLECLOTH = BooleanProperty.create("has_tablecloth");
    public static final VoxelShape TOP_SHAPE;
    public static final VoxelShape[] LEG_SHAPES;

    static {
        WATERLOGGED = BlockStateProperties.WATERLOGGED;
        TOP_SHAPE = Block.box(0.0, 13.0, 0.0, 16.0, 16.0, 16.0);
        LEG_SHAPES = new VoxelShape[]{
                Block.box(7.0, 0.0, 7.0, 9.0, 13.0, 9.0),
                Block.box(7.0, 0.0, 7.0, 9.0, 13.0, 9.0),
                Block.box(7.0, 0.0, 7.0, 9.0, 13.0, 9.0),
                Block.box(7.0, 0.0, 7.0, 9.0, 13.0, 9.0)
        };
    }

    public TableBlock(BlockBehaviour.Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, false).setValue(HAS_TABLECLOTH, false));
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(ItemStack itemStack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (itemStack.getItem().equals(ObjectRegistry.PATTERNED_CARPET.get())) {
            if (!state.getValue(HAS_TABLECLOTH)) {
                world.setBlock(pos, state.setValue(HAS_TABLECLOTH, true), 3);
                if (!player.isCreative()) {
                    itemStack.shrink(1);
                }
                return ItemInteractionResult.sidedSuccess(world.isClientSide);
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!state.getValue(HAS_TABLECLOTH)) {
            return super.useWithoutItem(state, level, pos, player, hitResult);
        }

        level.setBlock(pos, state.setValue(HAS_TABLECLOTH, false), 3);

        if (!level.isClientSide) {
            ItemStack returnedCarpet = new ItemStack(ObjectRegistry.PATTERNED_CARPET.get());
            if (!player.addItem(returnedCarpet)) {
                player.drop(returnedCarpet, false);
            }
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(FACING);
        LineConnectingType type = state.getValue(TYPE);

        if (type == LineConnectingType.MIDDLE) {
            return TOP_SHAPE;
        }

        if ((direction == Direction.NORTH && type == LineConnectingType.LEFT) || (direction == Direction.SOUTH && type == LineConnectingType.RIGHT)) {
            return Shapes.or(TOP_SHAPE, LEG_SHAPES[0], LEG_SHAPES[3]);
        } else if ((direction == Direction.NORTH && type == LineConnectingType.RIGHT) || (direction == Direction.SOUTH && type == LineConnectingType.LEFT)) {
            return Shapes.or(TOP_SHAPE, LEG_SHAPES[1], LEG_SHAPES[2]);
        } else if ((direction == Direction.EAST && type == LineConnectingType.LEFT) || (direction == Direction.WEST && type == LineConnectingType.RIGHT)) {
            return Shapes.or(TOP_SHAPE, LEG_SHAPES[0], LEG_SHAPES[1]);
        } else if ((direction == Direction.EAST && type == LineConnectingType.RIGHT) || (direction == Direction.WEST && type == LineConnectingType.LEFT)) {
            return Shapes.or(TOP_SHAPE, LEG_SHAPES[2], LEG_SHAPES[3]);
        }
        return Shapes.or(TOP_SHAPE, LEG_SHAPES);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level world = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();
        return Objects.requireNonNull(super.getStateForPlacement(context)).setValue(WATERLOGGED, world.getFluidState(clickedPos).getType() == Fluids.WATER);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(WATERLOGGED, HAS_TABLECLOTH);
    }

    @Override
    public @NotNull FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }
}
