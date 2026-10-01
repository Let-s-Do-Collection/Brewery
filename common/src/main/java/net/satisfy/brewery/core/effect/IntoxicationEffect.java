package net.satisfy.brewery.core.effect;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.satisfy.brewery.platform.PlatformHelper;
import net.satisfy.brewery.Brewery;
import net.satisfy.brewery.core.registry.MobEffectRegistry;

public class IntoxicationEffect extends MobEffect {
    private static final ResourceLocation SPEED_ID = Brewery.identifier("effect.intoxication.speed");
    private static final int BLACKOUT_DURATION = 240;

    public IntoxicationEffect() {
        super(MobEffectCategory.HARMFUL, 0x9C6C3C);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, SPEED_ID, 0.0D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        int lvl = Math.max(0, amplifier);
        if (lvl >= 5 && PlatformHelper.isBlackoutEnabled() && !entity.level().isClientSide()) {
            if (entity.getRandom().nextDouble() < PlatformHelper.getBlackoutChance() / 100.0D) {
                Holder<MobEffect> intox = BuiltInRegistries.MOB_EFFECT.wrapAsHolder(MobEffectRegistry.DRUNK.get());
                Holder<MobEffect> blackout = BuiltInRegistries.MOB_EFFECT.wrapAsHolder(MobEffectRegistry.BLACKOUT.get());
                entity.removeEffect(intox);
                entity.addEffect(new MobEffectInstance(blackout, BLACKOUT_DURATION, 0));
                return true;
            }
        }
        if (lvl > 1 && PlatformHelper.isDrunkSwayEnabled()) {
            if (entity.level().isClientSide()) {
                float t = entity.tickCount * 0.03F;
                float f = 0.45F + 0.15F * lvl;
                float yaw = Mth.sin(t * f) * (0.3F + 0.35F * lvl);
                float pitch = Mth.cos(t * (f * 0.85F)) * (0.12F + 0.18F * lvl);
                entity.setYRot(entity.getYRot() + yaw * 0.5F);
                entity.setXRot(Mth.clamp(entity.getXRot() + pitch * 0.5F, -90F, 90F));
                entity.yRotO = Mth.lerp(0.6F, entity.yRotO, entity.getYRot());
                entity.xRotO = Mth.lerp(0.6F, entity.xRotO, entity.getXRot());
            } else {
                int interval = Math.max(18 - lvl * 2, 6);
                if (entity.tickCount % interval == 0) {
                    double s = 0.01D * lvl;
                    double dx = (entity.getRandom().nextDouble() - 0.5D) * s;
                    double dz = (entity.getRandom().nextDouble() - 0.5D) * s;
                    entity.push(dx, 0.0D, dz);
                }
            }
        }
        if (lvl >= 4 && !entity.level().isClientSide()) {
            if (entity.tickCount % 40 == 0) {
                entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 100, 0, false, false, false));
            }
        }
        return true;
    }


    @Override
    public void addAttributeModifiers(AttributeMap attributeMap, int amplifier) {
        AttributeInstance inst = attributeMap.getInstance(Attributes.MOVEMENT_SPEED);
        if (inst != null) {
            inst.removeModifier(SPEED_ID);
            double amount = amplifier <= 1 || !PlatformHelper.isDrunkSlownessEnabled() ? 0.0D : -0.05D * (amplifier - 1);
            if (amount != 0.0D) {
                inst.addPermanentModifier(new AttributeModifier(SPEED_ID, amount, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
            }
        }
    }

    @Override
    public void removeAttributeModifiers(AttributeMap attributeMap) {
        AttributeInstance inst = attributeMap.getInstance(Attributes.MOVEMENT_SPEED);
        if (inst != null) {
            inst.removeModifier(SPEED_ID);
        }
    }
}
