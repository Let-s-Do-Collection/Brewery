package net.satisfy.brewery.client.renderer.block;

import net.satisfy.foundation.storage.StorageTypeRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.brewery.core.block.BeverageBlock;
import net.satisfy.foundation.storage.StorageBlockEntity;
import net.satisfy.brewery.core.item.DrinkBlockItem;
import net.satisfy.foundation.render.ClientUtil;

@Environment(EnvType.CLIENT)
public class BeverageRenderer implements StorageTypeRenderer {
    private static BlockState getState(DrinkBlockItem item) {
        return item.getBlock().defaultBlockState().setValue(BeverageBlock.FAKE_MODEL, false);
    }

    @Override
    public void render(StorageBlockEntity entity, PoseStack poseStack, MultiBufferSource multiBufferSource, NonNullList<ItemStack> nonNullList) {
        poseStack.translate(-0.5, 0, -0.5);
        switch (getCount(nonNullList)) {
            case 1 -> renderOne(entity, poseStack, multiBufferSource, nonNullList);
            case 2 -> renderTwo(entity, poseStack, multiBufferSource, nonNullList);
            case 3 -> renderThree(entity, poseStack, multiBufferSource, nonNullList);
        }
    }

    public int getCount(NonNullList<ItemStack> nonNullList) {
        int count = 0;
        for (ItemStack stack : nonNullList) {
            if (!stack.isEmpty()) count++;
        }
        return count;
    }

    private void renderOne(StorageBlockEntity entity, PoseStack matrices, MultiBufferSource vertexConsumers, NonNullList<ItemStack> nonNullList) {
        if (nonNullList.get(0).getItem() instanceof DrinkBlockItem item) {
            ClientUtil.renderBlock(getState(item), matrices, vertexConsumers, entity);
        }
    }

    private void renderTwo(StorageBlockEntity entity, PoseStack matrices, MultiBufferSource vertexConsumers, NonNullList<ItemStack> nonNullList) {
        DrinkBlockItem item1 = nonNullList.get(0).getItem() instanceof DrinkBlockItem item ? item : null;
        DrinkBlockItem item2 = nonNullList.get(1).getItem() instanceof DrinkBlockItem item ? item : null;

        matrices.translate(-0.15f, 0f, -0.25f);
        if (item1 != null) {
            ClientUtil.renderBlock(getState(item1), matrices, vertexConsumers, entity);
        }
        matrices.translate(.1f, 0f, .8f);
        matrices.mulPose(Axis.YP.rotationDegrees(30));
        if (item2 != null) {
            ClientUtil.renderBlock(getState(item2), matrices, vertexConsumers, entity);
        }
    }

    private void renderThree(StorageBlockEntity entity, PoseStack matrices, MultiBufferSource vertexConsumers, NonNullList<ItemStack> nonNullList) {
        DrinkBlockItem item1 = nonNullList.get(0).getItem() instanceof DrinkBlockItem item ? item : null;
        DrinkBlockItem item2 = nonNullList.get(1).getItem() instanceof DrinkBlockItem item ? item : null;
        DrinkBlockItem item3 = nonNullList.get(1).getItem() instanceof DrinkBlockItem item ? item : null;
        matrices.translate(-0.25f, 0f, -0.25f);
        if (item1 != null) {
            ClientUtil.renderBlock(getState(item1), matrices, vertexConsumers, entity);
        }
        matrices.translate(.15f, 0f, .5f);
        if (item2 != null) {
            ClientUtil.renderBlock(getState(item2), matrices, vertexConsumers, entity);
        }
        if (item3 == null) return;
        matrices.translate(.1f, 0f, 0f);
        matrices.mulPose(Axis.YP.rotationDegrees(30));
        ClientUtil.renderBlock(getState(item3), matrices, vertexConsumers, entity);
    }
}
