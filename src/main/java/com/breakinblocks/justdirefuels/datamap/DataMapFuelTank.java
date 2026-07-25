package com.breakinblocks.justdirefuels.datamap;

public interface DataMapFuelTank {
    void justdirefuels$enableDataMapFuels();

    boolean justdirefuels$dataMapFuelsEnabled();

    static void enable(Object tank) {
        if (tank instanceof DataMapFuelTank duck) duck.justdirefuels$enableDataMapFuels();
    }
}
