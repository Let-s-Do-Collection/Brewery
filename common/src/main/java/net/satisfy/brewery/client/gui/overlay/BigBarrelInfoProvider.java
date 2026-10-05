package net.satisfy.brewery.client.gui.overlay;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.satisfy.brewery.core.block.BigBarrelBlock;
import net.satisfy.brewery.core.block.entity.BigBarrelBlockEntity;
import net.satisfy.brewery.core.item.DrinkBlockItem;
import net.satisfy.brewery.platform.PlatformHelper;
import net.satisfy.foundation.overlay.BlockInfoProvider;
import net.satisfy.foundation.overlay.InfoSection;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BigBarrelInfoProvider implements BlockInfoProvider {
    private static final ResourceLocation DUNGAREES = ResourceLocation.fromNamespaceAndPath("farm_and_charm", "dungarees");

    @Override
    public List<InfoSection> describe(Level level, BlockPos pos, BlockState state, @Nullable BlockHitResult hit) {
        if (!(state.getBlock() instanceof BigBarrelBlock) || isHidden()) {
            return List.of();
        }
        if (!(level.getBlockEntity(BigBarrelBlock.mainPos(state, pos)) instanceof BigBarrelBlockEntity barrel) || barrel.isEmpty()) {
            return List.of();
        }
        List<InfoSection.Row> rows = new ArrayList<>();
        for (int slot = 0; slot < BigBarrelBlockEntity.SLOTS; slot++) {
            ItemStack drink = barrel.getDrink(slot);
            if (drink.isEmpty()) {
                continue;
            }
            Component status = barrel.getQuality(slot) >= DrinkBlockItem.AGED_QUALITY
                    ? Component.translatable("hud.brewery.barrel_aged").withStyle(ChatFormatting.GOLD)
                    : Component.translatable("hud.brewery.barrel_aging", barrel.getAgingPercent(slot)).withStyle(ChatFormatting.YELLOW);
            Component name = drink.getHoverName().copy().append(drink.getCount() > 1 ? " ×" + drink.getCount() : "");
            rows.add(InfoSection.Row.item(drink, name.copy().append(" ").append(status)));
        }
        return List.of(
                InfoSection.rows(Component.translatable("hud.brewery.in_barrel"), rows),
                InfoSection.title(Component.translatable("hud.brewery.barrel_hint").withStyle(ChatFormatting.GRAY))
        );
    }

    private static boolean isHidden() {
        if (!PlatformHelper.infoTooltipsNeedDungarees()) {
            return false;
        }
        Player player = Minecraft.getInstance().player;
        return player == null || !BuiltInRegistries.ITEM.getKey(player.getItemBySlot(EquipmentSlot.LEGS).getItem()).equals(DUNGAREES);
    }
}
