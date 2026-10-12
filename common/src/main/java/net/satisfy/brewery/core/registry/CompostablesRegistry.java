package net.satisfy.brewery.core.registry;

import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.world.level.ItemLike;
import net.satisfy.foundation.compostable.FoundationCompostables;

public class CompostablesRegistry {
    public static void init() {
        registerCompostableItem(ObjectRegistry.DRIED_WHEAT, 0.6F);
        registerCompostableItem(ObjectRegistry.DRIED_OAT, 0.6F);
        registerCompostableItem(ObjectRegistry.DRIED_CORN, 0.6F);
        registerCompostableItem(ObjectRegistry.DRIED_BARLEY, 0.6F);
        registerCompostableItem(ObjectRegistry.WILD_HOPS, 0.5F);
        registerCompostableItem(ObjectRegistry.HOPS, 0.4F);
        registerCompostableItem(ObjectRegistry.DUMPLINGS, 0.6F);
        registerCompostableItem(ObjectRegistry.FRIED_CHICKEN, 0.3F);
        registerCompostableItem(ObjectRegistry.HALF_CHICKEN, 0.3F);
        registerCompostableItem(ObjectRegistry.GINGERBREAD, 0.3F);
        registerCompostableItem(ObjectRegistry.PRETZEL, 0.3F);
        registerCompostableItem(ObjectRegistry.MASHED_POTATOES, 0.3F);
        registerCompostableItem(ObjectRegistry.SAUSAGE, 0.3F);
        registerCompostableItem(ObjectRegistry.PORK_KNUCKLE, 0.3F);
        registerCompostableItem(ObjectRegistry.POTATO_SALAD, 0.3F);
    }

    public static void registerCompostableItem(RegistrySupplier<? extends ItemLike> item, float chance) {
        FoundationCompostables.register(chance, item);
    }
}
