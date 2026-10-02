package net.satisfy.brewery.core.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.satisfy.brewery.Brewery;
import net.satisfy.brewery.core.recipe.BrewingRecipe;
import net.satisfy.brewery.core.recipe.PatternedCarpetRecipe;

import java.util.function.Supplier;

public class RecipeTypeRegistry {
    private static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Brewery.MOD_ID, Registries.RECIPE_SERIALIZER);
    public static final RegistrySupplier<RecipeSerializer<BrewingRecipe>> BREWING_RECIPE_SERIALIZER = create(BrewingRecipe.Serializer::new);
    public static final RegistrySupplier<RecipeSerializer<PatternedCarpetRecipe>> PATTERNED_CARPET_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("patterned_carpet", PatternedCarpetRecipe.Serializer::new);
    private static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Brewery.MOD_ID, Registries.RECIPE_TYPE);
    public static final RegistrySupplier<RecipeType<BrewingRecipe>> BREWING_RECIPE_TYPE = create();

    private static <T extends Recipe<?>> RegistrySupplier<RecipeSerializer<T>> create(Supplier<RecipeSerializer<T>> serializer) {
        return RECIPE_SERIALIZERS.register("brewing", serializer);
    }

    private static <T extends Recipe<?>> RegistrySupplier<RecipeType<T>> create() {
        Supplier<RecipeType<T>> type = () -> new RecipeType<>() {
            @Override
            public String toString() {
                return "brewing";
            }
        };
        return RECIPE_TYPES.register("brewing", type);
    }

    public static void init() {
        RECIPE_SERIALIZERS.register();
        RECIPE_TYPES.register();
    }
}
