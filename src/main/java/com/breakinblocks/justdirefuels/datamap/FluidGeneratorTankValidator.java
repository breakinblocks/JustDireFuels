package com.breakinblocks.justdirefuels.datamap;

import com.direwolf20.justdirethings.common.capabilities.JustDireFluidTank;
import com.direwolf20.justdirethings.common.fluids.basefluids.RefinedFuel;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.function.Predicate;

public final class FluidGeneratorTankValidator {
    private static final Predicate<FluidStack> VALIDATOR = stack ->
        stack.getFluid() instanceof RefinedFuel || FuelLookup.getFluid(stack.getFluid()) != null;

    private FluidGeneratorTankValidator() {}

    public static void apply(JustDireFluidTank tank) {
        if (tank != null) tank.setValidator(VALIDATOR);
    }
}
