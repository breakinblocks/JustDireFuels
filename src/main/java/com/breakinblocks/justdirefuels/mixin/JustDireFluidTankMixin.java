package com.breakinblocks.justdirefuels.mixin;

import com.breakinblocks.justdirefuels.datamap.DataMapFuelTank;
import com.breakinblocks.justdirefuels.datamap.FuelLookup;
import com.direwolf20.justdirethings.common.capabilities.JustDireFluidTank;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(JustDireFluidTank.class)
public abstract class JustDireFluidTankMixin implements DataMapFuelTank {
    @Unique
    private boolean justdirefuels$dataMapFuelsEnabled;

    @Override
    public void justdirefuels$enableDataMapFuels() {
        justdirefuels$dataMapFuelsEnabled = true;
    }

    @Override
    public boolean justdirefuels$dataMapFuelsEnabled() {
        return justdirefuels$dataMapFuelsEnabled;
    }

    @Inject(
        method = "isValid(ILnet/neoforged/neoforge/transfer/fluid/FluidResource;)Z",
        at = @At("HEAD"),
        cancellable = true,
        require = 1
    )
    private void justdirefuels$acceptDataMapFluid(int index, FluidResource resource, CallbackInfoReturnable<Boolean> cir) {
        if (!justdirefuels$dataMapFuelsEnabled) return;
        if (resource.isEmpty()) return;
        if (FuelLookup.getFluid(resource.getFluid()) != null) cir.setReturnValue(true);
    }
}
