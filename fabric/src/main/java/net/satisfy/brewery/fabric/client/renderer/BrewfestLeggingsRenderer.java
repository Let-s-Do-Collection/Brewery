package net.satisfy.brewery.fabric.client.renderer;

import net.satisfy.foundation.armor.TexturedArmorItem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.satisfy.brewery.core.registry.ArmorRegistry;

public class BrewfestLeggingsRenderer implements ArmorRenderer {
    @Override
    public void render(PoseStack matrices, MultiBufferSource vertexConsumers, ItemStack stack, LivingEntity entity, EquipmentSlot slot, int light, HumanoidModel<LivingEntity> contextModel) {
        if (slot != EquipmentSlot.LEGS) return;
        if (!(stack.getItem() instanceof TexturedArmorItem brewfestLegsItem)) return;

        Model model = ArmorRegistry.getLeggingsModel(brewfestLegsItem, contextModel.leftLeg, contextModel.rightLeg, contextModel);

        ResourceLocation baseTexture = brewfestLegsItem.getTexture();
        String texturePath = baseTexture.getPath();
        if (!texturePath.startsWith("textures/")) texturePath = "textures/" + texturePath;
        if (!texturePath.endsWith(".png")) texturePath = texturePath + ".png";
        ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(baseTexture.getNamespace(), texturePath);

        model.renderToBuffer(matrices, vertexConsumers.getBuffer(model.renderType(texture)), light, OverlayTexture.NO_OVERLAY, -1);
    }
}