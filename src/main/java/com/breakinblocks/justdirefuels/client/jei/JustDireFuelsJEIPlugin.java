package com.breakinblocks.justdirefuels.client.jei;

import com.breakinblocks.justdirefuels.JustDireFuels;
import com.breakinblocks.justdirefuels.datamap.FluidFuelData;
import com.breakinblocks.justdirefuels.datamap.FuelDataMaps;
import com.direwolf20.justdirethings.setup.Registration;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

@JeiPlugin
public class JustDireFuelsJEIPlugin implements IModPlugin {
    @Nonnull
    @Override
    public ResourceLocation getPluginUid() {
        return JustDireFuels.id("jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        ItemStack icon = new ItemStack(Registration.GeneratorFluidT1.get());
        registration.addRecipeCategories(new FluidFuelCategory(registration.getJeiHelpers().getGuiHelper(), icon));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        if (Minecraft.getInstance().level == null) return;
        List<FluidFuelRecipe> recipes = new ArrayList<>();
        for (Holder.Reference<Fluid> holder : BuiltInRegistries.FLUID.holders().toList()) {
            FluidFuelData data = holder.getData(FuelDataMaps.FLUID_FUELS);
            if (data == null) continue;
            Fluid fluid = holder.value();
            if (!fluid.defaultFluidState().isSource()) continue;
            recipes.add(new FluidFuelRecipe(new FluidStack(fluid, 1000), data.fePerMb()));
        }
        registration.addRecipes(FluidFuelCategory.TYPE, recipes);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
        registry.addRecipeCatalyst(new ItemStack(Registration.GeneratorFluidT1.get()), FluidFuelCategory.TYPE);
    }
}
