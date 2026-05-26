package com.breakinblocks.justdirefuels.client.jei;

import com.breakinblocks.justdirefuels.JustDireFuels;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class FluidFuelCategory implements IRecipeCategory<FluidFuelRecipe> {
    public static final RecipeType<FluidFuelRecipe> TYPE = RecipeType.create(
        JustDireFuels.MOD_ID, "fluid_generator_fuels", FluidFuelRecipe.class
    );

    private final IDrawable background;
    private final IDrawable icon;
    private final Component title = Component.translatable("jei.justdirefuels.fluid_fuel.title");

    public FluidFuelCategory(IGuiHelper guiHelper, ItemStack iconStack) {
        this.background = guiHelper.createBlankDrawable(120, 30);
        this.icon = guiHelper.createDrawableItemStack(iconStack);
    }

    @Override
    public RecipeType<FluidFuelRecipe> getRecipeType() { return TYPE; }

    @Override
    public Component getTitle() { return title; }

    @Override
    public IDrawable getBackground() { return background; }

    @Override
    public IDrawable getIcon() { return icon; }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, FluidFuelRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 4, 6)
            .addIngredient(NeoForgeTypes.FLUID_STACK, recipe.fluid());
    }

    @Override
    public void draw(FluidFuelRecipe recipe, IRecipeSlotsView slots, GuiGraphics graphics, double mouseX, double mouseY) {
        Component text = Component.translatable("jei.justdirefuels.fluid_fuel.fe_per_mb", recipe.fePerMb());
        Font font = Minecraft.getInstance().font;
        graphics.drawString(font, text, 30, 11, 0x404040, false);
    }
}
