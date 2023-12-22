package com.exaltedzoro.aeternautils.compat.jei;

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
    static final RecipeType<BeyondAltarRecipe> RECIPE_TYPE = RecipeType.create(AeternaUtils.MOD_ID, "beyond_altar", BeyondAltarRecipe.class);

    private final IDrawable icon;
    private final IDrawable background;

    public BeyondAltarRecipeCategory(IGuiHelper helper) {
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.BEYOND_ALTAR.get()));
        this.background = helper.createDrawable(TEXTURE, 0, 0, 128, 128);
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
        builder.addSlot(RecipeIngredientRole.INPUT, 56, 22).addIngredients(recipe.getInput());
        builder.addSlot(RecipeIngredientRole.INPUT, 0, 0).addIngredients(recipe.getPedestalItems().get(0));
        builder.addSlot(RecipeIngredientRole.INPUT, 0, 20).addIngredients(recipe.getPedestalItems().get(1));
        builder.addSlot(RecipeIngredientRole.INPUT, 0, 40).addIngredients(recipe.getPedestalItems().get(2));
        builder.addSlot(RecipeIngredientRole.INPUT, 0, 60).addIngredients(recipe.getPedestalItems().get(3));
        builder.addSlot(RecipeIngredientRole.INPUT, 112, 0).addIngredients(recipe.getPedestalItems().get(4));
        builder.addSlot(RecipeIngredientRole.INPUT, 112, 20).addIngredients(recipe.getPedestalItems().get(5));
        builder.addSlot(RecipeIngredientRole.INPUT, 112, 40).addIngredients(recipe.getPedestalItems().get(6));
        builder.addSlot(RecipeIngredientRole.INPUT, 112, 60).addIngredients(recipe.getPedestalItems().get(7));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 56, 70).addItemStack(recipe.getResultItem());
    }


}
