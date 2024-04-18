package com.exaltedzoro.aeternautils.compat.jei;

import com.exaltedzoro.aeternautils.AeternaUtils;
import com.exaltedzoro.aeternautils.block.ModBlocks;
import com.exaltedzoro.aeternautils.recipe.MetallurgicCatalystRecipe;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class MetallurgicCatalystRecipeCategory implements IRecipeCategory<MetallurgicCatalystRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(AeternaUtils.MOD_ID, "metallurgic_catalyst");
    public IDrawable background;
    public IDrawable icon;
    static final RecipeType<MetallurgicCatalystRecipe> RECIPE_TYPE = RecipeType.create(AeternaUtils.MOD_ID, "metallurgic_catalyst", MetallurgicCatalystRecipe.class);

    public MetallurgicCatalystRecipeCategory(IGuiHelper helper) {
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.METALLURGIC_CATALYST.get()));
        this.background = helper.createBlankDrawable(128, 48);
    }

    @Override
    public RecipeType<MetallurgicCatalystRecipe> getRecipeType() {
        return JEIAeternaUtilsPlugin.METALLURGIC_CATALYST_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.literal("Metallurgic Catalyst");
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
    public void setRecipe(IRecipeLayoutBuilder builder, MetallurgicCatalystRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.CATALYST, 56, 16).addItemStack(new ItemStack(ModBlocks.METALLURGIC_CATALYST.get()));
        builder.addSlot(RecipeIngredientRole.INPUT, 8, 8).addItemStack(recipe.getInputItem().getItems()[0]);
        builder.addSlot(RecipeIngredientRole.INPUT, 8, 24).addItemStack(recipe.getInputBlock());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 104, 16).addItemStack(recipe.getResultItem());
    }

    @Override
    public void draw(MetallurgicCatalystRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        IRecipeCategory.super.draw(recipe, recipeSlotsView, stack, mouseX, mouseY);
    }
}
