package net.satisfy.brewery.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.util.FastColor;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.satisfy.foundation.registry.FoundationParticles;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.brewery.core.block.BrewingstationBlock;
import net.satisfy.brewery.core.block.entity.BrewstationBlockEntity;
import net.satisfy.brewery.core.block.property.Liquid;
import net.satisfy.brewery.core.registry.BlockStateRegistry;
import net.satisfy.foundation.render.ClientUtil;
import org.joml.Quaternionf;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SuppressWarnings("unused")
public class BrewingstationRenderer implements BlockEntityRenderer<BrewstationBlockEntity> {
    private static final float ITEM_SCALE = 0.32f;
    private static final float RING_RADIUS = 0.19f;
    private static final float CENTER_X = 0.5f;
    private static final float CENTER_Z = 0.53f;
    private static final float TILT = (float) Math.toRadians(14);
    private static final float ITEM_THICKNESS = 0.012f;

    public BrewingstationRenderer(BlockEntityRendererProvider.Context ctx) {
    }

    @Override
    public void render(BrewstationBlockEntity entity, float partialTicks, PoseStack matrixStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {
        if (!entity.hasLevel() || !(entity.getBlockState().getBlock() instanceof BrewingstationBlock)) return;

        updateLiquid(entity);

        List<ItemStack> ingredients = new ArrayList<>();
        for (ItemStack stack : entity.getIngredient()) {
            if (!stack.isEmpty()) ingredients.add(stack);
        }
        if (ingredients.isEmpty()) return;

        float surface = surfaceHeight(entity.getBlockState());
        float time = entity.getLevel().getGameTime() + partialTicks;
        boolean floating = surface > 2f / 16f;
        Random random = new Random(entity.getBlockPos().hashCode());
        int count = ingredients.size();

        for (int index = 0; index < count; index++) {
            ItemStack stack = ingredients.get(index);
            float baseAngle = (float) (Math.PI * 2 * index / count);
            float drift = floating ? time * 0.012f : 0f;
            float ringAngle = baseAngle + drift;
            float radius = count == 1 ? 0f : RING_RADIUS;
            float bob = floating ? (float) Math.sin(time * 0.08f + index * 2.1f) * 0.008f : 0f;
            float spin = baseAngle * 1.7f + (floating ? time * 0.01f * (index % 2 == 0 ? 1 : -1) : 0f);

            matrixStack.pushPose();
            matrixStack.translate(CENTER_X + Math.cos(ringAngle) * radius, surface + bob, CENTER_Z + Math.sin(ringAngle) * radius);
            float tiltX = floating ? (float) Math.sin(time * 0.08f + index * 2.1f + 0.8f) * TILT : 0f;
            float tiltZ = floating ? (float) Math.sin(time * 0.065f + index * 1.3f) * TILT : 0f;
            matrixStack.mulPose(new Quaternionf().rotateX(tiltX).rotateZ(tiltZ).rotateY(spin).rotateX((float) Math.toRadians(90)));
            matrixStack.scale(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE);

            int layers = Math.min(stack.getCount() / 16 + 1, 4);
            for (int layer = 0; layer < layers; layer++) {
                matrixStack.pushPose();
                matrixStack.translate((random.nextFloat() - 0.5f) * 0.12f, (random.nextFloat() - 0.5f) * 0.12f, -layer * ITEM_THICKNESS / ITEM_SCALE);
                ClientUtil.renderItem(stack, matrixStack, bufferSource, entity);
                matrixStack.popPose();
            }
            matrixStack.popPose();
        }
    }

    /** The tint is baked into the chunk mesh, so rebuild it whenever the liquid color moved a step and spawn colored bubbles while brewing. */
    private void updateLiquid(BrewstationBlockEntity entity) {
        Level level = entity.getLevel();
        BlockState state = entity.getBlockState();
        if (!state.hasProperty(BlockStateRegistry.LIQUID)) return;
        Liquid liquid = state.getValue(BlockStateRegistry.LIQUID);
        if (liquid == Liquid.EMPTY || liquid == Liquid.DRAINED) return;

        int color = entity.getLiquidColor(BiomeColors.getAverageWaterColor(level, entity.getBlockPos()));
        int step = (color & 0xFFFFFF) >> 2 & 0x3F3F3F;
        if (step != entity.getClientTintStep()) {
            boolean first = entity.getClientTintStep() == -1;
            entity.setClientTintStep(step);
            if (!first) level.sendBlockUpdated(entity.getBlockPos(), state, state, 8);
        }

        boolean active = entity.isBrewingClient() || liquid == Liquid.OVERFLOWING;
        long now = level.getGameTime();
        if (!active || now == entity.getClientParticleTick()) return;
        entity.setClientParticleTick(now);
        RandomSource random = level.getRandom();
        if (random.nextFloat() > 0.25f) return;
        BlockPos pos = entity.getBlockPos();
        double x = pos.getX() + 0.2 + random.nextDouble() * 0.6;
        double z = pos.getZ() + 0.2 + random.nextDouble() * 0.6;
        double y = pos.getY() + surfaceHeight(state) + 0.02;
        level.addParticle(ColorParticleOption.create(FoundationParticles.COLORED_SOUP_BUBBLE.get(), FastColor.ARGB32.opaque(color)), x, y, z, 0, 0.01, 0);
    }

    private float surfaceHeight(BlockState state) {
        if (!state.hasProperty(BlockStateRegistry.LIQUID)) return 2f / 16f;
        Liquid liquid = state.getValue(BlockStateRegistry.LIQUID);
        return switch (liquid) {
            case EMPTY -> 2f / 16f;
            case DRAINED -> 4f / 16f;
            case FILLED -> 9f / 16f;
            case BEER -> 10f / 16f;
            case OVERFLOWING -> 15f / 16f;
        };
    }
}
