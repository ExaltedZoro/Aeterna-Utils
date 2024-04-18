package com.exaltedzoro.aeternautils.compat.jei;

import com.exaltedzoro.aeternautils.AeternaUtils;
import com.exaltedzoro.aeternautils.block.ModBlocks;
import com.exaltedzoro.aeternautils.recipe.BeyondAltarRecipe;
import com.exaltedzoro.aeternautils.recipe.MetallurgicCatalystRecipe;
import com.exaltedzoro.aeternautils.recipe.MetallurgicFuelRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.List;
import java.util.Objects;

@JeiPlugin
public class JEIAeternaUtilsPlugin implements IModPlugin {
    public static RecipeType<BeyondAltarRecipe> BEYOND_ALTAR_TYPE = new RecipeType<>(BeyondAltarRecipeCategory.UID, BeyondAltarRecipe.class);
    public static RecipeType<MetallurgicCatalystRecipe> METALLURGIC_CATALYST_TYPE = new RecipeType<>(MetallurgicCatalystRecipeCategory.UID, MetallurgicCatalystRecipe.class);
    public static RecipeType<MetallurgicFuelRecipe> METALLURGIC_FUEL_TYPE = new RecipeType<>(MetallurgicFuelRecipeCategory.UID, MetallurgicFuelRecipe.class);

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(AeternaUtils.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(
                new BeyondAltarRecipeCategory(registration.getJeiHelpers().getGuiHelper()),
                new MetallurgicCatalystRecipeCategory(registration.getJeiHelpers().getGuiHelper()),
                new MetallurgicFuelRecipeCategory(registration.getJeiHelpers().getGuiHelper())
        );

    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager rm = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();
        List<BeyondAltarRecipe> beyondAltarRecipeList = rm.getAllRecipesFor(BeyondAltarRecipe.Type.INSTANCE);
        registration.addRecipes(BEYOND_ALTAR_TYPE, beyondAltarRecipeList);
        List<MetallurgicCatalystRecipe> metallurgicCatalystRecipeList = rm.getAllRecipesFor(MetallurgicCatalystRecipe.Type.INSTANCE);
        registration.addRecipes(METALLURGIC_CATALYST_TYPE, metallurgicCatalystRecipeList);
        List<MetallurgicFuelRecipe> metallurgicFuelRecipeList = rm.getAllRecipesFor(MetallurgicFuelRecipe.Type.INSTANCE);
        registration.addRecipes(METALLURGIC_FUEL_TYPE, metallurgicFuelRecipeList);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.BEYOND_ALTAR.get()), BeyondAltarRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.METALLURGIC_CATALYST.get()), MetallurgicCatalystRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.METALLURGIC_CATALYST.get()), MetallurgicFuelRecipeCategory.RECIPE_TYPE);
    }
}
