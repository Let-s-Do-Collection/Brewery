package net.satisfy.brewery.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.brewery.platform.PlatformHelper;
import net.satisfy.brewery.core.block.BrewWhistleBlock;
import net.satisfy.brewery.core.block.entity.BrewWhistleBlockEntity;

public class BrewWhistleRenderer implements BlockEntityRenderer<BrewWhistleBlockEntity> {
    private final BlockRenderDispatcher dispatcher;

    public BrewWhistleRenderer(BlockEntityRendererProvider.Context context) {
        this.dispatcher = context.getBlockRenderDispatcher();
    }

    @Override
    public void render(BrewWhistleBlockEntity whistle, float partialTicks, PoseStack poseStack, MultiBufferSource buffers, int light, int overlay) {
        BlockState state = whistle.getBlockState();
        if (!BrewWhistleBlock.isVibrating(state) || whistle.getLevel() == null) {
            return;
        }
        float time = PlatformHelper.animationsEnabled() ? whistle.getLevel().getGameTime() + partialTicks : 0.0F;
        poseStack.pushPose();
        poseStack.translate(Mth.sin(time * 2.7F) * 0.012F, Math.abs(Mth.sin(time * 4.1F)) * 0.008F, Mth.cos(time * 3.3F) * 0.012F);
        dispatcher.getModelRenderer().renderModel(poseStack.last(), buffers.getBuffer(ItemBlockRenderTypes.getRenderType(state, false)), state, dispatcher.getBlockModel(state), 1.0F, 1.0F, 1.0F, light, overlay);
        poseStack.popPose();
    }
}
