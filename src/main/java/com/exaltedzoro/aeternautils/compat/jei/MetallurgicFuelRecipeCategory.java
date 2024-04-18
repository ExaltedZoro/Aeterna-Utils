package com.exaltedzoro.aeternautils.compat.jei;

import com.exaltedzoro.aeternautils.AeternaUtils;
import com.exaltedzoro.aeternautils.block.ModBlocks;
import com.exaltedzoro.aeternautils.recipe.MetallurgicFuelRecipe;
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
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;


public class MetallurgicFuelRecipeCategory implements IRecipeCategory<MetallurgicFuelRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(AeternaUtils.MOD_ID, "metallurgic_fuel");
    public IDrawable background;
    public IDrawable icon;
    static final RecipeType<MetallurgicFuelRecipe> RECIPE_TYPE = RecipeType.create(AeternaUtils.MOD_ID, "metallurgic_fuel", MetallurgicFuelRecipe.class);

    public MetallurgicFuelRecipeCategory(IGuiHelper helper) {
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.METALLURGIC_CATALYST.get()));
        this.background = helper.createBlankDrawable(128, 32);
    }

    @Override
    public RecipeType<MetallurgicFuelRecipe> getRecipeType() {
        return JEIAeternaUtilsPlugin.METALLURGIC_FUEL_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.literal("Metallurgic Fuel");
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
    public void setRecipe(IRecipeLayoutBuilder builder, MetallurgicFuelRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 8, 8).addItemStack(recipe.getInputItem().getItems()[0]);
    }

    @Override
    public void draw(MetallurgicFuelRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        Font renderer = Minecraft.getInstance().font;
        if(recipe.getFuel() == 1) {
            renderer.draw(stack, Component.literal("Grants ").append(Integer.toString(recipe.getFuel())).append(" charge"), 32, 12, 1);
        } else {
            renderer.draw(stack, Component.literal("Grants ").append(Integer.toString(recipe.getFuel())).append(" charges"), 32, 12, 1);
        }
        IRecipeCategory.super.draw(recipe, recipeSlotsView, stack, mouseX, mouseY);
    }
}
