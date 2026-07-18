package com.breakinblocks.justdirefuels.gametest;

import com.breakinblocks.justdirefuels.datamap.FluidFuelData;
import com.breakinblocks.justdirefuels.datamap.FuelLookup;
import com.direwolf20.justdirethings.common.blockentities.GeneratorFluidT1BE;
import com.direwolf20.justdirethings.common.capabilities.JustDireFluidTank;
import com.direwolf20.justdirethings.common.containers.slots.RefinedFuelSlot;
import com.direwolf20.justdirethings.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import net.neoforged.neoforge.items.IItemHandler;

@Mod(FluidGeneratorInputGameTests.MOD_ID)
@GameTestHolder(FluidGeneratorInputGameTests.MOD_ID)
@PrefixGameTestTemplate(false)
public final class FluidGeneratorInputGameTests {
    public static final String MOD_ID = "justdirefuelstest";

    private static final String TEMPLATE_NAMESPACE = "minecraft";
    private static final String TEMPLATE = "trial_chambers/decor/empty_pot";
    private static final BlockPos MACHINE_POS = BlockPos.ZERO;

    @GameTest(templateNamespace = TEMPLATE_NAMESPACE, template = TEMPLATE)
    public static void fluidCapabilityAcceptsDataMapFuel(GameTestHelper helper) {
        GeneratorFluidT1BE generator = placeGenerator(helper);
        FluidFuelData lavaFuel = FuelLookup.getFluid(Fluids.LAVA);
        helper.assertTrue(lavaFuel != null, "lava must be registered in the fluid fuel data map");
        helper.assertValueEqual(lavaFuel.fePerMb(), 100, "lava FE per mB");

        IFluidHandler fluidHandler = helper.getLevel().getCapability(
            Capabilities.FluidHandler.BLOCK,
            helper.absolutePos(MACHINE_POS),
            Direction.NORTH
        );
        helper.assertTrue(fluidHandler != null, "fluid generator must expose a block fluid capability");
        helper.assertValueEqual(
            fluidHandler.fill(new FluidStack(Fluids.WATER, 1000), IFluidHandler.FluidAction.SIMULATE),
            0,
            "unregistered water fill"
        );
        helper.assertValueEqual(
            fluidHandler.fill(new FluidStack(Fluids.LAVA, 1000), IFluidHandler.FluidAction.EXECUTE),
            1000,
            "data-map lava fill"
        );
        helper.assertTrue(generator.getFluidTank().getFluid().is(Fluids.LAVA), "generator tank must contain lava");
        helper.succeed();
    }

