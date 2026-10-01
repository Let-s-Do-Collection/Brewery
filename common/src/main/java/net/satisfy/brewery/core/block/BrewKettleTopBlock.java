package net.satisfy.brewery.core.block;

import net.satisfy.foundation.util.ShapeUtil;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.brewery.core.registry.ObjectRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/** Invisible upper half of the kettle, forwards all interaction to the kettle below. */
public class BrewKettleTopBlock extends BrewingstationBlock {
    public static final Map<Direction, VoxelShape> SHAPE = Util.make(new HashMap<>(), map -> {
        for (Direction direction : Direction.Plane.HORIZONTAL.stream().toList()) {
            map.put(direction, ShapeUtil.rotateShape(Direction.NORTH, direction, Shapes.box(0, 0, 0.0625, 0.0625, 0.875, 1)));
        }
    });

    public BrewKettleTopBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE.get(state.getValue(FACING));
    }

    @Override
    protected boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
        return true;
    }

    @Override
    protected float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
        return 1.0F;
    }

    @Override
    protected @NotNull RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        BlockPos kettlePos = blockPos.below();
        BlockState kettleState = level.getBlockState(kettlePos);
        if (kettleState.getBlock() instanceof BrewKettleBlock kettle) {
            return kettle.useItemOn(itemStack, kettleState, level, kettlePos, player, interactionHand, blockHitResult.withPosition(kettlePos));
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public @NotNull ItemStack getCloneItemStack(LevelReader getter, BlockPos pos, BlockState state) {
        return switch (state.getValue(MATERIAL)) {
            case COPPER -> new ItemStack(ObjectRegistry.COPPER_BREWINGSTATION.get());
            case WOOD -> new ItemStack(ObjectRegistry.WOODEN_BREWINGSTATION.get());
            case NETHERITE -> new ItemStack(ObjectRegistry.NETHERITE_BREWINGSTATION.get());
        };
    }
}
