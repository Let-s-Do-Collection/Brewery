package net.satisfy.brewery.core.registry;

import net.minecraft.world.entity.LivingEntity;
import net.satisfy.foundation.armor.ArmorSet;

public final class ArmorSetRegistry {
    private static ArmorSet brewfest;

    private ArmorSetRegistry() {
    }

    public static void init() {
        brewfest = ArmorSet.builder("tooltip.brewery.armor.brewfest_set")
                .piece(ObjectRegistry.BREWFEST_HAT, ObjectRegistry.BREWFEST_HAT_RED)
                .piece(ObjectRegistry.BREWFEST_REGALIA, ObjectRegistry.BREWFEST_BLOUSE)
                .piece(ObjectRegistry.BREWFEST_TROUSERS, ObjectRegistry.BREWFEST_DRESS)
                .piece(ObjectRegistry.BREWFEST_BOOTS, ObjectRegistry.BREWFEST_SHOES)
                .bonus("tooltip.brewery.armor.brewfest_set.bonus")
                .register();
    }

    public static boolean hasHarddrinking(LivingEntity entity) {
        return brewfest != null && brewfest.isComplete(entity);
    }
}
