package net.satisfy.brewery.core.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.brewery.core.block.entity.StorageBlockEntity;
import net.satisfy.brewery.core.registry.MobEffectRegistry;
import net.satisfy.brewery.core.registry.ObjectRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class DrinkBlockItem extends BlockItem {

    private final MobEffect effect;
    private final int baseDuration;

    public DrinkBlockItem(MobEffect effect, int duration, Block block, Properties settings) {
        super(block, settings);
        this.effect = effect;
        this.baseDuration = duration;
    }

    public static void addQuality(ItemStack itemStack, int quality) {
        CustomData customData = itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        CompoundTag tag = customData.copyTag();
        tag.putInt("brewery.beer_quality", Mth.clamp(quality, 0, 3));
        itemStack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    @Override
    protected BlockState getPlacementState(BlockPlaceContext context) {
        if (!Objects.requireNonNull(context.getPlayer()).isCrouching()) {
            return null;
        }

        BlockState blockState = this.getBlock().getStateForPlacement(context);
        return blockState != null && this.canPlace(context, blockState) ? blockState : null;
    }

    @Override
    protected boolean updateCustomBlockEntityTag(BlockPos blockPos, Level level, @Nullable Player player, ItemStack itemStack, BlockState blockState) {
        if (level.getBlockEntity(blockPos) instanceof StorageBlockEntity beerEntity) {
            beerEntity.setStack(0, itemStack.copyWithCount(1));
        }
        return super.updateCustomBlockEntityTag(blockPos, level, player, itemStack, blockState);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        return ItemUtils.startUsingInstantly(level, player, interactionHand);
    }

    @Override
    public @NotNull ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity livingEntity) {
        ItemStack returnStack = super.finishUsingItem(itemStack, level, livingEntity);
        if (livingEntity instanceof Player player && !player.isCreative()) {
            player.addItem(new ItemStack(ObjectRegistry.BEER_MUG.get()));
        }
        if (livingEntity instanceof ServerPlayer serverPlayer) {
            int quality = itemStack.has(DataComponents.CUSTOM_DATA) && Objects.requireNonNull(itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY)).contains("brewery.beer_quality")
                    ? itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getInt("brewery.beer_quality")
                    : 1;

            MobEffectInstance mainEffect = calculateEffectForQuality(quality);
            var holder = BuiltInRegistries.MOB_EFFECT.wrapAsHolder(effect);
            var current = serverPlayer.getEffect(holder);
            int currentAmp = current != null ? current.getAmplifier() : -1;
            int newAmp = Mth.clamp(Math.max(mainEffect.getAmplifier(), currentAmp + 1), 0, 5);
            serverPlayer.addEffect(new MobEffectInstance(holder, mainEffect.getDuration(), newAmp));

            var drunkHolder = MobEffectRegistry.holder(MobEffectRegistry.DRUNK);
            var drunkCurrent = serverPlayer.getEffect(drunkHolder);
            int drunkAmp = drunkCurrent != null ? drunkCurrent.getAmplifier() : -1;
            int newDrunkAmp = Mth.clamp(drunkAmp + 1, 0, 5);

            int min = 1800;
            int max;
            if (quality <= 1) {
                max = 9600;
            } else if (quality == 2) {
                max = 6000;
            } else {
                max = 3600;
            }

            int drunkDuration = Mth.nextInt(level.getRandom(), min, max);
            serverPlayer.addEffect(new MobEffectInstance(drunkHolder, drunkDuration, newDrunkAmp));
        }
        return returnStack;
    }


    @NotNull
    private MobEffectInstance calculateEffectForQuality(int quality) {
        int durationMultiplier;
        int amplifier;

        switch (quality) {
            case 0 -> {
                durationMultiplier = 0;
                amplifier = 0;
            }
            case 2 -> {
                durationMultiplier = 3;
                amplifier = 1;
            }
            case 3 -> {
                durationMultiplier = 5;
                amplifier = 2;
            }
            default -> {
                durationMultiplier = 1;
                amplifier = 0;
            }
        }

        int duration = quality == 0 ? baseDuration / 3 : baseDuration * durationMultiplier;

        return new MobEffectInstance(
                BuiltInRegistries.MOB_EFFECT.wrapAsHolder(effect),
                duration,
                amplifier
        );
    }

    public void addCount(ItemStack resultSack, int solved) {
        resultSack.setCount(solved);
    }

    @Override
    public @NotNull UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 32;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        int beerQuality = 1;
        if (stack.has(DataComponents.CUSTOM_DATA)) {
            CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
            if (customData.contains("brewery.beer_quality")) {
                beerQuality = customData.copyTag().getInt("brewery.beer_quality");
            }
        }

        MobEffectInstance instance = calculateEffectForQuality(beerQuality);

        if (this.effect != null) {
            MutableComponent effectName = Component.translatable(this.effect.getDescriptionId());
            if (instance.getAmplifier() > 0) {
                effectName.append(" ").append(Component.translatable("potion.potency." + instance.getAmplifier()));
            }

            MutableComponent line = instance.getDuration() > 20
                    ? Component.translatable("potion.withDuration", effectName, MobEffectUtil.formatDuration(instance, 1.0F, context.tickRate()))
                    : effectName;

            tooltip.add(line.withStyle(this.effect.getCategory().getTooltipFormatting()));
        } else {
            tooltip.add(Component.translatable("effect.none").withStyle(ChatFormatting.GRAY));
        }

        if (beerQuality != 1) {
            tooltip.add(Component.translatable("tooltip.brewery.beer_quality", beerQuality).withStyle(ChatFormatting.GOLD));
        }
    }
}
