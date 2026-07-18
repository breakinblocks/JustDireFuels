package com.breakinblocks.justdirefuels.mixin;

import com.breakinblocks.justdirefuels.datamap.FluidGeneratorTankValidator;
import com.direwolf20.justdirethings.common.blockentities.GeneratorFluidT1BE;
import com.direwolf20.justdirethings.common.blockentities.basebe.BaseMachineBE;
import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BaseMachineBE.class)
public abstract class BaseMachineBEMixin {
    @Inject(
        method = "loadAdditional(Lnet/minecraft/nbt/CompoundTag;Lnet/minecraft/core/HolderLookup$Provider;)V",
        at = @At("TAIL"),
        require = 1
    )
    private void justdirefuels$restoreFluidGeneratorValidator(
        CompoundTag tag,
        HolderLookup.Provider provider,
        CallbackInfo ci
    ) {
        if ((Object) this instanceof GeneratorFluidT1BE generator) {
            FluidGeneratorTankValidator.apply(generator.getData(Registration.GENERATOR_FLUID_HANDLER));
        }
    }
}
