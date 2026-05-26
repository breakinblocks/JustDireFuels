package com.breakinblocks.justdirefuels.datamap;

import com.breakinblocks.justdirefuels.JustDireFuels;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

@EventBusSubscriber(modid = JustDireFuels.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class FuelDataMaps {
    public static final DataMapType<Fluid, FluidFuelData> FLUID_FUELS = DataMapType.builder(
        JustDireFuels.id("fluid_fuels"),
        Registries.FLUID,
        FluidFuelData.CODEC
    ).synced(FluidFuelData.CODEC, false).build();

    public static final DataMapType<Item, ItemFuelData> ITEM_FUELS = DataMapType.builder(
        JustDireFuels.id("item_fuels"),
        Registries.ITEM,
        ItemFuelData.CODEC
    ).synced(ItemFuelData.CODEC, false).build();

    private FuelDataMaps() {}

    @SubscribeEvent
    public static void register(RegisterDataMapTypesEvent event) {
        event.register(FLUID_FUELS);
        event.register(ITEM_FUELS);
    }
}
