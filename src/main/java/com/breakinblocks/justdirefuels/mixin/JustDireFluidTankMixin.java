package com.breakinblocks.justdirefuels.mixin;

import com.breakinblocks.justdirefuels.datamap.FuelLookup;
import com.direwolf20.justdirethings.common.capabilities.JustDireFluidTank;
import net.neoforged.neoforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(JustDireFluidTank.class)
public abstract class JustDireFluidTankMixin {
    @Inject(method = "isFluidValid(Lnet/neoforged/neoforge/fluids/FluidStack;)Z", at = @At("HEAD"), cancellable = true)
    private void justdirefuels$acceptDataMapFluid(FluidStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (FuelLookup.getFluid(stack.getFluid()) != null) cir.setReturnValue(true);
    }
}
