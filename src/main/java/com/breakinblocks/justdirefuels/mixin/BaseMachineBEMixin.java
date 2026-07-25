package com.breakinblocks.justdirefuels.mixin;

import com.breakinblocks.justdirefuels.datamap.DataMapFuelTank;
import com.direwolf20.justdirethings.common.blockentities.GeneratorFluidT1BE;
import com.direwolf20.justdirethings.common.blockentities.basebe.BaseMachineBE;
import net.minecraft.world.level.storage.ValueInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BaseMachineBE.class)
public abstract class BaseMachineBEMixin {
    @Inject(
        method = "loadAdditional(Lnet/minecraft/world/level/storage/ValueInput;)V",
        at = @At("TAIL"),
        require = 1
    )
    private void justdirefuels$restoreFluidGeneratorTank(ValueInput input, CallbackInfo ci) {
        if ((Object) this instanceof GeneratorFluidT1BE generator) {
            DataMapFuelTank.enable(generator.getFluidTank());
        }
    }
}
