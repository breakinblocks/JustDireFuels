package com.breakinblocks.justdirefuels.mixin;

import com.breakinblocks.justdirefuels.datamap.FluidFuelData;
import com.breakinblocks.justdirefuels.datamap.FluidGeneratorTankValidator;
import com.breakinblocks.justdirefuels.datamap.FuelLookup;
import com.direwolf20.justdirethings.common.blockentities.GeneratorFluidT1BE;
import com.direwolf20.justdirethings.common.capabilities.JustDireFluidTank;
import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GeneratorFluidT1BE.class)
public abstract class GeneratorFluidT1BEMixin {
    @Inject(
        method = "<init>(Lnet/minecraft/world/level/block/entity/BlockEntityType;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V",
        at = @At("RETURN"),
        require = 1
    )
    private void justdirefuels$acceptDataMapFluids(
        BlockEntityType<?> type,
        BlockPos pos,
        BlockState state,
        CallbackInfo ci
    ) {
        GeneratorFluidT1BE generator = (GeneratorFluidT1BE) (Object) this;
        FluidGeneratorTankValidator.apply(generator.getData(Registration.GENERATOR_FLUID_HANDLER));
    }

    @Inject(
        method = "getFluidTank()Lcom/direwolf20/justdirethings/common/capabilities/JustDireFluidTank;",
        at = @At("RETURN"),
        require = 1
    )
    private void justdirefuels$validateCurrentTank(CallbackInfoReturnable<JustDireFluidTank> cir) {
        FluidGeneratorTankValidator.apply(cir.getReturnValue());
    }

    @Inject(
        method = "getFePerFuelTick()I",
        at = @At("HEAD"),
        cancellable = true,
        require = 1
    )
    private void justdirefuels$dataMapFePerMb(CallbackInfoReturnable<Integer> cir) {
        JustDireFluidTank tank = ((GeneratorFluidT1BE) (Object) this).getFluidTank();
        if (tank == null) return;
        FluidFuelData data = FuelLookup.getFluid(tank.getFluid().getFluid());
        if (data != null) cir.setReturnValue(data.fePerMb());
    }
}