    @GameTest(templateNamespace = TEMPLATE_NAMESPACE, template = TEMPLATE)
    public static void bucketRightClickFillsGenerator(GameTestHelper helper) {
        GeneratorFluidT1BE generator = placeGenerator(helper);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.LAVA_BUCKET));

        helper.useBlock(MACHINE_POS, player);

        helper.assertTrue(player.getMainHandItem().is(Items.BUCKET), "right-click input must leave an empty bucket");
        helper.assertTrue(generator.getFluidTank().getFluid().is(Fluids.LAVA), "right-click input must fill lava");
        helper.assertValueEqual(generator.getFluidTank().getFluidAmount(), 1000, "right-click lava amount");
        helper.succeed();
    }

    @GameTest(templateNamespace = TEMPLATE_NAMESPACE, template = TEMPLATE)
    public static void guiAndItemCapabilityAcceptLavaBucket(GameTestHelper helper) {
        GeneratorFluidT1BE generator = placeGenerator(helper);
        RefinedFuelSlot slot = new RefinedFuelSlot(generator.getMachineHandler(), 0, 0, 0);
        helper.assertTrue(slot.mayPlace(new ItemStack(Items.LAVA_BUCKET)), "GUI fuel slot must accept a lava bucket");
        helper.assertFalse(slot.mayPlace(new ItemStack(Items.WATER_BUCKET)), "GUI fuel slot must reject a water bucket");

        IItemHandler itemHandler = helper.getLevel().getCapability(
            Capabilities.ItemHandler.BLOCK,
            helper.absolutePos(MACHINE_POS),
            Direction.NORTH
        );
        helper.assertTrue(itemHandler != null, "fluid generator must expose a block item capability");
        ItemStack remainder = itemHandler.insertItem(0, new ItemStack(Items.LAVA_BUCKET), false);
        helper.assertTrue(remainder.isEmpty(), "item-pipe insertion must accept a lava bucket");

        generator.handleItemStack();

        helper.assertTrue(generator.getMachineHandler().getStackInSlot(0).is(Items.BUCKET), "draining must leave an empty bucket");
        helper.assertTrue(generator.getFluidTank().getFluid().is(Fluids.LAVA), "item input must fill lava");
        helper.assertValueEqual(generator.getFluidTank().getFluidAmount(), 1000, "item-input lava amount");
        helper.succeed();
    }

    @GameTest(templateNamespace = TEMPLATE_NAMESPACE, template = TEMPLATE)
    public static void lavaGeneratesConfiguredEnergy(GameTestHelper helper) {
        GeneratorFluidT1BE generator = placeGenerator(helper);
        helper.assertValueEqual(
            generator.getFluidTank().fill(new FluidStack(Fluids.LAVA, 1), IFluidHandler.FluidAction.EXECUTE),
            1,
            "one mB lava fill"
        );
        helper.assertValueEqual(generator.getFePerFuelTick(), 100, "data-map energy value");

        generator.doGenerate();

        helper.assertValueEqual(generator.getFluidTank().getFluidAmount(), 0, "consumed lava amount");
        helper.assertValueEqual(generator.getEnergyStorage().getEnergyStored(), 100, "generated energy");
        helper.succeed();
    }

    @GameTest(templateNamespace = TEMPLATE_NAMESPACE, template = TEMPLATE)
    public static void deserializedGeneratorAcceptsDataMapFuel(GameTestHelper helper) {
        GeneratorFluidT1BE original = placeGenerator(helper);
        helper.assertValueEqual(
            original.getFluidTank().fill(new FluidStack(Fluids.LAVA, 1), IFluidHandler.FluidAction.EXECUTE),
            1,
            "saved lava fill"
        );
        CompoundTag saved = original.saveWithFullMetadata(helper.getLevel().registryAccess());

        helper.setBlock(MACHINE_POS, Blocks.AIR);
        GeneratorFluidT1BE reloaded = placeGenerator(helper);
        reloaded.loadWithComponents(saved, helper.getLevel().registryAccess());

        IFluidHandler fluidHandler = helper.getLevel().getCapability(
            Capabilities.FluidHandler.BLOCK,
            helper.absolutePos(MACHINE_POS),
            Direction.NORTH
        );
        helper.assertTrue(fluidHandler != null, "reloaded generator must expose a block fluid capability");
        helper.assertTrue(fluidHandler.getFluidInTank(0).is(Fluids.LAVA), "reloaded tank must retain saved lava");
        fluidHandler.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.EXECUTE);
        helper.assertValueEqual(
            fluidHandler.fill(new FluidStack(Fluids.WATER, 1000), IFluidHandler.FluidAction.SIMULATE),
            0,
            "reloaded generator water fill"
        );
        helper.assertValueEqual(
            fluidHandler.fill(new FluidStack(Fluids.LAVA, 1000), IFluidHandler.FluidAction.EXECUTE),
            1000,
            "reloaded generator lava fill"
        );
        helper.succeed();
    }

    @GameTest(templateNamespace = TEMPLATE_NAMESPACE, template = TEMPLATE)
    public static void preservesAllJdtRefinedFuels(GameTestHelper helper) {
        Fluid[] refinedFuels = {
            Registration.REFINED_T2_FLUID_SOURCE.get(),
            Registration.REFINED_T3_FLUID_SOURCE.get(),
            Registration.REFINED_T4_FLUID_SOURCE.get()
        };

        for (Fluid refinedFuel : refinedFuels) {
            helper.setBlock(MACHINE_POS, Blocks.AIR);
            GeneratorFluidT1BE generator = placeGenerator(helper);
            helper.assertValueEqual(
                generator.getFluidTank().fill(new FluidStack(refinedFuel, 1), IFluidHandler.FluidAction.EXECUTE),
                1,
                "JDT refined fuel fill"
            );
        }
        helper.succeed();
    }

    @GameTest(templateNamespace = TEMPLATE_NAMESPACE, template = TEMPLATE)
    public static void unrelatedTankValidatorIsUnchanged(GameTestHelper helper) {
        JustDireFluidTank waterOnlyTank = new JustDireFluidTank(1000, stack -> stack.is(Fluids.WATER));
        helper.assertValueEqual(
            waterOnlyTank.fill(new FluidStack(Fluids.LAVA, 1000), IFluidHandler.FluidAction.SIMULATE),
            0,
            "unrelated tank lava fill"
        );
        helper.assertValueEqual(
            waterOnlyTank.fill(new FluidStack(Fluids.WATER, 1000), IFluidHandler.FluidAction.EXECUTE),
            1000,
            "unrelated tank water fill"
        );
        helper.succeed();
    }

    private static GeneratorFluidT1BE placeGenerator(GameTestHelper helper) {
        helper.setBlock(MACHINE_POS, Registration.GeneratorFluidT1.get());
        return helper.getBlockEntity(MACHINE_POS);
    }
}
