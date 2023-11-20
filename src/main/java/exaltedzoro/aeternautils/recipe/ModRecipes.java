package exaltedzoro.aeternautils.recipe;

import exaltedzoro.aeternautils.AeternaUtils;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, AeternaUtils.MOD_ID);

    public static final RegistryObject<RecipeSerializer<BeyondAltarRecipe>> BEYOND_ALTAR_SERIALIZER = SERIALIZERS.register("beyond_altar", () -> BeyondAltarRecipe.Serializer.INSTANCE);

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }
}
