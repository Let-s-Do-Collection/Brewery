package net.satisfy.brewery.core.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.satisfy.foundation.util.DyeHelper;

public class PatternedWoolBlock extends Block {
    public static final EnumProperty<DyeColor> COLOR = EnumProperty.create("color", DyeColor.class);

    private static final int BAVARIAN_BLUE = 0x68A0C7;

    public PatternedWoolBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(COLOR, DyeColor.LIGHT_BLUE));
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        return dye(stack, state, level, pos, player);
    }

    static ItemInteractionResult dye(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player) {
        if (stack.getItem() instanceof DyeItem dyeItem) {
            return DyeHelper.dye(stack, dyeItem.getDyeColor(), state, COLOR, level, pos, player);
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    /** Light blue is the default and renders as the Bavarian blue instead of vanilla light blue. */
    public static int getTint(DyeColor color) {
        return color == DyeColor.LIGHT_BLUE ? BAVARIAN_BLUE : color.getTextureDiffuseColor();
    }

    public static DyeColor getColor(ItemStack stack) {
        DyeColor color = stack.getOrDefault(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY).get(COLOR);
        return color != null ? color : DyeColor.LIGHT_BLUE;
    }

    public static ItemStack withColor(ItemStack stack, DyeColor color) {
        stack.set(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY.with(COLOR, color));
        return stack;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(COLOR);
    }
}
