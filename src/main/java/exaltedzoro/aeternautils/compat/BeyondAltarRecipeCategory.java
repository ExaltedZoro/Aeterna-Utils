package exaltedzoro.aeternautils.compat;

import exaltedzoro.aeternautils.AeternaUtils;
import exaltedzoro.aeternautils.block.ModBlocks;
import exaltedzoro.aeternautils.recipe.BeyondAltarRecipe;
import mezz.jei.api.JeiPlugin;
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

    private final IDrawable icon;

    public BeyondAltarRecipeCategory(IGuiHelper helper) {
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.BEYOND_ALTAR.get()));
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
        return null;
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
