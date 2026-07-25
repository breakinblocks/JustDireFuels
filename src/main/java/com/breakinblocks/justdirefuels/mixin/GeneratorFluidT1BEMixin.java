package com.breakinblocks.justdirefuels.mixin;

import com.breakinblocks.justdirefuels.datamap.DataMapFuelTank;
import com.breakinblocks.justdirefuels.datamap.FluidFuelData;
import com.breakinblocks.justdirefuels.datamap.FuelLookup;
import com.direwolf20.justdirethings.common.blockentities.GeneratorFluidT1BE;
import com.direwolf20.justdirethings.common.capabilities.JustDireFluidTank;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
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
    private void justdirefuels$enableDataMapFuels(
        BlockEntityType<?> type,
        BlockPos pos,
        BlockState state,
        CallbackInfo ci
    ) {
        DataMapFuelTank.enable(((GeneratorFluidT1BE) (Object) this).getFluidTank());
    }

    @Inject(
        method = "getFluidTank()Lcom/direwolf20/justdirethings/common/capabilities/JustDireFluidTank;",
        at = @At("RETURN"),
        require = 1
    )
    private void justdirefuels$enableCurrentTank(CallbackInfoReturnable<JustDireFluidTank> cir) {
        DataMapFuelTank.enable(cir.getReturnValue());
    }

    @Inject(
        method = "getFePerFuelTick()I",
        at = @At("HEAD"),
        cancellable = true,
        require = 1
    )
    private void justdirefuels$dataMapFePerMb(CallbackInfoReturnable<Integer> cir) {
        JustDireFluidTank tank = ((GeneratorFluidT1BE) (Object) this).getFluidTank();
        if (tank == null || tank.size() == 0) return;
        FluidResource resource = tank.getResource(0);
        if (resource.isEmpty()) return;
        FluidFuelData data = FuelLookup.getFluid(resource.getFluid());
        if (data != null) cir.setReturnValue(data.fePerMb());
    }
}
