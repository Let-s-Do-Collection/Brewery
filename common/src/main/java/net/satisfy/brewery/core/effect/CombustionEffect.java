package net.satisfy.brewery.core.effect;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.satisfy.brewery.platform.PlatformHelper;

public class CombustionEffect extends MobEffect {
    public CombustionEffect(MobEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.getCommandSenderWorld() instanceof ServerLevel) {
            entity.blockPosition();
            entity.level().getEntities(entity, entity.getBoundingBox().inflate(PlatformHelper.getCombustionEffectRadius())).forEach(e -> {
                if (e instanceof Monster && entity.getRandom().nextFloat() * 100.0F < PlatformHelper.getCombustionEffectIgniteChance()) {
                    e.igniteForSeconds(5);
                }
            });
        }
        return super.applyEffectTick(entity, amplifier);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}
