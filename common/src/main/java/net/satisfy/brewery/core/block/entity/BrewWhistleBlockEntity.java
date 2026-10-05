package net.satisfy.brewery.core.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.brewery.core.registry.EntityTypeRegistry;

public class BrewWhistleBlockEntity extends BlockEntity {
    public BrewWhistleBlockEntity(BlockPos pos, BlockState state) {
        super(EntityTypeRegistry.BREW_WHISTLE_BLOCK_ENTITY.get(), pos, state);
    }
}
