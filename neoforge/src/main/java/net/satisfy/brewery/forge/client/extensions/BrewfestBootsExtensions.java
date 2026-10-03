package net.satisfy.brewery.forge.client.extensions;

import net.satisfy.foundation.armor.TexturedArmorItem;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.satisfy.brewery.core.registry.ArmorRegistry;
import org.jetbrains.annotations.NotNull;

public class BrewfestBootsExtensions implements IClientItemExtensions {
    @Override
    public @NotNull Model getGenericArmorModel(@NotNull LivingEntity entity, @NotNull ItemStack stack, @NotNull EquipmentSlot slot, @NotNull HumanoidModel<?> original) {
        if (slot != EquipmentSlot.FEET || !(stack.getItem() instanceof TexturedArmorItem boots)) return original;
        return ArmorRegistry.getBootsModel(boots, original.rightLeg, original.leftLeg, original);
    }
}