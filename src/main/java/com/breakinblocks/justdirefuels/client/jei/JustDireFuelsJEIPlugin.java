package com.breakinblocks.justdirefuels.client.jei;

import com.breakinblocks.justdirefuels.JustDireFuels;
import com.breakinblocks.justdirefuels.datamap.FluidFuelData;
import com.breakinblocks.justdirefuels.datamap.FuelDataMaps;
import com.breakinblocks.justdirefuels.datamap.ItemFuelData;
import com.direwolf20.justdirethings.setup.JDTRegistration;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@JeiPlugin
public class JustDireFuelsJEIPlugin implements IModPlugin {
    @Nonnull
    @Override
    public Identifier getPluginUid() {
        return JustDireFuels.id("jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        var guiHelper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(new FluidFuelCategory(guiHelper, new ItemStack(JDTRegistration.GeneratorFluidT1.get())));
        registration.addRecipeCategories(new ItemFuelCategory(guiHelper, new ItemStack(JDTRegistration.GeneratorT1.get())));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        if (Minecraft.getInstance().level == null) return;

        List<FluidFuelRecipe> fluidRecipes = new ArrayList<>();
        for (Holder.Reference<Fluid> holder : BuiltInRegistries.FLUID.listElements().toList()) {
            FluidFuelData data = holder.getData(FuelDataMaps.FLUID_FUELS);
            if (data == null) continue;
            Fluid fluid = holder.value();
            if (!fluid.defaultFluidState().isSource()) continue;
            fluidRecipes.add(new FluidFuelRecipe(new FluidStack(fluid, 1000), data.fePerMb()));
        }
        registration.addRecipes(FluidFuelCategory.TYPE, fluidRecipes);

        List<ItemFuelRecipe> itemRecipes = new ArrayList<>();
        for (Holder.Reference<Item> holder : BuiltInRegistries.ITEM.listElements().toList()) {
            ItemFuelData data = holder.getData(FuelDataMaps.ITEM_FUELS);
            FurnaceFuel furnace = holder.getData(NeoForgeDataMaps.FURNACE_FUELS);
            if (data == null && furnace == null) continue;
            if (furnace == null || furnace.burnTime() <= 0) continue;
            itemRecipes.add(new ItemFuelRecipe(
                new ItemStack(holder.value()),
                data == null ? Optional.empty() : data.fePerTick(),
                data == null ? Optional.empty() : data.burnSpeedMultiplier(),
                furnace.burnTime()
            ));
        }
        registration.addRecipes(ItemFuelCategory.TYPE, itemRecipes);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
        registry.addCraftingStation(FluidFuelCategory.TYPE, JDTRegistration.GeneratorFluidT1.get());
        registry.addCraftingStation(ItemFuelCategory.TYPE, JDTRegistration.GeneratorT1.get());
    }
}
