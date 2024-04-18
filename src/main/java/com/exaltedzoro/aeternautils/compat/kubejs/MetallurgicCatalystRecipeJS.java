package com.exaltedzoro.aeternautils.compat.kubejs;

import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;

public interface MetallurgicCatalystRecipeJS {
    RecipeKey<OutputItem> OUTPUT = ItemComponents.OUTPUT.key("output");
    RecipeKey<InputItem> INPUT_ITEM = ItemComponents.INPUT.key("inputItem");
    RecipeKey<InputItem> INPUT_BLOCK = ItemComponents.INPUT.key("inputBlock");

    RecipeSchema SCHEMA = new RecipeSchema(OUTPUT, INPUT_ITEM, INPUT_BLOCK);
}
