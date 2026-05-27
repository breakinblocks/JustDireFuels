package com.breakinblocks.justdirefuels.mixin;

import com.breakinblocks.justdirefuels.datamap.FuelLookup;
import com.direwolf20.justdirethings.common.capabilities.JustDireFluidTank;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(JustDireFluidTank.class)
public abstract class JustDireFluidTankMixin {
    @Inject(method = "isValid(ILnet/neoforged/neoforge/transfer/fluid/FluidResource;)Z", at = @At("HEAD"), cancellable = true)
    private void justdirefuels$acceptDataMapFluid(int index, FluidResource resource, CallbackInfoReturnable<Boolean> cir) {
        if (resource.isEmpty()) return;
        if (FuelLookup.getFluid(resource.getFluid()) != null) cir.setReturnValue(true);
    }
}
