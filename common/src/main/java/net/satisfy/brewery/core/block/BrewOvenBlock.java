package net.satisfy.brewery.core.block;

import net.satisfy.foundation.util.ShapeUtil;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.brewery.core.block.property.BrewMaterial;
import net.satisfy.brewery.core.block.property.Heat;
import net.satisfy.brewery.core.registry.BlockStateRegistry;
import net.satisfy.brewery.core.registry.SoundEventRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class BrewOvenBlock extends BrewingstationBlock {
    public static final EnumProperty<Heat> HEAT;
    public static final Map<Direction, VoxelShape> SHAPE;
    private static final Supplier<VoxelShape> voxelShapeSupplier;

    static {
        HEAT = BlockStateRegistry.HEAT;
        voxelShapeSupplier = () -> {
            VoxelShape shape = Shapes.empty();
            shape = Shapes.or(shape, Shapes.box(0.125, 0, 0, 1, 0.125, 0.875));
            shape = Shapes.or(shape, Shapes.box(0, 0.125, 0, 1, 1, 0.9375));
            return shape;
        };
        SHAPE = Util.make(new HashMap<>(), map -> {
            for (Direction direction : Direction.Plane.HORIZONTAL.stream().toList()) {
                map.put(direction, ShapeUtil.rotateShape(Direction.NORTH, direction, voxelShapeSupplier.get()));
            }
        });
    }

    private long lastSoundTime = 0;

    public BrewOvenBlock(Properties properties) {
        super(properties.lightLevel(state -> hasHeat(state) ? 13 : 0));
        this.registerDefaultState(this.defaultBlockState().setValue(MATERIAL, BrewMaterial.WOOD).setValue(HEAT, Heat.OFF));
    }

    private static boolean hasHeat(BlockState state) {
        return state.getValue(HEAT) != Heat.OFF;
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (blockState.getValue(HEAT) != Heat.LIT && AbstractFurnaceBlockEntity.isFuel(itemStack)) {
            level.setBlock(blockPos, blockState.setValue(HEAT, Heat.LIT), 3);
            level.playSound(null, blockPos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
            if (!player.isCreative()) {
                itemStack.shrink(1);
            }
            return ItemInteractionResult.SUCCESS;
        }
        return super.useItemOn(itemStack, blockState, level, blockPos, player, interactionHand, blockHitResult);
    }

    @Override
    public void stepOn(Level world, BlockPos pos, BlockState state, Entity entity) {
        boolean isHeated = state.getValue(HEAT) != Heat.OFF;

        if (isHeated && !entity.fireImmune() && entity instanceof Player) {
            entity.hurt(world.damageSources().inFire(), 1.0F);
        }
        super.stepOn(world, pos, state, entity);
    }

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource) {
        if (blockState.getValue(HEAT) != Heat.OFF) {
            double x = blockPos.getX() + 0.5D;
            double y = blockPos.getY() + 0.7D;
            double z = blockPos.getZ() + 0.5D;

            if (randomSource.nextDouble() < 0.1D) {
                level.playLocalSound(x, y, z, SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
            }

            Direction direction = blockState.getValue(FACING).getCounterClockWise();
            Direction.Axis axis = direction.getAxis();
            double h = randomSource.nextDouble() * 0.6D - 0.3D;
            double i = axis == Direction.Axis.X ? direction.getStepX() * 0.62D : h;
            double j = randomSource.nextDouble() * 9D / 16D;
            double k = axis == Direction.Axis.Z ? direction.getStepZ() * 0.62D : h;

            if (blockState.getValue(HEAT) == Heat.WEAK) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastSoundTime >= 9500) {
                    level.playLocalSound(x, y, z, SoundEventRegistry.BREWSTATION_OVEN.get(), SoundSource.BLOCKS, 0.70F, 1.0F, false);
                    lastSoundTime = currentTime;
                }
                if (randomSource.nextDouble() < 0.2D) {
                    for (int l = 0; l < 5; l++) {
                        level.addParticle(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, x + i, y + j, z + k, 0.0, 0.01, 0.0);
                    }
                }
            } else {
                level.addParticle(ParticleTypes.SMOKE, x + i, y + j, z + k, 0.0, 0.07, 0.0);
                level.addParticle(ParticleTypes.FLAME, x + i, y + j, z + k, 0.0, 0.0, 0.0);
            }
        }
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE.get(state.getValue(FACING));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(HEAT);
    }
}