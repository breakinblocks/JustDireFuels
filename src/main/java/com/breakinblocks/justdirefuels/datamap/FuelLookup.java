package com.breakinblocks.justdirefuels.datamap;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;

public final class FuelLookup {
    private FuelLookup() {}

    @Nullable
    public static FluidFuelData getFluid(Fluid fluid) {
        if (fluid == null) return null;
        Holder<Fluid> holder = BuiltInRegistries.FLUID.wrapAsHolder(fluid);
        return holder.getData(FuelDataMaps.FLUID_FUELS);
    }

    @Nullable
    public static ItemFuelData getItem(Item item) {
        if (item == null) return null;
        Holder<Item> holder = BuiltInRegistries.ITEM.wrapAsHolder(item);
        return holder.getData(FuelDataMaps.ITEM_FUELS);
    }
}
