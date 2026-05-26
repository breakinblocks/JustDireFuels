package com.breakinblocks.justdirefuels.mixin;

import com.breakinblocks.justdirefuels.datamap.FuelLookup;
import com.direwolf20.justdirethings.common.capabilities.GeneratorFluidItemHandler;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GeneratorFluidItemHandler.class)
public abstract class GeneratorFluidItemHandlerMixin {
    @Inject(method = "isItemValid", at = @At("HEAD"), cancellable = true)
    private void justdirefuels$acceptDataMapFuels(int slot, ItemStack itemStack, CallbackInfoReturnable<Boolean> cir) {
        if (itemStack.isEmpty()) return;
        IFluidHandlerItem fluidHandlerItem = itemStack.getCapability(Capabilities.FluidHandler.ITEM);
        if (fluidHandlerItem == null) return;
        FluidStack fluidStack = fluidHandlerItem.drain(1000, IFluidHandler.FluidAction.SIMULATE);
        if (fluidStack.getAmount() == 0) return;
        if (FuelLookup.getFluid(fluidStack.getFluid()) != null) cir.setReturnValue(true);
    }
}
