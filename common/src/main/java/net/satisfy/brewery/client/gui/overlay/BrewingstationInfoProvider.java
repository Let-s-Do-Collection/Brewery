package net.satisfy.brewery.client.gui.overlay;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.satisfy.brewery.core.block.BrewKettleTopBlock;
import net.satisfy.brewery.core.block.entity.BrewstationBlockEntity;
import net.satisfy.brewery.core.block.property.Heat;
import net.satisfy.brewery.core.block.property.Liquid;
import net.satisfy.brewery.core.item.DrinkBlockItem;
import net.satisfy.brewery.core.event.brew_event.BrewHelper;
import net.satisfy.brewery.core.recipe.BrewingRecipe;
import net.satisfy.brewery.core.registry.BlockStateRegistry;
import net.satisfy.brewery.core.registry.ObjectRegistry;
import net.satisfy.brewery.core.registry.RecipeTypeRegistry;
import net.satisfy.brewery.platform.PlatformHelper;
import net.satisfy.foundation.overlay.BlockInfoProvider;
import net.satisfy.foundation.overlay.InfoSection;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BrewingstationInfoProvider implements BlockInfoProvider {
    private static final ResourceLocation DUNGAREES = ResourceLocation.fromNamespaceAndPath("farm_and_charm", "dungarees");

    @Override
    public List<InfoSection> describe(Level level, BlockPos pos, BlockState state, @Nullable BlockHitResult hit) {
        if (isHidden()) {
            return List.of();
        }
        if (state.getBlock() instanceof BrewKettleTopBlock) {
            pos = pos.below();
            state = level.getBlockState(pos);
        }
        if (!(level.getBlockEntity(pos) instanceof BrewstationBlockEntity station) || !state.hasProperty(BlockStateRegistry.LIQUID)) {
            return List.of();
        }
        List<InfoSection> sections = new ArrayList<>();
        Liquid liquid = state.getValue(BlockStateRegistry.LIQUID);
        if (liquid == Liquid.BEER) {
            ItemStack beer = station.peekBeer();
            if (beer != null) {
                sections.add(InfoSection.icons(Component.translatable("hud.brewery.ready").withStyle(ChatFormatting.GREEN), List.of(beer), InfoSection.ROW_COLUMNS));
                sections.add(InfoSection.title(Component.translatable(DrinkBlockItem.isBottled(beer) ? "hud.brewery.fill_hint_bottle" : "hud.brewery.fill_hint").withStyle(ChatFormatting.GRAY)));
            }
            return sections;
        }
        List<ItemStack> present = station.getIngredient().stream().filter(stack -> !stack.isEmpty()).toList();
        if (present.isEmpty()) {
            return sections;
        }
        sections.add(InfoSection.icons(Component.translatable("hud.brewery.in_kettle"), present, InfoSection.ROW_COLUMNS));

        BrewingRecipe complete = null;
        List<ItemStack> next = new ArrayList<>();
        List<InfoSection.Row> results = new ArrayList<>();
        for (RecipeHolder<BrewingRecipe> holder : level.getRecipeManager().getAllRecipesFor(RecipeTypeRegistry.BREWING_RECIPE_TYPE.get())) {
            BrewingRecipe recipe = holder.value();
            List<Ingredient> missing = missingIngredients(recipe.getIngredients(), present);
            if (missing == null) {
                continue;
            }
            if (missing.isEmpty()) {
                complete = recipe;
                break;
            }
            for (Ingredient ingredient : missing) {
                ItemStack[] choices = ingredient.getItems();
                if (choices.length > 0 && next.stream().noneMatch(stack -> ItemStack.isSameItem(stack, choices[0]))) {
                    next.add(choices[0]);
                }
            }
            if (missing.size() == 1) {
                ItemStack result = recipe.getResultItem(level.registryAccess());
                results.add(InfoSection.Row.item(result, result.getHoverName()));
            }
        }

        if (complete != null) {
            ItemStack result = complete.getResultItem(level.registryAccess());
            sections.add(InfoSection.rows(Component.translatable("hud.brewery.result").withStyle(ChatFormatting.GREEN), List.of(InfoSection.Row.item(result, result.getHoverName()))));
            sections.add(status(level, station, state, liquid, complete));
        } else if (!next.isEmpty()) {
            String nextKey = results.isEmpty() ? "hud.brewery.next_ingredients" : next.size() == 1 ? "hud.brewery.missing_ingredient" : "hud.brewery.missing_ingredients";
            sections.add(InfoSection.icons(Component.translatable(nextKey), next, InfoSection.GRID_COLUMNS * 2));
            if (!results.isEmpty()) {
                sections.add(InfoSection.rows(Component.translatable(results.size() == 1 ? "hud.brewery.result" : "hud.brewery.possible_results").withStyle(ChatFormatting.GREEN), results));
            }
        } else {
            sections.add(InfoSection.title(Component.translatable("hud.brewery.no_recipe").withStyle(ChatFormatting.RED)));
        }
        return sections;
    }

    private static boolean isHidden() {
        if (!PlatformHelper.showBrewingstationInfo()) {
            return true;
        }
        if (!PlatformHelper.infoTooltipsNeedDungarees()) {
            return false;
        }
        Player player = Minecraft.getInstance().player;
        return player == null || !BuiltInRegistries.ITEM.getKey(player.getItemBySlot(EquipmentSlot.LEGS).getItem()).equals(DUNGAREES);
    }

    private static InfoSection status(Level level, BrewstationBlockEntity station, BlockState state, Liquid liquid, BrewingRecipe recipe) {
        if (state.getValue(BlockStateRegistry.MATERIAL).getLevel() < recipe.getMaterial().getLevel()) {
            return InfoSection.title(Component.translatable("hud.brewery.needs_better_station").withStyle(ChatFormatting.RED));
        }
        if (liquid == Liquid.EMPTY) {
            return InfoSection.icons(Component.translatable("hud.brewery.needs_water").withStyle(ChatFormatting.GOLD), List.of(new ItemStack(Items.WATER_BUCKET)), InfoSection.ROW_COLUMNS);
        }
        BlockPos oven = BrewHelper.getBlock(ObjectRegistry.BREW_OVEN.get(), station.getComponents(), level);
        if (oven == null || level.getBlockState(oven).getValue(BlockStateRegistry.HEAT) == Heat.OFF) {
            return InfoSection.icons(Component.translatable("hud.brewery.needs_heat").withStyle(ChatFormatting.GOLD), List.of(new ItemStack(Items.COAL)), InfoSection.ROW_COLUMNS);
        }
        if (!station.isBrewingClient() && !station.isStarted()) {
            return InfoSection.title(Component.translatable("hud.brewery.press_start").withStyle(ChatFormatting.GOLD));
        }
        return InfoSection.title(Component.translatable("hud.brewery.brewing").withStyle(ChatFormatting.GRAY));
    }

    /** Ingredients still needed, or null when the present items do not fit this recipe. */
    private static @Nullable List<Ingredient> missingIngredients(List<Ingredient> required, List<ItemStack> present) {
        List<Ingredient> missing = new ArrayList<>(required);
        for (ItemStack stack : present) {
            Ingredient match = missing.stream().filter(ingredient -> ingredient.test(stack)).findFirst().orElse(null);
            if (match == null) {
                return null;
            }
            missing.remove(match);
        }
        return missing;
    }
}
