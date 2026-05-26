package com.breakinblocks.justdirefuels.mixin;

import com.breakinblocks.justdirefuels.datamap.FluidFuelData;
import com.breakinblocks.justdirefuels.datamap.FuelLookup;
import com.direwolf20.justdirethings.common.blockentities.GeneratorFluidT1BE;
import com.direwolf20.justdirethings.common.capabilities.JustDireFluidTank;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GeneratorFluidT1BE.class)
public abstract class GeneratorFluidT1BEMixin {
    @Inject(method = "getFePerFuelTick", at = @At("HEAD"), cancellable = true)
    private void justdirefuels$dataMapFePerMb(CallbackInfoReturnable<Integer> cir) {
        JustDireFluidTank tank = ((GeneratorFluidT1BE) (Object) this).getFluidTank();
        if (tank == null) return;
        FluidFuelData data = FuelLookup.getFluid(tank.getFluid().getFluid());
        if (data != null) cir.setReturnValue(data.fePerMb());
    }
}
