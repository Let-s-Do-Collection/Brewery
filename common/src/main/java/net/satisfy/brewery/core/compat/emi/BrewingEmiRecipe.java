package net.satisfy.brewery.core.compat.emi;

import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.satisfy.brewery.core.recipe.BrewingRecipe;

public class BrewingEmiRecipe extends BasicEmiRecipe {
    public BrewingEmiRecipe(RecipeHolder<BrewingRecipe> holder) {
        super(BreweryEMIPlugin.BREWING, holder.id(), 84, 58);
        holder.value().getIngredients().forEach(ingredient -> inputs.add(EmiIngredient.of(ingredient)));
        outputs.add(EmiStack.of(holder.value().getResultItem(null)));
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        for (int i = 0; i < 3; i++) {
            widgets.addSlot(i < inputs.size() ? inputs.get(i) : EmiStack.EMPTY, 0, i * 20);
        }
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 26, 20);
        widgets.addAnimatedTexture(EmiTexture.FULL_ARROW, 26, 20, 2500, true, false, false);
        widgets.addSlot(outputs.get(0), 58, 16).large(true).recipeContext(this);
    }
}
