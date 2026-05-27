package com.breakinblocks.justdirefuels.client.jei;

import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public record ItemFuelRecipe(ItemStack item, Optional<Integer> fePerTick, Optional<Integer> burnSpeedMultiplier, int burnTime) {}
