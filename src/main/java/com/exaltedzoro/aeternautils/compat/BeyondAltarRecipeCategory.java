package com.exaltedzoro.aeternautils.compat;

import com.exaltedzoro.aeternautils.AeternaUtils;
import com.exaltedzoro.aeternautils.block.ModBlocks;
import com.exaltedzoro.aeternautils.recipe.BeyondAltarRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class BeyondAltarRecipeCategory implements IRecipeCategory<BeyondAltarRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(AeternaUtils.MOD_ID, "beyond_altar");
    public static final ResourceLocation TEXTURE = new ResourceLocation(AeternaUtils.MOD_ID, "textures/gui/beyond_altar_jei_background.png");

    private final IDrawable icon;
    private final IDrawable background;

    public BeyondAltarRecipeCategory(IGuiHelper helper) {
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.BEYOND_ALTAR.get()));
        this.background = helper.createDrawable(TEXTURE, 0, 0, 100, 100);
    }

    @Override
    public RecipeType<BeyondAltarRecipe> getRecipeType() {
        return JEIAeternaUtilsPlugin.BEYOND_ALTAR_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.literal("Altar to the Beyond");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, BeyondAltarRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 10, 10).addIngredients(recipe.getInput());
        builder.addSlot(RecipeIngredientRole.INPUT, 20, 10).addIngredients(recipe.getPedestalItems().get(0));
        builder.addSlot(RecipeIngredientRole.INPUT, 30, 10).addIngredients(recipe.getPedestalItems().get(1));
        builder.addSlot(RecipeIngredientRole.INPUT, 40, 10).addIngredients(recipe.getPedestalItems().get(2));
        builder.addSlot(RecipeIngredientRole.INPUT, 50, 10).addIngredients(recipe.getPedestalItems().get(3));
        builder.addSlot(RecipeIngredientRole.INPUT, 60, 10).addIngredients(recipe.getPedestalItems().get(4));
        builder.addSlot(RecipeIngredientRole.INPUT, 70, 10).addIngredients(recipe.getPedestalItems().get(5));
        builder.addSlot(RecipeIngredientRole.INPUT, 80, 10).addIngredients(recipe.getPedestalItems().get(6));
        builder.addSlot(RecipeIngredientRole.INPUT, 90, 10).addIngredients(recipe.getPedestalItems().get(7));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 100, 10).addItemStack(recipe.getResultItem());
    }
}
