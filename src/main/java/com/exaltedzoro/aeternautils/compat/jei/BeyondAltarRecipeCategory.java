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
    private final int[] yValues = {0, 20, 40, 60};

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
        builder.addSlot(RecipeIngredientRole.INPUT, 56, 30).addIngredients(recipe.getInput());
        for(int i = 0; i < 8; i++) {
            int x;
            int y;
            if(i % 2 == 0) {
                x = 0;
                y = yValues[i];
            } else {
                x = 112;
                y = yValues[i - 1];
            }
            builder.addSlot(RecipeIngredientRole.INPUT, x, y).addIngredients(recipe.getPedestalItems().get(i));
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT, 56, 70).addItemStack(recipe.getResultItem());
    }


}
