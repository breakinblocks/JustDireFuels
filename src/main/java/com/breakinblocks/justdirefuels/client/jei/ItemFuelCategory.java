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

public class ItemFuelCategory extends AbstractRecipeCategory<ItemFuelRecipe> {
    public static final IRecipeType<ItemFuelRecipe> TYPE = IRecipeType.create(
        JustDireFuels.MOD_ID, "item_generator_fuels", ItemFuelRecipe.class
    );

    private static final int WIDTH = 170;
    private static final int HEIGHT = 54;
    private static final int LINE_HEIGHT = 12;

    public ItemFuelCategory(IGuiHelper guiHelper, ItemStack iconStack) {
        super(TYPE,
            Component.translatable("jei.justdirefuels.item_fuel.title"),
            guiHelper.createDrawableItemStack(iconStack),
            WIDTH, HEIGHT);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ItemFuelRecipe recipe, IFocusGroup focuses) {
        builder.addInputSlot(4, 18)
            .setStandardSlotBackground()
            .add(recipe.item());
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, ItemFuelRecipe recipe, IFocusGroup focuses) {
        builder.addAnimatedRecipeFlame(recipe.burnTime())
            .setPosition(7, 1);

        builder.addRecipeArrow().setPosition(28, 19);

        int textX = 60;
        int textWidth = WIDTH - textX;

        Component feLine;
        if (recipe.fePerTick().isPresent()) {
            feLine = Component.translatable(
                "jei.justdirefuels.item_fuel.fe_per_tick",
                Component.literal(String.valueOf(recipe.fePerTick().get())).withStyle(ChatFormatting.WHITE)
            ).withStyle(ChatFormatting.GRAY);
        } else {
            feLine = Component.translatable("jei.justdirefuels.item_fuel.fe_per_tick_default")
                .withStyle(ChatFormatting.DARK_GRAY);
        }
        builder.addText(feLine, textWidth, LINE_HEIGHT)
            .setPosition(textX, 6)
            .setTextAlignment(HorizontalAlignment.LEFT)
            .setTextAlignment(VerticalAlignment.CENTER);

        int row = 1;
        if (recipe.burnSpeedMultiplier().isPresent()) {
            Component speedLine = Component.translatable(
                "jei.justdirefuels.item_fuel.burn_speed",
                Component.literal("x" + recipe.burnSpeedMultiplier().get()).withStyle(ChatFormatting.WHITE)
            ).withStyle(ChatFormatting.GRAY);
            builder.addText(speedLine, textWidth, LINE_HEIGHT)
                .setPosition(textX, 6 + row * 14)
                .setTextAlignment(HorizontalAlignment.LEFT)
                .setTextAlignment(VerticalAlignment.CENTER);
            row++;
        }

        Component burnLine = Component.translatable(
            "jei.justdirefuels.item_fuel.burn_time",
            Component.literal(String.valueOf(recipe.burnTime())).withStyle(ChatFormatting.WHITE)
        ).withStyle(ChatFormatting.GRAY);
        builder.addText(burnLine, textWidth, LINE_HEIGHT)
            .setPosition(textX, 6 + row * 14)
            .setTextAlignment(HorizontalAlignment.LEFT)
            .setTextAlignment(VerticalAlignment.CENTER);
    }
}
