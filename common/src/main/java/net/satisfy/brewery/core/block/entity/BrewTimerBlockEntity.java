package net.satisfy.brewery.core.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.brewery.core.registry.EntityTypeRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BrewTimerBlockEntity extends BlockEntity {
    private long pressTime = -100L;
    private float needleAngle = -60.0F;
    @Nullable
    private BlockPos stationPos;
    private long nextStationSearch;

    public BrewTimerBlockEntity(BlockPos pos, BlockState state) {
        super(EntityTypeRegistry.BREW_TIMER_BLOCK_ENTITY.get(), pos, state);
    }

    public void press() {
        if (level != null) {
            pressTime = level.getGameTime();
            setChanged();
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    public long getPressTime() {
        return pressTime;
    }

    public float updateNeedle(float target, float speed) {
        needleAngle += (target - needleAngle) * speed;
        return needleAngle;
    }

    @Nullable
    public BrewstationBlockEntity findStation() {
        if (level == null) {
            return null;
        }
        if (stationPos != null && level.getBlockEntity(stationPos) instanceof BrewstationBlockEntity station && station.isPartOf(worldPosition)) {
            return station;
        }
        stationPos = null;
        if (level.getGameTime() < nextStationSearch) {
            return null;
        }
        nextStationSearch = level.getGameTime() + 40L;
        for (BlockPos pos : BlockPos.betweenClosed(worldPosition.offset(-2, -2, -2), worldPosition.offset(2, 2, 2))) {
            if (level.getBlockEntity(pos) instanceof BrewstationBlockEntity station && station.isPartOf(worldPosition)) {
                stationPos = pos.immutable();
                return station;
            }
        }
        return null;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        pressTime = tag.contains("PressTime") ? tag.getLong("PressTime") : -100L;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.putLong("PressTime", pressTime);
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, provider);
        return tag;
    }
}
