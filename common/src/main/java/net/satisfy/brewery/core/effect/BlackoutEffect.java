package net.satisfy.brewery.core.effect;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.satisfy.brewery.platform.PlatformHelper;
import net.satisfy.brewery.core.registry.MobEffectRegistry;

public class BlackoutEffect extends MobEffect {
    private static final int BLACK_PHASE_TICKS = 140;
    private static final int TELEPORT_AT_REMAINING = 40;
    private static final int UNLOCK_AT_REMAINING = 20;
    private static final Map<UUID, Float> LOCK_YAW = new HashMap<>();

    public BlackoutEffect() {
        super(MobEffectCategory.HARMFUL, 0x111111);
    }

    @Override
    public void onEffectAdded(LivingEntity livingEntity, int amplifier) {
        Level level = livingEntity.level();
        BlockState state = livingEntity.getBlockStateOn();
        var sound = state.getBlock().getSoundType(state).getFallSound();
        livingEntity.playSound(sound, 1.0F, 1.0F);
        level.playSound(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), sound, SoundSource.PLAYERS, 1.0F, 1.0F);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        Holder<MobEffect> holder = BuiltInRegistries.MOB_EFFECT.wrapAsHolder(MobEffectRegistry.BLACKOUT.get());
        MobEffectInstance self = livingEntity.getEffect(holder);
        if (self != null) {
            int remaining = self.getDuration();
            if (remaining == BLACK_PHASE_TICKS) {
                LOCK_YAW.put(livingEntity.getUUID(), livingEntity.getYRot());
                livingEntity.setXRot(-75F);
                livingEntity.xRotO = livingEntity.getXRot();
            }
            if (remaining <= BLACK_PHASE_TICKS && remaining > UNLOCK_AT_REMAINING) {
                Float yaw = LOCK_YAW.get(livingEntity.getUUID());
                if (yaw != null) {
                    livingEntity.setYRot(yaw);
                    livingEntity.yRotO = livingEntity.getYRot();
                    livingEntity.setYHeadRot(yaw);
                    livingEntity.yHeadRotO = livingEntity.getYHeadRot();
                    livingEntity.setYBodyRot(yaw);
                    livingEntity.yBodyRotO = livingEntity.yBodyRot;
                }
                livingEntity.setXRot(-75F);
                livingEntity.xRotO = livingEntity.getXRot();
                livingEntity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, remaining, 0, false, false, false));
                livingEntity.addEffect(new MobEffectInstance(MobEffects.DARKNESS, remaining, 1, false, false, false));
                if (livingEntity.getPose() != Pose.SLEEPING) {
                    livingEntity.setPose(Pose.SLEEPING);
                }
            }
            if (remaining == TELEPORT_AT_REMAINING && PlatformHelper.isBlackoutTeleportEnabled() && livingEntity.level() instanceof ServerLevel serverLevel) {
                int range = PlatformHelper.getBlackoutTeleportRange();
                double x = livingEntity.getX() + Mth.nextInt(livingEntity.getRandom(), -range, range);
                double z = livingEntity.getZ() + Mth.nextInt(livingEntity.getRandom(), -range, range);
                int gx = Mth.floor(x);
                int gz = Mth.floor(z);
                int gy = serverLevel.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, gx, gz);
                BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(gx, gy, gz);
                while (gy < serverLevel.getMaxBuildHeight() - 2 && (!serverLevel.getBlockState(pos).isAir() || !serverLevel.getBlockState(pos.above()).isAir())) {
                    gy++;
                    pos.setY(gy);
                }
                livingEntity.teleportTo(gx + 0.5D, gy, gz + 0.5D);
            }
            if (remaining == UNLOCK_AT_REMAINING) {
                livingEntity.removeEffect(MobEffects.BLINDNESS);
                livingEntity.removeEffect(MobEffects.DARKNESS);
                livingEntity.setPose(Pose.STANDING);
                LOCK_YAW.remove(livingEntity.getUUID());
            }
            if (remaining == 1) {
                if (!livingEntity.level().isClientSide()) {
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 600, 0, false, false));
                }
                livingEntity.setPose(Pose.STANDING);
                LOCK_YAW.remove(livingEntity.getUUID());
            }
        }
        if (!livingEntity.level().isClientSide()) {
            if (livingEntity.tickCount % 10 == 0) {
                double s = 0.05D;
                double dx = (livingEntity.getRandom().nextDouble() - 0.5D) * s;
                double dz = (livingEntity.getRandom().nextDouble() - 0.5D) * s;
                livingEntity.push(dx, 0.0D, dz);
            }
        }
        return true;
    }
}
