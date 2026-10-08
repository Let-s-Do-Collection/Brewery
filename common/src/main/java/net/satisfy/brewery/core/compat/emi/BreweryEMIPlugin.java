package net.satisfy.brewery.core.compat.emi;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.satisfy.brewery.Brewery;
import net.satisfy.brewery.core.recipe.BrewingRecipe;
import net.satisfy.brewery.core.registry.ObjectRegistry;
import net.satisfy.brewery.core.registry.RecipeTypeRegistry;

@EmiEntrypoint
public class BreweryEMIPlugin implements EmiPlugin {
    public static final EmiStack BREWING_ICON = EmiStack.of(ObjectRegistry.WOODEN_BREWINGSTATION.get());
    public static final EmiRecipeCategory BREWING = new EmiRecipeCategory(Brewery.identifier("brewing"), BREWING_ICON);

    @Override
    public void register(EmiRegistry registry) {
        registry.addCategory(BREWING);
        registry.addWorkstation(BREWING, EmiStack.of(ObjectRegistry.WOODEN_BREWINGSTATION.get()));
        registry.addWorkstation(BREWING, EmiStack.of(ObjectRegistry.COPPER_BREWINGSTATION.get()));
        registry.addWorkstation(BREWING, EmiStack.of(ObjectRegistry.NETHERITE_BREWINGSTATION.get()));

        for (RecipeHolder<BrewingRecipe> holder : registry.getRecipeManager().getAllRecipesFor(RecipeTypeRegistry.BREWING_RECIPE_TYPE.get())) {
            registry.addRecipe(new BrewingEmiRecipe(holder));
        }
    }
}
