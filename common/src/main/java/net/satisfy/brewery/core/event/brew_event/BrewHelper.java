package net.satisfy.brewery.core.event.brew_event;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.brewery.core.block.BrewKettleBlock;
import net.satisfy.brewery.core.block.entity.BrewstationBlockEntity;
import net.satisfy.brewery.core.block.property.Liquid;
import net.satisfy.brewery.core.registry.BlockStateRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Supplier;

public class BrewHelper {

    @Nullable
    public static BlockPos getBlock(Block block, Set<BlockPos> components, Level level) {
        for (BlockPos pos : components) {
            if (level.getBlockState(pos).getBlock() == block) {
                return pos;
            }
        }
        return null;
    }

    @Nullable
    public static BlockPos getBlock(Class<?> clazz, Set<BlockPos> components, Level level) {
        for (BlockPos pos : components) {
            if (clazz.isInstance(level.getBlockState(pos).getBlock())) {
                return pos;
            }
        }
        return null;
    }

    public static List<ResourceLocation> possibleEvents(BrewstationBlockEntity entity) {
        List<ResourceLocation> possibleEvents = new ArrayList<>();
        List<ResourceLocation> runningEvents = BrewEvents.toLocations(entity.getRunningEvents());

        for (ResourceLocation event : BrewEvents.BREW_EVENTS.keySet()) {
            boolean canAdd = true;
            if (runningEvents.contains(event)) {
                canAdd = false;
            } else {
                for (ResourceLocation runningEvent : runningEvents) {
                    if (conflictsWith(event, runningEvent)) {
                        canAdd = false;
                        break;
                    }
                }
            }

            if (canAdd) {
                possibleEvents.add(event);
            }
        }
        return possibleEvents;
    }

    private static boolean conflictsWith(ResourceLocation event1, ResourceLocation event2) {
        return (event1.equals(BrewEvents.WHISTLE_EVENT) && event2.equals(BrewEvents.KETTLE_EVENT)) ||
                (event1.equals(BrewEvents.KETTLE_EVENT) && event2.equals(BrewEvents.WHISTLE_EVENT));
    }

    public static BrewEvent getRdmEvent(BrewstationBlockEntity entity) {
        List<ResourceLocation> possibleEvents = possibleEvents(entity);
        if (possibleEvents.isEmpty()) {
            return null;
        }
        ResourceLocation eventLocation = possibleEvents.get(Objects.requireNonNull(entity.getLevel()).getRandom().nextInt(possibleEvents.size()));

        Supplier<BrewEvent> type = BrewEvents.byId(eventLocation);
        assert type != null;
        BrewEvent event = type.get();
        event.setTimeForEvent(entity.getLevel().getRandom().nextInt(8 * 20, 20 * 20));
        return event;
    }

    public static void finishEvents(BrewstationBlockEntity entity) {
        Set<BrewEvent> eventSet = entity.getRunningEvents();
        if (eventSet.isEmpty()) return;
        Iterator<BrewEvent> iterator = eventSet.iterator();
        while (iterator.hasNext()) {
            BrewEvent event = iterator.next();
            if (event == null) continue;
            endEvent(entity, iterator, event);
        }
    }

    public static void checkRunningEvents(BrewstationBlockEntity entity) {
        Set<BrewEvent> eventSet = entity.getRunningEvents();
        if (eventSet.isEmpty()) return;
        Iterator<BrewEvent> iterator = eventSet.iterator();
        while (iterator.hasNext()) {
            BrewEvent event = iterator.next();
            if (event == null) continue;
            event.tick(entity);
            if (event.isFinish(entity.getComponents(), entity.getLevel())) {
                entity.onEventFinished(event, true);
                endEvent(entity, iterator, event);
                entity.growSolved();
            } else if (event.getTimeLeft() <= 0) {
                entity.onEventFinished(event, false);
                endEvent(entity, iterator, event);
            }
        }
    }

    public static void endEvent(BrewstationBlockEntity entity, Iterator<BrewEvent> iterator, BrewEvent event) {
        if (event == null) return;
        event.finish(entity.getComponents(), entity.getLevel());
        iterator.remove();
    }

    public static void saveAdditional(BrewstationBlockEntity entity, CompoundTag compoundTag) {
        Set<BrewEvent> events = entity.getRunningEvents();
        if (events.isEmpty()) return;
        ListTag list = new ListTag();
        for (BrewEvent event : events) {
            CompoundTag tag = event.save(new CompoundTag());
            tag.putString("id", Objects.requireNonNull(BrewEvents.getId(event)).toString());
            tag.putInt("timeLeft", event.getTimeLeft());
            list.add(tag);
        }
        compoundTag.put("runningEvents", list);
    }

    public static void load(BrewstationBlockEntity entity, CompoundTag compoundTag) {
        if (!compoundTag.contains("runningEvents")) return;
        ListTag list = compoundTag.getList("runningEvents", Tag.TAG_COMPOUND);
        for (Tag tag : list) {
            if (tag instanceof CompoundTag cTag) {
                String id = cTag.getString("id");
                ResourceLocation location = ResourceLocation.tryParse(id);
                Supplier<BrewEvent> type = BrewEvents.byId(location);
                if (type == null) continue;
                BrewEvent event = type.get();
                event.load(cTag);
                event.setTimeForEvent(cTag.getInt("timeLeft"));
                entity.getRunningEvents().add(event);
            }
        }
    }

    public static void resetWater(Set<BlockPos> components, Level level) {
        if (components == null || level == null) return;
        BlockPos basinPos = BrewHelper.getBlock(BrewKettleBlock.class, components, level);
        if (basinPos != null) {
            BlockState basinState = level.getBlockState(basinPos);
            Liquid liquid = basinState.getValue(BlockStateRegistry.LIQUID);
            if (liquid.equals(Liquid.FILLED)) return;
            level.setBlockAndUpdate(basinPos, basinState.setValue(BlockStateRegistry.LIQUID, Liquid.FILLED));
        }
    }
}
