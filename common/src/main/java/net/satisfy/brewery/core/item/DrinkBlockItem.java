package net.satisfy.brewery.core.item;

import net.satisfy.brewery.core.registry.ArmorSetRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.satisfy.brewery.Brewery;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.satisfy.brewery.platform.PlatformHelper;
import net.satisfy.brewery.core.registry.MobEffectRegistry;
import net.satisfy.brewery.core.registry.ObjectRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import net.satisfy.foundation.food.PlaceableDrinkItem;

public class DrinkBlockItem extends PlaceableDrinkItem {

    private final MobEffect effect;
    private final int baseDuration;

    private static final int MAX_QUALITY = 3;
    private static final ResourceLocation QUALITY_FONT = Brewery.identifier("quality");
    private static final String QUALITY_FULL = "\uE000";
    private static final String QUALITY_EMPTY = "\uE001";
    private static final String QUALITY_AGED = "\uE002";
    public static final int AGED_QUALITY = 4;

    public DrinkBlockItem(MobEffect effect, int duration, Block block, Properties settings) {
        super(block, settings);
        crouchToPlace().drinkAnytime();
        this.effect = effect;
        this.baseDuration = duration;
    }

    public static boolean isBottled(ItemStack stack) {
        return BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath().startsWith("whiskey_");
    }

    public static Item getContainer(ItemStack stack) {
        return isBottled(stack) ? Items.GLASS_BOTTLE : ObjectRegistry.BEER_MUG.get().asItem();
    }

    public static void addQuality(ItemStack itemStack, int quality) {
        CustomData customData = itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        CompoundTag tag = customData.copyTag();
        tag.putInt("brewery.beer_quality", Mth.clamp(quality, 0, AGED_QUALITY));
        itemStack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    public static int getQuality(ItemStack itemStack) {
        CustomData customData = itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        return customData.contains("brewery.beer_quality") ? customData.copyTag().getInt("brewery.beer_quality") : MAX_QUALITY;
    }

    @Override
    protected Item containerFor(ItemStack stack) {
        return getContainer(stack);
    }

    @Override
    protected void applyEffects(ItemStack itemStack, Level level, LivingEntity livingEntity) {
        if (livingEntity instanceof ServerPlayer serverPlayer) {
            int quality = getQuality(itemStack);

            MobEffectInstance mainEffect = calculateEffectForQuality(quality);
            var holder = BuiltInRegistries.MOB_EFFECT.wrapAsHolder(effect);
            var current = serverPlayer.getEffect(holder);
            int currentAmp = current != null ? current.getAmplifier() : -1;
            int newAmp = Mth.clamp(Math.max(mainEffect.getAmplifier(), currentAmp + 1), 0, 5);
            serverPlayer.addEffect(new MobEffectInstance(holder, mainEffect.getDuration(), newAmp, false, false));

            if (PlatformHelper.isDrunkennessEnabled() && !ArmorSetRegistry.hasHarddrinking(serverPlayer)) {
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
                } else if (quality >= AGED_QUALITY) {
                    max = 2400;
                } else {
                    max = 3600;
                }

                int drunkDuration = Mth.nextInt(level.getRandom(), min, max);
                serverPlayer.addEffect(new MobEffectInstance(drunkHolder, drunkDuration, newDrunkAmp, false, false));
            }
        }
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
            case 4 -> {
                durationMultiplier = 7;
                amplifier = 3;
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
                amplifier,
                false,
                false
        );
    }

    public void addCount(ItemStack resultSack, int solved) {
        resultSack.setCount(solved);
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 32;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        int beerQuality = MAX_QUALITY;
        CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        if (customData.contains("brewery.beer_quality")) {
            beerQuality = customData.copyTag().getInt("brewery.beer_quality");
            String mugs = beerQuality >= AGED_QUALITY ? QUALITY_FULL.repeat(MAX_QUALITY) + QUALITY_AGED : QUALITY_FULL.repeat(beerQuality) + QUALITY_EMPTY.repeat(Math.max(0, MAX_QUALITY - beerQuality));
            Component icons = Component.literal(mugs).withStyle(Style.EMPTY.withFont(QUALITY_FONT).withColor(ChatFormatting.WHITE));
            tooltip.add(Component.translatable("tooltip.brewery.beer_quality", icons).withStyle(ChatFormatting.GOLD));
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
    }
}
