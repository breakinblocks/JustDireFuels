package com.breakinblocks.justdirefuels.datamap;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record FluidFuelData(int fePerMb) {
    public static final Codec<FluidFuelData> CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
            Codec.INT.fieldOf("fe_per_mb").forGetter(FluidFuelData::fePerMb)
        ).apply(instance, FluidFuelData::new)
    );
}
