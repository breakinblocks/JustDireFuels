package com.breakinblocks.justdirefuels.client.jei;

import com.breakinblocks.justdirefuels.JustDireFuels;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.placement.HorizontalAlignment;
import mezz.jei.api.gui.placement.VerticalAlignment;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class FluidFuelCategory extends AbstractRecipeCategory<FluidFuelRecipe> {
    public static final IRecipeType<FluidFuelRecipe> TYPE = IRecipeType.create(
        JustDireFuels.MOD_ID, "fluid_generator_fuels", FluidFuelRecipe.class
    );

    private static final int WIDTH = 150;
    private static final int HEIGHT = 36;

    public FluidFuelCategory(IGuiHelper guiHelper, ItemStack iconStack) {
        super(TYPE,
            Component.translatable("jei.justdirefuels.fluid_fuel.title"),
            guiHelper.createDrawableItemStack(iconStack),
            WIDTH, HEIGHT);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, FluidFuelRecipe recipe, IFocusGroup focuses) {
        builder.addInputSlot(4, 9)
            .setStandardSlotBackground()
            .add(recipe.fluid().getFluid(), recipe.fluid().getAmount());
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, FluidFuelRecipe recipe, IFocusGroup focuses) {
        builder.addRecipeArrow().setPosition(28, 10);

        Component text = Component.translatable(
            "jei.justdirefuels.fluid_fuel.fe_per_mb",
            Component.literal(String.valueOf(recipe.fePerMb())).withStyle(ChatFormatting.WHITE)
        ).withStyle(ChatFormatting.GRAY);

        builder.addText(text, WIDTH - 60, HEIGHT)
            .setPosition(60, 0)
            .setTextAlignment(HorizontalAlignment.LEFT)
            .setTextAlignment(VerticalAlignment.CENTER);
    }
}
