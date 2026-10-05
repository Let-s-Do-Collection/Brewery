package net.satisfy.brewery.core.event.brew_event;

import net.satisfy.foundation.registry.FoundationParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.satisfy.brewery.core.block.BrewKettleBlock;
import net.satisfy.brewery.core.block.BrewWhistleBlock;
import net.satisfy.brewery.core.block.entity.BrewstationBlockEntity;
import net.satisfy.brewery.core.block.property.Liquid;
import net.satisfy.brewery.core.registry.BlockStateRegistry;
import net.satisfy.brewery.core.registry.ObjectRegistry;

import java.util.Set;

public class WhistleEvent extends BrewEvent {
    @Override
    public CompoundTag save(CompoundTag compoundTag) {
        return compoundTag;
    }

    @Override
    public void load(CompoundTag compoundTag) {

    }

    @Override
    public void onTick(BrewstationBlockEntity entity) {
        Level level = entity.getLevel();
        assert level != null;
        if (!level.isClientSide()) return;
        RandomSource randomSource = level.getRandom();
        if (!(getTimeLeft() % 3 == 0 && randomSource.nextFloat() < 0.05F)) return;

        BlockPos blockPos = BrewHelper.getBlock(BrewWhistleBlock.class, entity.getComponents(), level);
        BlockState blockState = level.getBlockState(blockPos);

        if (blockState.getValue(BrewWhistleBlock.WHISTLE) && blockState.getValue(BrewWhistleBlock.HALF) == DoubleBlockHalf.UPPER) {
            assert blockPos != null;
            double x = blockPos.getX() + 0.5D;
            double y = blockPos.getY();
            double z = blockPos.getZ() + 0.5D;
            if (randomSource.nextDouble() < 0.1D) {
                level.playLocalSound(x, y, z, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.8F, 0.5F, false);
            }
            double j = randomSource.nextDouble() * 12.0D / 16.0D;
            level.addParticle(FoundationParticles.SOUP_STEAM.get(), x, y + j, z, 0.0, 0.0, 0.0);
        }
    }

    @Override
    public void start(Set<BlockPos> components, Level level) {
        if (components.isEmpty() || level == null) return;
        BlockPos basinPos = BrewHelper.getBlock(BrewKettleBlock.class, components, level);
        BlockPos whistlePos = BrewHelper.getBlock(ObjectRegistry.BREW_WHISTLE.get(), components, level);
        if (basinPos != null && whistlePos != null) {
            BlockState basinState = level.getBlockState(basinPos);
            BlockState whistleState = level.getBlockState(whistlePos);
            level.setBlockAndUpdate(basinPos, basinState.setValue(BlockStateRegistry.LIQUID, Liquid.DRAINED));
            level.setBlockAndUpdate(whistlePos, whistleState.setValue(BlockStateRegistry.WHISTLE, true));
        }
    }

    @Override
    public boolean isFinish(Set<BlockPos> components, Level level) {
        if (components == null || level == null) return true;
        BlockPos basinPos = BrewHelper.getBlock(BrewKettleBlock.class, components, level);
        if (basinPos != null) {
            BlockState basinState = level.getBlockState(basinPos);
            return basinState.getValue(BlockStateRegistry.LIQUID) == Liquid.FILLED;
        }
        return true;
    }

    @Override
    public void finish(Set<BlockPos> components, Level level) {
        BlockPos whistlePos = BrewHelper.getBlock(ObjectRegistry.BREW_WHISTLE.get(), components, level);
        if (whistlePos != null) {
            BlockState whistleState = level.getBlockState(whistlePos);
            level.setBlock(whistlePos, whistleState.setValue(BlockStateRegistry.WHISTLE, false), 3);
        }

        BrewHelper.resetWater(components, level);
    }
}
