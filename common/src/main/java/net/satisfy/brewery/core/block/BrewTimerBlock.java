package net.satisfy.brewery.core.block;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.brewery.core.block.property.BrewMaterial;
import net.satisfy.brewery.core.registry.BlockStateRegistry;
import net.satisfy.brewery.core.registry.SoundEventRegistry;
import net.satisfy.farm_and_charm.core.util.GeneralUtil;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class BrewTimerBlock extends BrewingstationBlock {
    public static final BooleanProperty TIME;
    public static final BooleanProperty ACTIVATED;
    public static final BooleanProperty PRESSED;
    public static final Map<Direction, VoxelShape> SHAPE;
    private static final Supplier<VoxelShape> voxelShapeSupplier;

    static {
        TIME = BlockStateRegistry.TIME;
        PRESSED = BooleanProperty.create("pressed");
        ACTIVATED = BooleanProperty.create("activated");
        voxelShapeSupplier = () -> {
            VoxelShape shape = Shapes.empty();
            shape = Shapes.or(shape, Shapes.box(0.125, 0.5, 0.9375, 0.5, 0.875, 1));
            shape = Shapes.or(shape, Shapes.box(0.6875, 0.6875, 0.9375, 0.8125, 0.8125, 1));
            shape = Shapes.or(shape, Shapes.box(0.6875, 0.5, 0.9375, 0.8125, 0.625, 1));
            shape = Shapes.or(shape, Shapes.box(0, 0.125, 0.875, 1, 1, 0.9375));
            shape = Shapes.or(shape, Shapes.box(0.875, 0.125, 0, 1, 1, 0.875));
            shape = Shapes.or(shape, Shapes.box(0, 0, 0, 0.875, 0.125, 0.875));
            shape = Shapes.or(shape, Shapes.box(0, 0.9375, 0, 0.875, 1, 0.875));
            return shape;
        };
        SHAPE = Util.make(new HashMap<>(), map -> {
            for (Direction direction : Direction.Plane.HORIZONTAL.stream().toList()) {
                map.put(direction, GeneralUtil.rotateShape(Direction.NORTH, direction, voxelShapeSupplier.get()));
            }
        });
    }

    private long lastSoundTime = 0;

    public BrewTimerBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(MATERIAL, BrewMaterial.WOOD).setValue(TIME, false).setValue(ACTIVATED, false).setValue(PRESSED, false));
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult) {
        if (Boolean.TRUE.equals(blockState.getValue(TIME))) {
            level.setBlockAndUpdate(blockPos, blockState.setValue(TIME, false).setValue(ACTIVATED, false));
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE.get(state.getValue(FACING));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(TIME, ACTIVATED, PRESSED);
    }

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource) {
        if (Boolean.TRUE.equals(blockState.getValue(TIME))) {
            double x = blockPos.getX() + 0.5;
            double y = blockPos.getY() + 1.2;
            double z = blockPos.getZ() + 0.5;

            DustParticleOptions redDust = new DustParticleOptions(new Vector3f(1.0F, 0.0F, 0.0F), 1.0F);

            long currentTime = System.currentTimeMillis();
            if (currentTime - lastSoundTime >= 900) {
                level.playLocalSound(x, y, z, SoundEventRegistry.BREWSTATION_TIMER_LOOP.get(), SoundSource.BLOCKS, 1.0F, 1.0F, false);
                lastSoundTime = currentTime;
            }
            for (int i = 0; i < 4; i++) {
                double offsetX = randomSource.nextDouble() * 0.6D - 0.3D;
                double offsetZ = randomSource.nextDouble() * 0.6D - 0.3D;
                level.addParticle(redDust, x + offsetX, y, z + offsetZ, 1.0, 0.0, 0.0);
            }

            if (!Boolean.TRUE.equals(blockState.getValue(ACTIVATED))) {
                level.setBlock(blockPos, blockState.setValue(ACTIVATED, true), 3);
            }
        }
    }
}

