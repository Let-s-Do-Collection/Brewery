package net.satisfy.brewery.core.effect;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.satisfy.brewery.Brewery;
import net.satisfy.brewery.platform.PlatformHelper;

public class StoutHeartEffect extends MobEffect {
    private static final ResourceLocation KNOCKBACK_ID = Brewery.identifier("stoutheart_knockback");

    public StoutHeartEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x8B6A3E);
        addAttributeModifier(Attributes.KNOCKBACK_RESISTANCE, KNOCKBACK_ID, 0.4D, AttributeModifier.Operation.ADD_VALUE);
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (livingEntity.level().isClientSide) {
            return true;
        }

        float maxHealth = livingEntity.getMaxHealth();
        float capHealth = maxHealth * PlatformHelper.getStoutHeartEffectHealthCap() / 100.0F;
        float currentHealth = livingEntity.getHealth();

        if (currentHealth >= capHealth) {
            return true;
        }

        float healPerTick = (float) PlatformHelper.getStoutHeartEffectHealAmount() + (0.1F * amplifier);
        float missingToCap = capHealth - currentHealth;

        livingEntity.heal(Math.min(healPerTick, missingToCap));
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % 10 == 0;
    }
}