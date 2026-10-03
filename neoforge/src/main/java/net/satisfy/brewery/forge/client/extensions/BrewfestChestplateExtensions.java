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

public class BrewfestChestplateExtensions implements IClientItemExtensions {
    @Override
    public @NotNull Model getGenericArmorModel(@NotNull LivingEntity entity, @NotNull ItemStack stack, @NotNull EquipmentSlot slot, @NotNull HumanoidModel<?> original) {
        if (slot != EquipmentSlot.CHEST || !(stack.getItem() instanceof TexturedArmorItem chest)) return original;
        return ArmorRegistry.getChestplateModel(chest, original.body, original.leftArm, original.rightArm, original.leftLeg, original.rightLeg, original);
    }
}