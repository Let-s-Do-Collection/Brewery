package net.satisfy.brewery.core.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.brewery.core.item.DrinkBlockItem;
import net.satisfy.brewery.core.registry.EntityTypeRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BigBarrelBlockEntity extends BlockEntity {
    public static final int SLOTS = 9;
    public static final long TICKS_PER_QUALITY = 7L * 24000L;

    private final NonNullList<ItemStack> drinks = NonNullList.withSize(SLOTS, ItemStack.EMPTY);
    private final long[] storedAt = new long[SLOTS];

    public BigBarrelBlockEntity(BlockPos pos, BlockState state) {
        super(EntityTypeRegistry.BIG_BARREL_BLOCK_ENTITY.get(), pos, state);
    }

    public static boolean canAge(ItemStack stack) {
        return stack.getItem() instanceof DrinkBlockItem;
    }

    public ItemStack getDrink(int slot) {
        return drinks.get(slot);
    }

    public boolean isEmpty() {
        return drinks.stream().allMatch(ItemStack::isEmpty);
    }

    public boolean insert(ItemStack stack) {
        if (level == null || !canAge(stack)) {
            return false;
        }
        long now = level.getGameTime();
        for (int slot = 0; slot < SLOTS && !stack.isEmpty(); slot++) {
            ItemStack stored = drinks.get(slot);
            int room = stored.getMaxStackSize() - stored.getCount();
            if (stored.isEmpty() || room <= 0 || !ItemStack.isSameItemSameComponents(stored, stack)) {
                continue;
            }
            int moved = Math.min(room, stack.getCount());
            storedAt[slot] = (storedAt[slot] * stored.getCount() + now * moved) / (stored.getCount() + moved);
            stored.grow(moved);
            stack.shrink(moved);
        }
        if (stack.isEmpty()) {
            sync();
            return true;
        }
        for (int slot = 0; slot < SLOTS; slot++) {
            if (drinks.get(slot).isEmpty()) {
                drinks.set(slot, stack.split(stack.getCount()));
                storedAt[slot] = level.getGameTime();
                sync();
                return true;
            }
        }
        return false;
    }

    public ItemStack takeLast() {
        for (int slot = SLOTS - 1; slot >= 0; slot--) {
            if (!drinks.get(slot).isEmpty()) {
                ItemStack aged = aged(slot);
                drinks.set(slot, ItemStack.EMPTY);
                storedAt[slot] = 0L;
                sync();
                return aged;
            }
        }
        return ItemStack.EMPTY;
    }

    public NonNullList<ItemStack> takeAll() {
        NonNullList<ItemStack> all = NonNullList.create();
        for (int slot = 0; slot < SLOTS; slot++) {
            if (!drinks.get(slot).isEmpty()) {
                all.add(aged(slot));
                drinks.set(slot, ItemStack.EMPTY);
            }
        }
        return all;
    }

    public int getQuality(int slot) {
        return Math.min(DrinkBlockItem.AGED_QUALITY, DrinkBlockItem.getQuality(drinks.get(slot)) + (int) (elapsed(slot) / TICKS_PER_QUALITY));
    }

    public int getAgingPercent(int slot) {
        if (getQuality(slot) >= DrinkBlockItem.AGED_QUALITY) {
            return 100;
        }
        return (int) (elapsed(slot) % TICKS_PER_QUALITY * 100L / TICKS_PER_QUALITY);
    }

    private long elapsed(int slot) {
        return level == null ? 0L : Math.max(0L, level.getGameTime() - storedAt[slot]);
    }

    private ItemStack aged(int slot) {
        ItemStack stack = drinks.get(slot).copy();
        DrinkBlockItem.addQuality(stack, getQuality(slot));
        return stack;
    }

    private void sync() {
        setChanged();
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        for (int slot = 0; slot < SLOTS; slot++) {
            drinks.set(slot, ItemStack.EMPTY);
        }
        ContainerHelper.loadAllItems(tag, drinks, provider);
        long[] times = tag.getLongArray("StoredAt");
        for (int slot = 0; slot < SLOTS; slot++) {
            storedAt[slot] = slot < times.length ? times[slot] : 0L;
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        ContainerHelper.saveAllItems(tag, drinks, true, provider);
        tag.putLongArray("StoredAt", storedAt);
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
