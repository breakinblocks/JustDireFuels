# Just Dire Fuels

A NeoForge 1.21.1 addon for [Just Dire Things](https://www.curseforge.com/minecraft/mc-mods/just-dire-things) that replaces JDT's hardcoded, config-locked fuel values with **data-driven fuels** for both the Fluid Generator T1 and the Solid Generator T1. Datapacks can add new fuels, override existing values, and the changes are reflected in JEI.

## Requirements

- Minecraft 1.21.1
- NeoForge 21.1.209+
- Just Dire Things 1.5.7+
- JEI (optional, for the in-game fuel browser)

## What it changes

### Fluid Generator T1

In stock JDT, the fluid generator only accepts fluids that implement the internal `RefinedFuel` interface, and energy output per millibucket is locked to three server-config values (`fuel_tier2_fe_per_mb`, `fuel_tier3_fe_per_mb`, `fuel_tier4_fe_per_mb`). New mods or packs cannot register fuels.

Just Dire Fuels adds the `justdirefuels:fluid_fuels` data map on the `minecraft:fluid` registry. Any fluid present in the map is now a valid generator fuel at the rate the map specifies.

```json5
// data/<your-pack>/data_maps/fluid/fluid_fuels.json
{
  "values": {
    "minecraft:lava": { "fe_per_mb": 100 },
    "yourmod:plasma":  { "fe_per_mb": 7500 }
  }
}
```

The addon ships defaults mirroring JDT's current config values so out-of-the-box behavior is unchanged. Datapacks can override any of them.

### Solid Generator T1

JDT's solid generator already reads burn time from NeoForge's `furnace_fuels` data map, so adding new burnable items has always been datapack-driven. What was *not* datapack-driven was the FE generated per burn tick and the per-fuel burn-speed multiplier (these were hardcoded in `Coal_T1`/`Coal_T2`/etc. with a single global config knob for FE/tick).

Just Dire Fuels adds the `justdirefuels:item_fuels` data map on the `minecraft:item` registry with two optional fields:

```json5
// data/<your-pack>/data_maps/item/item_fuels.json
{
  "values": {
    "minecraft:diamond":         { "fe_per_tick": 500, "burn_speed_multiplier": 8 },
    "justdirethings:coal_t2":    { "burn_speed_multiplier": 6 }
  }
}
```

| Field | Default behavior if omitted |
|-------|------------------------------|
| `fe_per_tick` | Falls back to JDT's `generator_t1_fe_per_fuel_tick` server config (15). |
| `burn_speed_multiplier` | Falls back to JDT's hardcoded value (1 for items not specially handled by JDT). |

Solid fuels still need a `burn_time` in NeoForge's `furnace_fuels` data map — that's separate, and the addon does not touch it.

### JEI

A new "Fluid Generator Fuels" category lists every fluid currently in the data map with its FE/mB rate. The Fluid Generator T1 block is the recipe catalyst, so right-clicking it in JEI takes you straight to the fuel list.

## Installation

Drop the mod into your `mods/` folder alongside the JDT jar. That's it.

## Sample datapack

A working demo datapack lives at `examples/justdirefuels_demo/`. It does three things at once:

1. Makes vanilla lava a fluid fuel at 100 FE/mB.
2. Overrides JDT's T2 refined fuel from 450 to 999 FE/mB.
3. Makes vanilla diamond a (rather overpowered) solid fuel.

Copy the folder into `<world>/datapacks/`, `/reload`, and check JEI.

## How it works under the hood

The addon uses four mixins against JDT. None of them remove or break existing JDT behavior; they layer a data-map check in front of it.

| Mixin target | What it does |
|---|---|
| `GeneratorFluidT1BE.getFePerFuelTick` | Returns data-map `fe_per_mb` when the tank's fluid is in the map; otherwise lets JDT's `RefinedFuel.fePerMb` run. |
| `GeneratorFluidItemHandler.isItemValid` | Accepts buckets/containers of any fluid present in the data map. |
| `JustDireFluidTank.isFluidValid` | Accepts any fluid in the data map (lets non-`RefinedFuel` fluids settle into the tank). |
| `GeneratorT1BE` (capture + getter + NBT) | Snapshots data-map `fe_per_tick` and `burn_speed_multiplier` at burn start, persists them across world reloads, and serves them from the getters during the burn. |

## License

MIT. See [LICENSE.md](LICENSE.md).

## Changelog

See [metadata/changelogs/](metadata/changelogs/).
