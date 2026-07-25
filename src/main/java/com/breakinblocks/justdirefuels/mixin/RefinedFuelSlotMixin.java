package com.breakinblocks.justdirefuels.mixin;

import com.breakinblocks.justdirefuels.datamap.FuelLookup;
import com.direwolf20.justdirethings.common.containers.slots.RefinedFuelSlot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RefinedFuelSlot.class)
public abstract class RefinedFuelSlotMixin {
    @Inject(
        method = "mayPlace(Lnet/minecraft/world/item/ItemStack;)Z",
        at = @At("HEAD"),
        cancellable = true,
        require = 1
    )
    private void justdirefuels$acceptDataMapFuels(ItemStack itemStack, CallbackInfoReturnable<Boolean> cir) {
        if (itemStack.isEmpty()) return;
        ResourceHandler<FluidResource> handler = ItemAccess.forStack(itemStack).getCapability(Capabilities.Fluid.ITEM);
        if (handler == null) return;
        for (int i = 0; i < handler.size(); i++) {
            FluidResource resource = handler.getResource(i);
            if (resource.isEmpty()) continue;
            if (FuelLookup.getFluid(resource.getFluid()) != null) {
                cir.setReturnValue(true);
                return;
            }
        }
    }
}
