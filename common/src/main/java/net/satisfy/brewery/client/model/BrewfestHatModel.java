package net.satisfy.brewery.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.LivingEntity;
import net.satisfy.brewery.Brewery;
import org.jetbrains.annotations.NotNull;

public class BrewfestHatModel<T extends LivingEntity> extends HumanoidModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Brewery.identifier("brewfest_hat"), "main");

    public BrewfestHatModel(ModelPart root) {
        super(root);
        setAllVisible(false);
        head.visible = true;
        hat.visible = true;
    }

    @SuppressWarnings("unused")
    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition root = meshDefinition.getRoot();

        root.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.ZERO);

        root.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-5.0F, 2.0F, 0.0F));
        root.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(5.0F, 2.0F, 0.0F));

        root.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(-1.9F, 12.0F, 0.0F));
        root.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offset(1.9F, 12.0F, 0.0F));

        root.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.ZERO);

        root.addOrReplaceChild(
                "hat",
                CubeListBuilder.create()
                        .texOffs(-14, 15).addBox(-7.0F, -6.0F, -7.0F, 14.0F, 0.0F, 14.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 0).addBox(-4.0F, -10.2F, -4.0F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.2F))
                        .texOffs(-1, 4).addBox(-4.05F, -8.21F, -4.05F, 8.1F, 2.0F, 8.1F, new CubeDeformation(0.2F))
                        .texOffs(22, 22).addBox(4.21F, -13.4F, -1.0F, 0.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)),
                PartPose.ZERO
        );

        return LayerDefinition.create(meshDefinition, 32, 32);
    }

    @Override
    public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer buffer, int packedLight, int packedOverlay, int packedColor) {
        setAllVisible(false);
        head.visible = true;
        hat.visible = true;
        hat.copyFrom(head);
        super.renderToBuffer(poseStack, buffer, packedLight, packedOverlay, packedColor);
    }
}