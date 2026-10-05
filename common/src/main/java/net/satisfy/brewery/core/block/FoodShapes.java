package net.satisfy.brewery.core.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class FoodShapes {
    public static final VoxelShape[] PORK_KNUCKLE = {
            shape(
                    Block.box(1, 0, 1, 15, 1, 15),
                    Block.box(1, 1, 3, 13, 9, 13)
            ),
            shape(
                    Block.box(1, 0, 1, 15, 1, 15),
                    Block.box(3, 1, 3, 13, 5, 13)
            ),
            shape(
                    Block.box(1, 0, 1, 15, 1, 15),
                    Block.box(4, 1, 4, 12, 4, 12)
            ),
            shape(
                    Block.box(1, 0, 1, 15, 1, 15)
            )
    };

    public static final VoxelShape[] FRIED_CHICKEN = {
            shape(
                    Block.box(1, 0, 1, 15, 1, 15),
                    Block.box(3, 1, 3, 7, 7, 7),
                    Block.box(4, 1, 8, 8, 7, 12),
                    Block.box(4, 7, 4, 6, 11, 6),
                    Block.box(5, 7, 9, 7, 11, 11)
            ),
            shape(
                    Block.box(1, 0, 1, 15, 1, 15),
                    Block.box(4, 1, 8, 8, 7, 12),
                    Block.box(5, 7, 9, 7, 11, 11)
            ),
            shape(
                    Block.box(1, 0, 1, 15, 1, 15),
                    Block.box(6, 1, 6, 10, 5, 12),
                    Block.box(7, 2, 2, 9, 4, 6)
            ),
            shape(
                    Block.box(1, 0, 1, 15, 1, 15)
            )
    };

    public static final VoxelShape[] HALF_CHICKEN = {
            shape(
                    Block.box(1, 0, 0, 15, 1, 16),
                    Block.box(5, 1, 1, 10, 7, 15),
                    Block.box(6, 7, 2, 10, 9, 14)
            ),
            shape(
                    Block.box(1, 0, 0, 15, 1, 16),
                    Block.box(5, 1, 1, 10, 7, 15),
                    Block.box(6, 7, 2, 10, 9, 14)
            ),
            shape(
                    Block.box(1, 0, 0, 15, 1, 16),
                    Block.box(5, 1, 5, 10, 7, 15),
                    Block.box(6, 7, 5, 10, 9, 14)
            ),
            shape(
                    Block.box(1, 0, 0, 15, 1, 16),
                    Block.box(5, 1, 9, 10, 7, 15),
                    Block.box(6, 7, 9, 10, 9, 14)
            )
    };

    public static final VoxelShape[] MASHED_POTATOES = {
            shape(
                    Block.box(4, 0, 4, 5, 3, 12),
                    Block.box(5, 0, 4, 11, 3, 5),
                    Block.box(5, 0, 5, 11, 1, 11),
                    Block.box(5, 0, 11, 11, 3, 12),
                    Block.box(5, 1, 5, 8, 4, 11),
                    Block.box(8, 1, 5, 11, 4, 11),
                    Block.box(11, 0, 4, 12, 3, 12)
            ),
            shape(
                    Block.box(4, 0, 4, 5, 3, 12),
                    Block.box(5, 0, 4, 11, 3, 5),
                    Block.box(5, 0, 5, 8, 3, 11),
                    Block.box(5, 0, 5, 11, 1, 11),
                    Block.box(5, 0, 11, 11, 3, 12),
                    Block.box(8, 1, 5, 11, 4, 11),
                    Block.box(11, 0, 4, 12, 3, 12)
            ),
            shape(
                    Block.box(4, 0, 4, 5, 3, 12),
                    Block.box(5, 0, 4, 11, 3, 5),
                    Block.box(5, 0, 5, 11, 1, 11),
                    Block.box(5, 0, 11, 11, 3, 12),
                    Block.box(8, 1, 5, 11, 4, 11),
                    Block.box(11, 0, 4, 12, 3, 12)
            ),
            shape(
                    Block.box(4, 0, 4, 5, 3, 12),
                    Block.box(5, 0, 4, 11, 3, 5),
                    Block.box(5, 0, 5, 11, 1, 11),
                    Block.box(5, 0, 11, 11, 3, 12),
                    Block.box(8, 1, 5, 11, 2, 11),
                    Block.box(11, 0, 4, 12, 3, 12)
            )
    };

    public static final VoxelShape[] POTATO_SALAD = {
            shape(
                    Block.box(4, 0, 4, 5, 3, 12),
                    Block.box(5, 0, 4, 11, 3, 5),
                    Block.box(5, 0, 5, 11, 1, 11),
                    Block.box(5, 0, 11, 11, 3, 12),
                    Block.box(5, 1, 5, 8, 4, 11),
                    Block.box(8, 1, 5, 11, 4, 11),
                    Block.box(11, 0, 4, 12, 3, 12)
            ),
            shape(
                    Block.box(4, 0, 4, 5, 3, 12),
                    Block.box(5, 0, 4, 11, 3, 5),
                    Block.box(5, 0, 5, 8, 3, 11),
                    Block.box(5, 0, 5, 11, 1, 11),
                    Block.box(5, 0, 11, 11, 3, 12),
                    Block.box(8, 1, 5, 11, 4, 11),
                    Block.box(11, 0, 4, 12, 3, 12)
            ),
            shape(
                    Block.box(4, 0, 4, 5, 3, 12),
                    Block.box(5, 0, 4, 11, 3, 5),
                    Block.box(5, 0, 5, 11, 1, 11),
                    Block.box(5, 0, 11, 11, 3, 12),
                    Block.box(8, 1, 5, 11, 4, 11),
                    Block.box(11, 0, 4, 12, 3, 12)
            ),
            shape(
                    Block.box(4, 0, 4, 5, 3, 12),
                    Block.box(5, 0, 4, 11, 3, 5),
                    Block.box(5, 0, 5, 11, 1, 11),
                    Block.box(5, 0, 11, 11, 3, 12),
                    Block.box(8, 1, 5, 11, 2, 11),
                    Block.box(11, 0, 4, 12, 3, 12)
            )
    };

    public static final VoxelShape[] DUMPLINGS = {
            shape(
                    Block.box(4, 0, 4, 5, 3, 12),
                    Block.box(5, 0, 4, 11, 3, 5),
                    Block.box(5, 0, 5, 11, 1, 11),
                    Block.box(5, 0, 11, 11, 3, 12),
                    Block.box(5, 1, 5, 8, 4, 8),
                    Block.box(6, 4, 6, 9, 7, 9),
                    Block.box(8, 1, 8, 11, 4, 11),
                    Block.box(11, 0, 4, 12, 3, 12)
            ),
            shape(
                    Block.box(4, 0, 4, 5, 3, 12),
                    Block.box(5, 0, 4, 11, 3, 5),
                    Block.box(5, 0, 5, 11, 1, 11),
                    Block.box(5, 0, 11, 11, 3, 12),
                    Block.box(5, 1, 5, 8, 4, 8),
                    Block.box(8, 1, 8, 11, 4, 11),
                    Block.box(11, 0, 4, 12, 3, 12)
            ),
            shape(
                    Block.box(4, 0, 4, 5, 3, 12),
                    Block.box(5, 0, 4, 11, 3, 5),
                    Block.box(5, 0, 5, 11, 1, 11),
                    Block.box(5, 0, 11, 11, 3, 12),
                    Block.box(5, 1, 5, 8, 4, 8),
                    Block.box(8, 1, 8, 11, 2, 11),
                    Block.box(11, 0, 4, 12, 3, 12)
            ),
            shape(
                    Block.box(4, 0, 4, 5, 3, 12),
                    Block.box(5, 0, 4, 11, 3, 5),
                    Block.box(5, 0, 5, 11, 1, 11),
                    Block.box(5, 0, 11, 11, 3, 12),
                    Block.box(5, 1, 5, 8, 4, 8),
                    Block.box(11, 0, 4, 12, 3, 12)
            )
    };

    private FoodShapes() {
    }

    private static VoxelShape shape(VoxelShape first, VoxelShape... rest) {
        return Shapes.or(first, rest);
    }
}
