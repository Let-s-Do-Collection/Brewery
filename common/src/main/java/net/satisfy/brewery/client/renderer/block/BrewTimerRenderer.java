package net.satisfy.brewery.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.brewery.platform.PlatformHelper;
import net.satisfy.brewery.Brewery;
import net.satisfy.brewery.core.block.BrewTimerBlock;
import net.satisfy.brewery.core.block.entity.BrewTimerBlockEntity;
import net.satisfy.brewery.core.block.entity.BrewstationBlockEntity;
import net.satisfy.brewery.core.block.property.BrewMaterial;
import net.satisfy.brewery.core.registry.BlockStateRegistry;

public class BrewTimerRenderer implements BlockEntityRenderer<BrewTimerBlockEntity> {
    private static final float PX = 1.0F / 16.0F;
    private static final float PRESS_TICKS = 6.0F;

    public BrewTimerRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(BrewTimerBlockEntity timer, float partialTicks, PoseStack poseStack, MultiBufferSource buffers, int light, int overlay) {
        if (timer.getLevel() == null) {
            return;
        }
        BlockState state = timer.getBlockState();
        float time = PlatformHelper.animationsEnabled() ? timer.getLevel().getGameTime() + partialTicks : 0.0F;
        BrewMaterial material = state.getValue(BlockStateRegistry.MATERIAL);
        VertexConsumer consumer = buffers.getBuffer(RenderType.entityCutout(TextureAtlas.LOCATION_BLOCKS));

        poseStack.pushPose();
        poseStack.translate(0.5F, 0.0F, 0.5F);
        poseStack.mulPose(Axis.YP.rotationDegrees(-yRotation(state.getValue(BrewTimerBlock.FACING))));
        poseStack.translate(-0.5F, 0.0F, -0.5F);

        renderButton(poseStack, consumer, sprite(material, "button"), time, timer.getPressTime(), light, overlay);
        renderNeedle(poseStack, consumer, sprite(material, "tachometer"), needleTarget(timer, state, time), timer, light, overlay);

        poseStack.popPose();
    }

    private static float needleTarget(BrewTimerBlockEntity timer, BlockState state, float time) {
        if (state.getValue(BrewTimerBlock.TIME)) {
            return 55.0F + Mth.sin(time * 1.9F) * 20.0F;
        }
        BrewstationBlockEntity station = timer.findStation();
        float progress = station == null ? -1.0F : station.getBrewProgress();
        if (progress < 0.0F) {
            return -60.0F;
        }
        return -60.0F + progress * 120.0F + Mth.sin(time * 0.8F) * 1.5F;
    }

    private void renderNeedle(PoseStack poseStack, VertexConsumer consumer, TextureAtlasSprite sprite, float target, BrewTimerBlockEntity timer, int light, int overlay) {
        float angle = timer.updateNeedle(target, 0.12F);
        float u = sprite.getU(2.5F / 16.0F);
        float v = sprite.getV(2.5F / 16.0F);
        poseStack.pushPose();
        poseStack.translate(5.0F * PX, 10.8F * PX, 16.02F * PX);
        poseStack.mulPose(Axis.ZP.rotationDegrees(-angle));
        float half = 0.5F * PX;
        float length = 2.0F * PX;
        quad(poseStack, consumer, -half, 0.0F, half, length, 0.0F, u, v, u, v, 0, 0, 1, 0xFFB01818, light, overlay);
        poseStack.popPose();
    }

