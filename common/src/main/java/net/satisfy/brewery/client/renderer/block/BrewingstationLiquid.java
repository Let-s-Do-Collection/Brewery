package net.satisfy.brewery.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.brewery.platform.PlatformHelper;
import net.satisfy.brewery.Brewery;
import net.satisfy.brewery.core.block.entity.BrewstationBlockEntity;
import net.satisfy.brewery.core.block.property.Liquid;
import net.satisfy.brewery.core.registry.BlockStateRegistry;

public final class BrewingstationLiquid {
    private static final float PX = 1.0F / 16.0F;
    private static final ResourceLocation WATER = ResourceLocation.withDefaultNamespace("block/water_still");
    private static final ResourceLocation BEER = Brewery.identifier("block/brewing_station/beer_finished");

    private BrewingstationLiquid() {
    }

    public static float surfaceHeight(Liquid liquid) {
        return switch (liquid) {
            case EMPTY -> 2.0F * PX;
            case DRAINED -> 4.0F * PX;
            case FILLED -> 9.0F * PX;
            case BEER -> 10.0F * PX;
            case OVERFLOWING -> 15.0F * PX;
        };
    }

    public static void render(BrewstationBlockEntity station, float partialTicks, PoseStack poseStack, MultiBufferSource buffers, int light, int overlay) {
        BlockState state = station.getBlockState();
        if (station.getLevel() == null || !state.hasProperty(BlockStateRegistry.LIQUID)) {
            return;
        }
        Liquid liquid = state.getValue(BlockStateRegistry.LIQUID);
        float height = station.updateLiquidHeight(surfaceHeight(liquid), 0.06F);
        if (height <= 2.05F * PX) {
            return;
        }
        float time = PlatformHelper.animationsEnabled() ? station.getLevel().getGameTime() + partialTicks : 0.0F;
        if (liquid == Liquid.OVERFLOWING) {
            height += Mth.sin(time * 0.6F) * 0.25F * PX;
        }
        boolean beer = liquid == Liquid.BEER;
        int color = beer ? -1 : 0xFF000000 | station.getLiquidColor(BiomeColors.getAverageWaterColor(station.getLevel(), station.getBlockPos()));
        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(beer ? BEER : WATER);
        VertexConsumer consumer = buffers.getBuffer(RenderType.entityTranslucent(TextureAtlas.LOCATION_BLOCKS));

        poseStack.pushPose();
        poseStack.translate(0.5F, 0.0F, 0.5F);
        poseStack.mulPose(Axis.YP.rotationDegrees(-yRotation(state.getValue(HorizontalDirectionalBlock.FACING))));
        poseStack.translate(-0.5F, 0.0F, -0.5F);
        PoseStack.Pose pose = poseStack.last();
        float x0 = 2.0F * PX, x1 = 14.0F * PX, z0 = 2.0F * PX, z1 = 15.0F * PX;
        float u0 = sprite.getU(2.0F / 16.0F), u1 = sprite.getU(14.0F / 16.0F), v0 = sprite.getV(2.0F / 16.0F), v1 = sprite.getV(15.0F / 16.0F);
        vertex(consumer, pose, x0, height, z0, u0, v0, color, light, overlay);
        vertex(consumer, pose, x0, height, z1, u0, v1, color, light, overlay);
        vertex(consumer, pose, x1, height, z1, u1, v1, color, light, overlay);
        vertex(consumer, pose, x1, height, z0, u1, v0, color, light, overlay);
        poseStack.popPose();
    }

    private static void vertex(VertexConsumer consumer, PoseStack.Pose pose, float x, float y, float z, float u, float v, int color, int light, int overlay) {
        consumer.addVertex(pose, x, y, z).setColor(color).setUv(u, v).setOverlay(overlay).setLight(light).setNormal(pose, 0.0F, 1.0F, 0.0F);
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
