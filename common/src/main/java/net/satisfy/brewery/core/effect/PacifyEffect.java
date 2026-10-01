package net.satisfy.brewery.core.effect;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.Holder;
import org.jetbrains.annotations.NotNull;
import net.satisfy.brewery.platform.PlatformHelper;

public class PacifyEffect extends MobEffect {
    public PacifyEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public @NotNull MobEffect addAttributeModifier(Holder<Attribute> holder, ResourceLocation id, double amount, AttributeModifier.Operation op) {
        return super.addAttributeModifier(holder, id, amount, op);
    }

    @Override
    public void removeAttributeModifiers(AttributeMap map) {
        super.removeAttributeModifiers(map);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity instanceof Player player) {
            player.level().getEntitiesOfClass(EnderMan.class, player.getBoundingBox().inflate(PlatformHelper.getPacifyEffectRange())).forEach(e -> e.setTarget(null));
        }
        return true;
    }

    @Override
    public void onMobRemoved(LivingEntity entity, int amplifier, Entity.RemovalReason reason) {
    }
}
