package net.satisfy.brewery.core.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.satisfy.brewery.core.block.property.BrewMaterial;
import net.satisfy.brewery.core.registry.RecipeTypeRegistry;
import org.jetbrains.annotations.NotNull;

public class BrewingRecipe implements Recipe<RecipeInput> {
    private final NonNullList<Ingredient> ingredients;
    private final ItemStack output;
    private final BrewMaterial material;

    public BrewingRecipe(NonNullList<Ingredient> ingredients, ItemStack output, BrewMaterial material) {
        this.ingredients = ingredients;
        this.output = output;
        this.material = material;
    }

    public BrewMaterial getMaterial() {
        return material;
    }

    @Override
    public boolean matches(RecipeInput inventory, Level world) {
        boolean[] used = new boolean[inventory.size()];
        for (Ingredient ingredient : this.ingredients) {
            boolean found = false;
            for (int i = 0; i < inventory.size(); i++) {
                if (!used[i] && ingredient.test(inventory.getItem(i))) {
                    used[i] = true;
                    found = true;
                    break;
                }
            }
            if (!found) return false;
        }
        return true;
    }

    @Override
    public @NotNull ItemStack assemble(RecipeInput recipeInput, HolderLookup.Provider provider) {
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        return this.ingredients;
    }


    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.Provider provider) {
        return this.output.copy();
    }

    public @NotNull ResourceLocation getId() {
        return RecipeTypeRegistry.BREWING_RECIPE_TYPE.getId();
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return RecipeTypeRegistry.BREWING_RECIPE_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return RecipeTypeRegistry.BREWING_RECIPE_TYPE.get();
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    public static class Serializer implements RecipeSerializer<BrewingRecipe> {

        public static final StreamCodec<RegistryFriendlyByteBuf, BrewingRecipe> STREAM_CODEC = StreamCodec.of(BrewingRecipe.Serializer::toNetwork, BrewingRecipe.Serializer::fromNetwork);
        private static final MapCodec<BrewingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(Ingredient.CODEC_NONEMPTY.listOf().fieldOf("ingredients").flatXmap((list) -> {
            Ingredient[] ingredients = list.stream().filter((ingredient) -> !ingredient.isEmpty()).toArray(Ingredient[]::new);
            if (ingredients.length == 0) {
                return DataResult.error(() -> "No ingredients for Brewing recipe");
            } else {
                return ingredients.length > 3 ? DataResult.error(() -> "Too many ingredients for Brewing recipe") : DataResult.success(NonNullList.of(Ingredient.EMPTY, ingredients));
            }
        }, DataResult::success).forGetter(bakingStationRecipe -> bakingStationRecipe.ingredients),
                ItemStack.STRICT_CODEC.fieldOf("result").forGetter(bakingStationRecipe -> bakingStationRecipe.output),
                Codec.STRING.fieldOf("material").forGetter(brewingRecipe -> brewingRecipe.material.toString())).apply(instance, (ingredients1, stack, string) -> new BrewingRecipe(ingredients1, stack, BrewMaterial.valueOf(string))));

        public static @NotNull BrewingRecipe fromNetwork(RegistryFriendlyByteBuf buf) {
            int i = buf.readVarInt();
            NonNullList<Ingredient> nonNullList = NonNullList.withSize(i, Ingredient.EMPTY);
            nonNullList.replaceAll((ingredient) -> Ingredient.CONTENTS_STREAM_CODEC.decode(buf));
            ItemStack itemStack = ItemStack.STREAM_CODEC.decode(buf);
            BrewMaterial material = buf.readEnum(BrewMaterial.class);
            return new BrewingRecipe(nonNullList, itemStack, material);
        }

        public static void toNetwork(RegistryFriendlyByteBuf buf, BrewingRecipe recipe) {
            buf.writeVarInt(recipe.ingredients.size());
            for (Ingredient ingredient : recipe.ingredients) {
                Ingredient.CONTENTS_STREAM_CODEC.encode(buf, ingredient);
            }

            ItemStack.STREAM_CODEC.encode(buf, recipe.output);
            buf.writeEnum(recipe.material);
        }

        @Override
        public @NotNull MapCodec<BrewingRecipe> codec() {
            return CODEC;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, BrewingRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}