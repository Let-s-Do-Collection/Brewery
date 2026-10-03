package net.satisfy.brewery.core.event;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.core.Holder;
import net.satisfy.brewery.core.registry.ArmorSetRegistry;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.LootEvent;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.event.events.common.TickEvent;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.EntityHitResult;
import net.satisfy.brewery.core.registry.MobEffectRegistry;
import org.jetbrains.annotations.Nullable;

public class CommonEvents {
    private static final String HALEY_ACTIVE = "brewery:haley_active";
    private static final String HALEY_SET_MAYFLY = "brewery:haley_set_mayfly";

    public static void init() {
        LootEvent.MODIFY_LOOT_TABLE.register(CommonEvents::onModifyLootTable);
        PlayerEvent.ATTACK_ENTITY.register(CommonEvents::onPlayerAttack);
        TickEvent.PLAYER_PRE.register(CommonEvents::tickHaley);
        TickEvent.PLAYER_POST.register(CommonEvents::tickHarddrinking);
    }

    private static void tickHarddrinking(Player player) {
        if (player.level().isClientSide() || !ArmorSetRegistry.hasHarddrinking(player)) {
            return;
        }
        Holder<MobEffect> drunk = MobEffectRegistry.holder(MobEffectRegistry.DRUNK);
        if (player.hasEffect(drunk)) {
            player.removeEffect(drunk);
        }
    }

    public static void onModifyLootTable(ResourceKey<LootTable> key, LootEvent.LootTableModificationContext context, boolean builtin) {
        LoottableInjector.InjectLoot(key, context);
    }

    public static EventResult onPlayerAttack(Player player, Level level, Entity target, InteractionHand hand, @Nullable EntityHitResult result) {
        if (player.hasEffect(BuiltInRegistries.MOB_EFFECT.wrapAsHolder(MobEffectRegistry.PROTECTIVETOUCH.get()))) {
            handleProtectiveTouch(level, target);
            return EventResult.interruptFalse();
        }
        if (player.hasEffect(BuiltInRegistries.MOB_EFFECT.wrapAsHolder(MobEffectRegistry.HEALINGTOUCH.get()))) {
            handleHealingTouch(level, target);
            return EventResult.interruptFalse();
        }
        if (player.hasEffect(BuiltInRegistries.MOB_EFFECT.wrapAsHolder(MobEffectRegistry.RENEWINGTOUCH.get()))) {
            handleRenewingTouch(level, target);
            return EventResult.interruptFalse();
        }
        if (player.hasEffect(BuiltInRegistries.MOB_EFFECT.wrapAsHolder(MobEffectRegistry.TOXICTOUCH.get()))) {
            handleToxicTouch(level, target);
            return EventResult.pass();
        }
        if (player.hasEffect(BuiltInRegistries.MOB_EFFECT.wrapAsHolder(MobEffectRegistry.LIGHTNING_STRIKE.get()))) {
            handlelightningStrike(level, target);
            return EventResult.pass();
        }
        if (player.hasEffect(BuiltInRegistries.MOB_EFFECT.wrapAsHolder(MobEffectRegistry.EXPLOSION.get()))) {
            handleExplosiveTouch(level, target, player);
            return EventResult.pass();
        }
        return EventResult.pass();
    }

    private static void tickHaley(Player player) {
        if (player.level().isClientSide) return;
        if (player.isCreative() || player.isSpectator()) return;
        boolean has = player.hasEffect(BuiltInRegistries.MOB_EFFECT.wrapAsHolder(MobEffectRegistry.HALEY.get()));
        if (has) {
            if (!player.getTags().contains(HALEY_ACTIVE)) {
                player.addTag(HALEY_ACTIVE);
                if (!player.getAbilities().mayfly) {
                    player.addTag(HALEY_SET_MAYFLY);
                    player.getAbilities().mayfly = true;
                    player.onUpdateAbilities();
                }
            }
        } else {
            if (player.getTags().contains(HALEY_ACTIVE)) {
                player.removeTag(HALEY_ACTIVE);
                if (player.getTags().contains(HALEY_SET_MAYFLY)) {
                    player.removeTag(HALEY_SET_MAYFLY);
                    if (player.getAbilities().mayfly || player.getAbilities().flying) {
                        player.getAbilities().mayfly = false;
                        player.getAbilities().flying = false;
                        player.onUpdateAbilities();
                    }
                }
            }
        }
    }

    private static void handleRenewingTouch(Level level, Entity target) {
        if (target instanceof LivingEntity entity) {
            entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 1));
            spawnParticles(level, entity, ParticleTypes.COMPOSTER);
        }
    }

    private static void handleProtectiveTouch(Level level, Entity target) {
        if (target instanceof LivingEntity entity) {
            entity.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 200, 1));
            spawnParticles(level, entity, ParticleTypes.GLOW_SQUID_INK);
        }
    }

    private static void handleToxicTouch(Level level, Entity target) {
        if (target instanceof LivingEntity entity) {
            entity.addEffect(new MobEffectInstance(MobEffects.POISON, 300, 2));
            spawnParticles(level, entity, ParticleTypes.SCRAPE);
        }
    }

    private static void handleHealingTouch(Level level, Entity target) {
        if (target instanceof LivingEntity entity) {
            ((LivingEntity) target).heal(6.0f);
            spawnParticles(level, entity, ParticleTypes.HEART);
        }
    }

    private static void handlelightningStrike(Level level, Entity target) {
        float chance = level.isThundering() ? 0.75f : 0.1f;
        if (level.random.nextFloat() < chance) {
            LightningBolt lightningBolt = new LightningBolt(EntityType.LIGHTNING_BOLT, level);
            lightningBolt.moveTo(target.getX(), target.getY(), target.getZ());
            level.addFreshEntity(lightningBolt);
        }
    }

    private static void handleExplosiveTouch(Level level, Entity target, Player player) {
        if (level.random.nextFloat() < 0.1) {
            Fireball fireball = EntityType.FIREBALL.create(level);
            double dx = target.getX() - player.getX();
            double dy = target.getY() + target.getBbHeight() / 2 - (player.getY() + player.getBbHeight() / 2);
            double dz = target.getZ() - player.getZ();
            assert fireball != null;
            fireball.setPos(player.getX(), player.getY() + player.getBbHeight() / 2, player.getZ());
            fireball.shoot(dx, dy, dz, 1.5f, 0);
            level.addFreshEntity(fireball);
        }
    }

    private static void spawnParticles(Level level, Entity target, ParticleOptions particleType) {
        double height = target.getBbHeight();
        double x = target.getX();
        double y = target.getY();
        double z = target.getZ();
        for (double i = 2.5; i >= 1.5; i -= 0.5) {
            level.addParticle(particleType, x, y + height / i, z, 0, 0, 0);
        }
    }
}
