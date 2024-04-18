package com.exaltedzoro.aeternautils.compat.kubejs;

import com.exaltedzoro.aeternautils.recipe.ModRecipes;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.recipe.schema.RegisterRecipeSchemasEvent;

public class KubeJSAeternaUtilsPlugin extends KubeJSPlugin {
    public static EventGroup GROUP = EventGroup.of("AeternaUtilsEvents");

    @Override
    public void registerRecipeSchemas(RegisterRecipeSchemasEvent event) {
        event.register(ModRecipes.BEYOND_ALTAR_SERIALIZER.getId(), BeyondAltarRecipeJS.SCHEMA);
        event.register(ModRecipes.METALLURGIC_CATALYST_SERIALIZER.getId(), MetallurgicCatalystRecipeJS.SCHEMA);
        event.register(ModRecipes.METALLURGIC_FUEL_SERIALIZER.getId(), MetallurgicFuelRecipeJS.SCHEMA);
    }

    @Override
    public void registerEvents() {
        GROUP.register();
    }
}
