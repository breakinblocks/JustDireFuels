package com.breakinblocks.justdirefuels.mixin;

import com.breakinblocks.justdirefuels.datamap.FuelLookup;
import com.breakinblocks.justdirefuels.datamap.ItemFuelData;
import com.direwolf20.justdirethings.common.blockentities.GeneratorT1BE;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GeneratorT1BE.class)
public abstract class GeneratorT1BEMixin {
    @Shadow int fuelBurnMultiplier;

    @Shadow public abstract ItemStackHandler getMachineHandler();

    @Unique
    private int justdirefuels$activeFePerTick = -1;

    @Inject(method = "doBurn", at = @At("HEAD"))
    private void justdirefuels$captureFePerTick(CallbackInfo ci) {
        ItemStack fuelStack = getMachineHandler().getStackInSlot(0);
        justdirefuels$activeFePerTick = -1;
        if (fuelStack.isEmpty()) return;
        ItemFuelData data = FuelLookup.getItem(fuelStack.getItem());
        if (data == null) return;
        data.fePerTick().ifPresent(v -> justdirefuels$activeFePerTick = v);
    }

    @Inject(method = "doBurn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;hasCraftingRemainingItem()Z"))
    private void justdirefuels$overrideBurnMultiplier(CallbackInfo ci) {
        ItemStack fuelStack = getMachineHandler().getStackInSlot(0);
        if (fuelStack.isEmpty()) return;
        ItemFuelData data = FuelLookup.getItem(fuelStack.getItem());
        if (data == null) return;
        data.burnSpeedMultiplier().ifPresent(v -> fuelBurnMultiplier = v);
    }

    @Inject(method = "getFePerFuelTick", at = @At("HEAD"), cancellable = true)
    private void justdirefuels$dataMapFePerTick(CallbackInfoReturnable<Integer> cir) {
        if (justdirefuels$activeFePerTick > 0) cir.setReturnValue(justdirefuels$activeFePerTick);
    }

    @Inject(method = "saveAdditional", at = @At("TAIL"))
    private void justdirefuels$save(CompoundTag tag, HolderLookup.Provider provider, CallbackInfo ci) {
        if (justdirefuels$activeFePerTick > 0) tag.putInt("justdirefuels:active_fe_per_tick", justdirefuels$activeFePerTick);
    }

    @Inject(method = "loadAdditional", at = @At("TAIL"))
    private void justdirefuels$load(CompoundTag tag, HolderLookup.Provider provider, CallbackInfo ci) {
        justdirefuels$activeFePerTick = tag.contains("justdirefuels:active_fe_per_tick")
            ? tag.getInt("justdirefuels:active_fe_per_tick") : -1;
    }
}
