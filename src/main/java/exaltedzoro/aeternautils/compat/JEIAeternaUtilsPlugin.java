package exaltedzoro.aeternautils.compat;

import exaltedzoro.aeternautils.AeternaUtils;
import exaltedzoro.aeternautils.recipe.BeyondAltarRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.List;
import java.util.Objects;

@JeiPlugin
public class JEIAeternaUtilsPlugin implements IModPlugin {
    public static RecipeType<BeyondAltarRecipe> BEYOND_ALTAR_TYPE = new RecipeType<>(BeyondAltarRecipeCategory.UID, BeyondAltarRecipe.class);

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(AeternaUtils.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new BeyondAltarRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager rm = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();
        List<BeyondAltarRecipe> beyondAltarRecipeList = rm.getAllRecipesFor(BeyondAltarRecipe.Type.INSTANCE);
        registration.addRecipes(BEYOND_ALTAR_TYPE, beyondAltarRecipeList);
    }

}
