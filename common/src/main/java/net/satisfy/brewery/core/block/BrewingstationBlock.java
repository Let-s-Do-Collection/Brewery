package net.satisfy.brewery.core.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.satisfy.brewery.core.block.entity.BrewstationBlockEntity;
import net.satisfy.brewery.core.block.property.BrewMaterial;
import net.satisfy.brewery.core.registry.BlockStateRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BrewingstationBlock extends HorizontalDirectionalBlock {

    public static final EnumProperty<BrewMaterial> MATERIAL = BlockStateRegistry.MATERIAL;

    public BrewingstationBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState());
    }

    public static final MapCodec<BrewingstationBlock> CODEC = simpleCodec(BrewingstationBlock::new);

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public @NotNull ItemStack getCloneItemStack(LevelReader levelReader, BlockPos pos, BlockState state) {
        BrewstationBlockEntity blockEntity = getController(pos, levelReader);
        if (blockEntity != null) {
            ItemStack beerStack = blockEntity.peekBeer();
            if (beerStack != null && !beerStack.isEmpty()) {
                return beerStack;
            }
        }
        return super.getCloneItemStack(levelReader, pos, state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(MATERIAL, FACING);
    }

    @Override
    public @NotNull BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        BrewstationBlockEntity brewstationEntity = getController(pos, level);
        if (brewstationEntity != null) {
            brewstationEntity.getComponents().stream()
                    .filter(componentPos -> !componentPos.equals(pos))
                    .forEach(componentPos -> level.removeBlock(componentPos, false));
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Nullable
    protected BrewstationBlockEntity getController(BlockPos centerPos, Level level) {
        return findController(centerPos, level);
    }

    @Nullable
    protected BrewstationBlockEntity getController(BlockPos centerPos, BlockGetter getter) {
        return findController(centerPos, getter);
    }

    private <T extends BlockGetter> BrewstationBlockEntity findController(BlockPos centerPos, T blockGetter) {
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                BlockEntity blockEntity = blockGetter.getBlockEntity(centerPos.offset(x, 0, y));
                if (blockEntity instanceof BrewstationBlockEntity brewstationEntity && brewstationEntity.isPartOf(centerPos)) {
                    return brewstationEntity;
                }
            }
        }
        return null;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
        list.add(Component.translatable("tooltip.farm_and_charm.canbeplaced").withStyle(ChatFormatting.GRAY));
    }
}
