package com.breakinblocks.justdirefuels.datamap;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Optional;

public record ItemFuelData(Optional<Integer> fePerTick, Optional<Integer> burnSpeedMultiplier) {
    public static final Codec<ItemFuelData> CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
            Codec.INT.optionalFieldOf("fe_per_tick").forGetter(ItemFuelData::fePerTick),
            Codec.INT.optionalFieldOf("burn_speed_multiplier").forGetter(ItemFuelData::burnSpeedMultiplier)
        ).apply(instance, ItemFuelData::new)
    );
}
