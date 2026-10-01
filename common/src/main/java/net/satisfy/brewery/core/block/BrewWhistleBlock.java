package net.satisfy.brewery.core.block;

import net.satisfy.foundation.util.ShapeUtil;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.brewery.core.block.property.BrewMaterial;
import net.satisfy.brewery.core.registry.BlockStateRegistry;
import net.satisfy.brewery.core.registry.SoundEventRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class BrewWhistleBlock extends BrewingstationBlock {
    public static final BooleanProperty WHISTLE;
    public static final EnumProperty<DoubleBlockHalf> HALF;
    public static final Map<Direction, VoxelShape> BOTTOM_SHAPE;
    public static final Map<Direction, VoxelShape> TOP_SHAPE;
    private static final Supplier<VoxelShape> bottomVoxelShapeSupplier;
    private static final Supplier<VoxelShape> topVoxelShapeSupplier;

    static {
        WHISTLE = BlockStateRegistry.WHISTLE;
        HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
        bottomVoxelShapeSupplier = () -> {
            VoxelShape shape = Shapes.empty();
            shape = Shapes.or(shape, Shapes.box(0, 0.125, 0, 1, 1, 0.125));
            shape = Shapes.or(shape, Shapes.box(0, 0.125, 0.125, 0.125, 1, 1));
            shape = Shapes.or(shape, Shapes.box(0.125, 0, 0.125, 1, 0.125, 1));
            shape = Shapes.or(shape, Shapes.box(0.125, 0.9375, 0.125, 1, 1, 1));
            return shape;
        };
        topVoxelShapeSupplier = () -> {
            VoxelShape shape = Shapes.empty();
            shape = Shapes.or(shape, Shapes.box(0.1875, 0, 0.25, 0.4375, 1, 0.5));
            shape = Shapes.or(shape, Shapes.box(0.125, 0.5, 0.1875, 0.5, 0.5625, 0.5625));
            shape = Shapes.or(shape, Shapes.box(0.125, 0.875, 0.1875, 0.5, 0.9375, 0.5625));
            shape = Shapes.or(shape, Shapes.box(0.125, 0.5, 0.5625, 0.5, 0.75, 0.625));
            shape = Shapes.or(shape, Shapes.box(0.15625, 0.59375, 0.21875, 0.46875, 0.84375, 0.53125));
            return shape;
        };
        BOTTOM_SHAPE = Util.make(new HashMap<>(), map -> {
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                map.put(direction, ShapeUtil.rotateShape(Direction.NORTH, direction, bottomVoxelShapeSupplier.get()));
            }
        });
        TOP_SHAPE = Util.make(new HashMap<>(), map -> {
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                map.put(direction, ShapeUtil.rotateShape(Direction.NORTH, direction, topVoxelShapeSupplier.get()));
            }
        });
    }

    private long lastSoundTime = 0;

    public BrewWhistleBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(MATERIAL, BrewMaterial.WOOD).setValue(WHISTLE, false).setValue(HALF, DoubleBlockHalf.LOWER));
    }

    @Override
    public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        if (blockState.getValue(HALF) == DoubleBlockHalf.LOWER) {
            level.setBlockAndUpdate(blockPos.above(), blockState.setValue(HALF, DoubleBlockHalf.UPPER));
        }
    }

    public @NotNull BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2) {
        DoubleBlockHalf doubleBlockHalf = blockState.getValue(HALF);
        if (direction.getAxis() == Direction.Axis.Y && doubleBlockHalf == DoubleBlockHalf.LOWER == (direction == Direction.UP)) {
            return blockState2.is(this) && blockState2.getValue(HALF) != doubleBlockHalf ? blockState.setValue(FACING, blockState2.getValue(FACING)) : Blocks.AIR.defaultBlockState();
        } else {
            return doubleBlockHalf == DoubleBlockHalf.LOWER && direction == Direction.DOWN && !blockState.canSurvive(levelAccessor, blockPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(blockState, direction, blockState2, levelAccessor, blockPos, blockPos2);
        }
    }

    @Override
    public @NotNull BlockState playerWillDestroy(Level level, BlockPos blockPos, BlockState blockState, Player player) {
        if (blockState.getValue(HALF).equals(DoubleBlockHalf.UPPER)) {
            blockPos = blockPos.below();
        }
        return super.playerWillDestroy(level, blockPos, blockState, player);
    }

    @Override
    public @NotNull ItemStack getCloneItemStack(LevelReader blockGetter, BlockPos blockPos, BlockState blockState) {
        if (blockState.getValue(HALF).equals(DoubleBlockHalf.UPPER)) {
            blockPos = blockPos.below();
        }
        return super.getCloneItemStack(blockGetter, blockPos, blockState);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Map<Direction, VoxelShape> shapeMap = state.getValue(HALF) == DoubleBlockHalf.LOWER ? BOTTOM_SHAPE : TOP_SHAPE;
        return shapeMap.get(state.getValue(FACING));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(WHISTLE, HALF);
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource rand) {
        if (!state.getValue(WHISTLE)) {
            return;
        }

        if (state.getValue(HALF) != DoubleBlockHalf.UPPER) {
            return;
        }


        Direction direction = state.getValue(FACING);

        double offsetX = 0.5 + direction.getStepX() * 0.6;
        double offsetY = 0.8;
        double offsetZ = 0.5 + direction.getStepZ() * 0.6;

        double x = pos.getX() + offsetX;
        double y = pos.getY() + offsetY;
        double z = pos.getZ() + offsetZ;

        double speedX = direction.getStepX() * 0.1 + (rand.nextFloat() - 0.5) * 0.05;
        double speedY = 0.5;
        double speedZ = direction.getStepZ() * 0.1 + (rand.nextFloat() - 0.5) * 0.05;

        for (int i = 0; i < 5; i++) {
            world.addParticle(ParticleTypes.LARGE_SMOKE, x, y, z, speedX, speedY, speedZ);
        }
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastSoundTime >= 3000) {
            world.playLocalSound(x, y, z, SoundEventRegistry.BREWSTATION_WHISTLE.get(), SoundSource.BLOCKS, 1.0F, 1.0F, false);
            lastSoundTime = currentTime;
        }
    }
}
