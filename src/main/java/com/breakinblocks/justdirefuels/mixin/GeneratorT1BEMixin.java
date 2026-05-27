package com.breakinblocks.justdirefuels.mixin;

import com.breakinblocks.justdirefuels.datamap.FuelLookup;
import com.breakinblocks.justdirefuels.datamap.ItemFuelData;
import com.direwolf20.justdirethings.common.blockentities.GeneratorT1BE;
import com.direwolf20.justdirethings.common.capabilities.GeneratorItemHandler;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.item.ItemResource;
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

    @Shadow public abstract GeneratorItemHandler getMachineHandler();

    @Unique
    private int justdirefuels$activeFePerTick = -1;

    @Unique
    private ItemResource justdirefuels$currentFuelResource() {
        GeneratorItemHandler handler = getMachineHandler();
        return (ItemResource) handler.getResource(0);
    }

    @Inject(method = "doBurn", at = @At("HEAD"))
    private void justdirefuels$captureFePerTick(CallbackInfo ci) {
        justdirefuels$activeFePerTick = -1;
        ItemResource resource = justdirefuels$currentFuelResource();
        if (resource.isEmpty()) return;
        ItemFuelData data = FuelLookup.getItem(resource.getItem());
        if (data == null) return;
        data.fePerTick().ifPresent(v -> justdirefuels$activeFePerTick = v);
    }

    @Inject(method = "doBurn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getCraftingRemainder()Lnet/minecraft/world/item/ItemStackTemplate;"))
    private void justdirefuels$overrideBurnMultiplier(CallbackInfo ci) {
        ItemResource resource = justdirefuels$currentFuelResource();
        if (resource.isEmpty()) return;
        ItemFuelData data = FuelLookup.getItem(resource.getItem());
        if (data == null) return;
        data.burnSpeedMultiplier().ifPresent(v -> fuelBurnMultiplier = v);
    }

    @Inject(method = "getFePerFuelTick", at = @At("HEAD"), cancellable = true)
    private void justdirefuels$dataMapFePerTick(CallbackInfoReturnable<Integer> cir) {
        if (justdirefuels$activeFePerTick > 0) cir.setReturnValue(justdirefuels$activeFePerTick);
    }

    @Inject(method = "saveAdditional", at = @At("TAIL"))
    private void justdirefuels$save(ValueOutput output, CallbackInfo ci) {
        if (justdirefuels$activeFePerTick > 0) output.putInt("justdirefuels:active_fe_per_tick", justdirefuels$activeFePerTick);
    }

    @Inject(method = "loadAdditional", at = @At("TAIL"))
    private void justdirefuels$load(ValueInput input, CallbackInfo ci) {
        justdirefuels$activeFePerTick = input.getIntOr("justdirefuels:active_fe_per_tick", -1);
    }
}
