package net.satisfy.brewery.core.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.level.Level;
import net.satisfy.brewery.core.block.PatternedWoolBlock;
import net.satisfy.brewery.core.registry.ObjectRegistry;
import net.satisfy.brewery.core.registry.RecipeTypeRegistry;
import org.jetbrains.annotations.NotNull;

/** Shaped recipe that passes the color of the patterned wool on to the result. */
public class PatternedCarpetRecipe extends ShapedRecipe {
    private final ShapedRecipePattern pattern;
    private final ItemStack result;

    public PatternedCarpetRecipe(String group, CraftingBookCategory category, ShapedRecipePattern pattern, ItemStack result, boolean showNotification) {
        super(group, category, pattern, result, showNotification);
        this.pattern = pattern;
        this.result = result;
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        return super.matches(input, level) && findColor(input) != null;
    }

    @Override
    public @NotNull ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        DyeColor color = findColor(input);
        return PatternedWoolBlock.withColor(result.copy(), color != null ? color : DyeColor.LIGHT_BLUE);
    }

    /** Returns the shared wool color, or null if the wool colors differ. */
    private static DyeColor findColor(CraftingInput input) {
        DyeColor color = null;
        for (ItemStack stack : input.items()) {
            if (!stack.is(ObjectRegistry.PATTERNED_WOOL.get().asItem())) {
                continue;
            }
            DyeColor stackColor = PatternedWoolBlock.getColor(stack);
            if (color != null && color != stackColor) {
                return null;
            }
            color = stackColor;
        }
        return color != null ? color : DyeColor.LIGHT_BLUE;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return RecipeTypeRegistry.PATTERNED_CARPET_RECIPE_SERIALIZER.get();
    }

    public static class Serializer implements RecipeSerializer<PatternedCarpetRecipe> {
        private static final MapCodec<PatternedCarpetRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.STRING.optionalFieldOf("group", "").forGetter(ShapedRecipe::getGroup),
                CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(ShapedRecipe::category),
                ShapedRecipePattern.MAP_CODEC.forGetter(recipe -> recipe.pattern),
                ItemStack.STRICT_CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
                Codec.BOOL.optionalFieldOf("show_notification", true).forGetter(ShapedRecipe::showNotification)
        ).apply(instance, PatternedCarpetRecipe::new));

        private static final StreamCodec<RegistryFriendlyByteBuf, PatternedCarpetRecipe> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8, ShapedRecipe::getGroup,
                CraftingBookCategory.STREAM_CODEC, ShapedRecipe::category,
                ShapedRecipePattern.STREAM_CODEC, recipe -> recipe.pattern,
                ItemStack.STREAM_CODEC, recipe -> recipe.result,
                ByteBufCodecs.BOOL, ShapedRecipe::showNotification,
                PatternedCarpetRecipe::new
        );

        @Override
        public @NotNull MapCodec<PatternedCarpetRecipe> codec() {
            return CODEC;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, PatternedCarpetRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
