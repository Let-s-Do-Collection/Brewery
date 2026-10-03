package net.satisfy.brewery.core.registry;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.item.Item;
import net.satisfy.brewery.client.model.BrewfestBootsModel;
import net.satisfy.brewery.client.model.BrewfestChestplateModel;
import net.satisfy.brewery.client.model.BrewfestHatModel;
import net.satisfy.brewery.client.model.BrewfestLeggingsModel;

import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class ArmorRegistry {
    private static final Map<Item, BrewfestHatModel<?>> hatModels = new HashMap<>();
    private static final Map<Item, BrewfestChestplateModel<?>> chestplateModels = new HashMap<>();
    private static final Map<Item, BrewfestLeggingsModel<?>> leggingsModels = new HashMap<>();
    private static final Map<Item, BrewfestBootsModel<?>> bootsModels = new HashMap<>();

    public static Model getHatModel(Item item, ModelPart baseHead, HumanoidModel<?> original) {
        if (item != ObjectRegistry.BREWFEST_HAT_RED.get() && item != ObjectRegistry.BREWFEST_HAT.get()) return original;

        BrewfestHatModel<?> model = hatModels.computeIfAbsent(item, key -> new BrewfestHatModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(BrewfestHatModel.LAYER_LOCATION)));

        model.young = original.young;
        model.copyHead(baseHead);

        return model;
    }

    public static Model getChestplateModel(Item item, ModelPart body, ModelPart leftArm, ModelPart rightArm, ModelPart leftLeg, ModelPart rightLeg, HumanoidModel<?> original) {
        if (item != ObjectRegistry.BREWFEST_BLOUSE.get() && item != ObjectRegistry.BREWFEST_REGALIA.get()) return original;

        BrewfestChestplateModel<?> model = chestplateModels.computeIfAbsent(item, key -> new BrewfestChestplateModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(BrewfestChestplateModel.LAYER_LOCATION)));

        model.young = original.young;
        model.copyBody(body, leftArm, rightArm);

        return model;
    }

    public static Model getLeggingsModel(Item item, ModelPart rightLeg, ModelPart leftLeg, HumanoidModel<?> original) {
        if (item != ObjectRegistry.BREWFEST_DRESS.get() && item != ObjectRegistry.BREWFEST_TROUSERS.get()) return original;

        BrewfestLeggingsModel<?> model = leggingsModels.computeIfAbsent(item, key -> new BrewfestLeggingsModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(BrewfestLeggingsModel.LAYER_LOCATION)));

        model.young = original.young;
        model.copyLegs(rightLeg, leftLeg);

        return model;
    }

    public static Model getBootsModel(Item item, ModelPart rightLeg, ModelPart leftLeg, HumanoidModel<?> original) {
        if (item != ObjectRegistry.BREWFEST_BOOTS.get() && item != ObjectRegistry.BREWFEST_SHOES.get()) return original;

        BrewfestBootsModel<?> model = bootsModels.computeIfAbsent(item, key -> new BrewfestBootsModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(BrewfestBootsModel.LAYER_LOCATION)));

        model.young = original.young;
        model.copyLegs(rightLeg, leftLeg);

        return model;
    }
}