    private void renderButton(PoseStack poseStack, VertexConsumer consumer, TextureAtlasSprite sprite, float time, long pressTime, int light, int overlay) {
        float progress = (time - pressTime) / PRESS_TICKS;
        float depth = progress >= 0.0F && progress < 1.0F ? Mth.sin(progress * Mth.PI) * 0.8F * PX : 0.0F;
        float x0 = 11.0F * PX, x1 = 13.0F * PX, y0 = 11.0F * PX, y1 = 13.0F * PX;
        float z0 = 15.0F * PX - depth, z1 = 16.0F * PX - depth;
        float u0 = sprite.getU(0.0F), u1 = sprite.getU(2.0F / 16.0F), v0 = sprite.getV(0.0F), v1 = sprite.getV(2.0F / 16.0F);
        float eu0 = sprite.getU(3.0F / 16.0F), eu1 = sprite.getU(4.0F / 16.0F);
        float wu0 = sprite.getU(0.0F), wu1 = sprite.getU(1.0F / 16.0F);
        float cu0 = sprite.getU(3.0F / 16.0F), cu1 = sprite.getU(5.0F / 16.0F), cv1 = sprite.getV(1.0F / 16.0F);
        quad(poseStack, consumer, x0, y0, x1, y1, z1, u0, v1, u1, v0, 0, 0, 1, -1, light, overlay);
        side(poseStack, consumer, x1, y0, y1, z0, z1, eu0, v1, eu1, v0, 1, light, overlay);
        side(poseStack, consumer, x0, y0, y1, z0, z1, wu0, v1, wu1, v0, -1, light, overlay);
        cap(poseStack, consumer, x0, x1, y1, z0, z1, cu1, cv1, cu0, v0, 1, light, overlay);
        cap(poseStack, consumer, x0, x1, y0, z0, z1, cu0, v0, cu1, cv1, -1, light, overlay);
    }

    private static void quad(PoseStack poseStack, VertexConsumer consumer, float x0, float y0, float x1, float y1, float z, float u0, float v0, float u1, float v1, float nx, float ny, float nz, int color, int light, int overlay) {
        PoseStack.Pose pose = poseStack.last();
        vertex(consumer, pose, x0, y0, z, u0, v0, nx, ny, nz, color, light, overlay);
        vertex(consumer, pose, x1, y0, z, u1, v0, nx, ny, nz, color, light, overlay);
        vertex(consumer, pose, x1, y1, z, u1, v1, nx, ny, nz, color, light, overlay);
        vertex(consumer, pose, x0, y1, z, u0, v1, nx, ny, nz, color, light, overlay);
    }

    private static void side(PoseStack poseStack, VertexConsumer consumer, float x, float y0, float y1, float z0, float z1, float u0, float v0, float u1, float v1, int dir, int light, int overlay) {
        PoseStack.Pose pose = poseStack.last();
        float za = dir > 0 ? z1 : z0;
        float zb = dir > 0 ? z0 : z1;
        vertex(consumer, pose, x, y0, za, u0, v0, dir, 0, 0, -1, light, overlay);
        vertex(consumer, pose, x, y0, zb, u1, v0, dir, 0, 0, -1, light, overlay);
        vertex(consumer, pose, x, y1, zb, u1, v1, dir, 0, 0, -1, light, overlay);
        vertex(consumer, pose, x, y1, za, u0, v1, dir, 0, 0, -1, light, overlay);
    }

    private static void cap(PoseStack poseStack, VertexConsumer consumer, float x0, float x1, float y, float z0, float z1, float u0, float v0, float u1, float v1, int dir, int light, int overlay) {
        PoseStack.Pose pose = poseStack.last();
        float za = dir > 0 ? z1 : z0;
        float zb = dir > 0 ? z0 : z1;
        vertex(consumer, pose, x0, y, za, u0, v0, 0, dir, 0, -1, light, overlay);
        vertex(consumer, pose, x1, y, za, u1, v0, 0, dir, 0, -1, light, overlay);
        vertex(consumer, pose, x1, y, zb, u1, v1, 0, dir, 0, -1, light, overlay);
        vertex(consumer, pose, x0, y, zb, u0, v1, 0, dir, 0, -1, light, overlay);
    }

    private static void vertex(VertexConsumer consumer, PoseStack.Pose pose, float x, float y, float z, float u, float v, float nx, float ny, float nz, int color, int light, int overlay) {
        consumer.addVertex(pose, x, y, z).setColor(color).setUv(u, v).setOverlay(overlay).setLight(light).setNormal(pose, nx, ny, nz);
    }

    private static TextureAtlasSprite sprite(BrewMaterial material, String part) {
        String folder = material.getSerializedName();
        String prefix = material == BrewMaterial.WOOD ? "wooden" : folder;
        ResourceLocation id = Brewery.identifier("block/brewing_station/" + folder + "/" + prefix + "_brewing_station_" + part);
        return Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(id);
    }

    private static float yRotation(Direction facing) {
        return switch (facing) {
            case EAST -> 90.0F;
            case SOUTH -> 180.0F;
            case WEST -> 270.0F;
            default -> 0.0F;
        };
    }
}
