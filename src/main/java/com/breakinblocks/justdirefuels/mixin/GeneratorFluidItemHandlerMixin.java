package com.breakinblocks.justdirefuels.mixin;

import com.breakinblocks.justdirefuels.datamap.FuelLookup;
import com.direwolf20.justdirethings.common.capabilities.GeneratorFluidItemHandler;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.item.ItemResource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GeneratorFluidItemHandler.class)
public abstract class GeneratorFluidItemHandlerMixin {
    @Inject(method = "isValid(ILnet/neoforged/neoforge/transfer/item/ItemResource;)Z", at = @At("HEAD"), cancellable = true)
    private void justdirefuels$acceptDataMapFuels(int slot, ItemResource resource, CallbackInfoReturnable<Boolean> cir) {
        if (resource.isEmpty()) return;
        ItemStack stack = resource.toStack();
        ResourceHandler<FluidResource> handler = ItemAccess.forStack(stack).getCapability(Capabilities.Fluid.ITEM);
        if (handler == null) return;
        for (int i = 0; i < handler.size(); i++) {
            FluidResource fluidResource = handler.getResource(i);
            if (fluidResource.isEmpty()) continue;
            if (FuelLookup.getFluid(fluidResource.getFluid()) != null) {
                cir.setReturnValue(true);
                return;
            }
        }
    }
}
