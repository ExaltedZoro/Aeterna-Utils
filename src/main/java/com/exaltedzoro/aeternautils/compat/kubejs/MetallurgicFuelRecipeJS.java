package com.exaltedzoro.aeternautils.compat.kubejs;

import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;

public interface MetallurgicFuelRecipeJS {
    RecipeKey<InputItem> INPUT = ItemComponents.INPUT.key("inputItem");
    RecipeKey<Integer> FUEL = NumberComponent.INT.key("fuel");


    RecipeSchema SCHEMA = new RecipeSchema(INPUT, FUEL);
}
