package com.exaltedzoro.aeternautils.compat.kubejs;

import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;

public interface BeyondAltarRecipeJS {
    RecipeKey<OutputItem> OUTPUT = ItemComponents.OUTPUT.key("output");
    RecipeKey<InputItem> INPUT = ItemComponents.INPUT.key("input");
    RecipeKey<InputItem[]> PEDESTAL_ITEMS = ItemComponents.INPUT_ARRAY.key("pedestalItems");
    RecipeKey<Integer> SOURCE = NumberComponent.INT.key("sourceCost");

    RecipeSchema SCHEMA = new RecipeSchema(OUTPUT, INPUT, PEDESTAL_ITEMS, SOURCE);
}
