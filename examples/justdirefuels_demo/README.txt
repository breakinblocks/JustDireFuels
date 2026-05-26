Just Dire Fuels — Demo Datapack
================================

What this datapack does
-----------------------

1. FLUID GENERATOR T1 — accepts lava buckets
   - minecraft:lava is registered at 100 FE/mB.
   - Place a bucket of lava in the Fluid Generator T1's input slot.
   - The generator will run on lava.

2. FLUID GENERATOR T1 — buffs T2 refined fuel
   - justdirethings:refined_t2_fluid_source is overridden from 450 to 999 FE/mB.
   - Check the "Fluid Generator Fuels" JEI category to confirm.

3. SOLID GENERATOR T1 — accepts diamonds (overkill demo)
   - minecraft:diamond is added to NeoForge's furnace_fuels at 32000 ticks burn time.
   - It is also added to justdirefuels:item_fuels with fe_per_tick=500 and burn_speed_multiplier=8.
   - Drop a diamond in the Solid Generator T1 to feel the power.

How to install
--------------

Drop the entire 'justdirefuels_demo' folder into your world's datapacks folder:

  <minecraft instance>/saves/<your-world>/datapacks/justdirefuels_demo/

Then in-game:
  /reload
  /datapack enable "file/justdirefuels_demo"   (if not already enabled)

If you change the JSON files, run /reload again.

Verifying it works
------------------

- JEI: open JEI, search "Fluid Generator T1". Click it. The "Fluid Generator Fuels"
  category should now list lava (100 FE/mB) alongside the T2/T3/T4 fuels, and the
  T2 fuel should read 999 FE/mB instead of 450.

- Solid generator: place a Generator T1, give it a lever or signal, drop a single
  diamond in the fuel slot. It should burn for 32000 / (config burn speed * 8) ticks
  and produce 500 FE per burn tick.

Removing the datapack
---------------------

  /datapack disable "file/justdirefuels_demo"

All overridden values revert to the addon's defaults (mirroring stock JDT values).
