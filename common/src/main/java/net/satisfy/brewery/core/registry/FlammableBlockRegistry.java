package net.satisfy.brewery.core.registry;

import net.satisfy.foundation.flammable.FoundationFlammables;

import static net.satisfy.brewery.core.registry.ObjectRegistry.*;

public class FlammableBlockRegistry {
    public static void init() {
        FoundationFlammables.plant(WILD_HOPS);
        FoundationFlammables.wool(DRIED_WHEAT, DRIED_BARLEY, DRIED_CORN, DRIED_OAT, PATTERNED_WOOL);
        FoundationFlammables.hay(PATTERNED_CARPET_BLOCK);
        FoundationFlammables.wood(BENCH, TABLE, CABINET, DRAWER, BAR_COUNTER, SIDEBOARD, WALL_CABINET, WOODEN_BREWINGSTATION, BARREL_MAIN, BARREL_MAIN_HEAD, BARREL_RIGHT, BARREL_HEAD_RIGHT);
    }
}
