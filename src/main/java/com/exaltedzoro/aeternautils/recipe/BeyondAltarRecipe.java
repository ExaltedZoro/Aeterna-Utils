package com.exaltedzoro.aeternautils.recipe;

import com.exaltedzoro.aeternautils.AeternaUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BeyondAltarRecipe implements Recipe<SimpleContainer> {
    private final ResourceLocation id;
    private final ItemStack output;
    private final Ingredient input;
    private final NonNullList<Ingredient> pedestalItems;
    private final int sourceCost;

    public BeyondAltarRecipe(ResourceLocation id, ItemStack output, Ingredient input, NonNullList<Ingredient> pedestalItems, int sourceCost) {
        this.id = id;
        this.output = output;
        this.input = input;
        this.pedestalItems = pedestalItems;
        this.sourceCost =  sourceCost;
    }

    @Override
    public boolean matches(SimpleContainer pContainer, Level pLevel) {
        if(pLevel.isClientSide) {
            return false;
        }

        return input.test(pContainer.getItem(0));
    }

    public boolean doPedestalsMatch (List<ItemStack> pedestals) {
        int emptyInIngredients = 0;
        int emptyInStacks = 0;
        for (Ingredient ingredient : pedestalItems) {
            if (ingredient.isEmpty()) {
                emptyInIngredients++;
            }
        }
        for (ItemStack stack : pedestals) {
            if (stack.isEmpty()) {
                emptyInStacks++;
            }
        }

        if (emptyInIngredients != emptyInStacks) {
            return false;
        }

        boolean matched;
        for (Ingredient pedestalItem : this.pedestalItems) {
            if(pedestalItem == Ingredient.EMPTY) {
                continue;
            }

            matched = false;
            for(int i = 0; i < pedestals.size(); i++) {
                ItemStack stack = pedestals.get(i);
                if(pedestalItem.test(stack)) {
                    pedestals.set(i, ItemStack.EMPTY);
                    matched = true;
                    break;
                }
            }

            if(!matched) {
                return false;
            }
        }

        return true;
    }

    @Override
    public ItemStack assemble(SimpleContainer pContainer) {
        return output;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return output.copy();
    }

    public NonNullList<Ingredient> getPedestalItems() {
        return this.pedestalItems;
    }

    public Ingredient getInput() {
        return input;
    }

    public int getSourceCost() {
        return sourceCost;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<BeyondAltarRecipe> {
        private Type() { }
        public static final BeyondAltarRecipe.Type INSTANCE = new BeyondAltarRecipe.Type();
        public static final String ID = "beyond_altar";
    }

    public static class Serializer implements RecipeSerializer<BeyondAltarRecipe> {
        public static final BeyondAltarRecipe.Serializer INSTANCE = new BeyondAltarRecipe.Serializer();
        public static final ResourceLocation ID = new ResourceLocation(AeternaUtils.MOD_ID, "beyond_altar");

        private Serializer() {

        }

        @Override
        public BeyondAltarRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "output"));
            Ingredient input = Ingredient.fromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "input"));
            JsonArray pedestals = GsonHelper.getAsJsonArray(pSerializedRecipe, "pedestalItems");
            NonNullList<Ingredient> pedestalItems = NonNullList.withSize(8, Ingredient.EMPTY);

            for (int i = 0; i < pedestals.size(); i++) {
                pedestalItems.set(i, Ingredient.fromJson(pedestals.get(i)));
            }

            int sourceCost = GsonHelper.getAsInt(pSerializedRecipe, "sourceCost");

            return new BeyondAltarRecipe(pRecipeId, output, input, pedestalItems, sourceCost);
        }

        @Override
        public @Nullable BeyondAltarRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            ItemStack output = pBuffer.readItem();
            Ingredient input = Ingredient.fromNetwork(pBuffer);
            NonNullList<Ingredient> pedestalItems = NonNullList.withSize(8, Ingredient.EMPTY);

            for (int i = 0; i < 8; i++) {
                pedestalItems.set(i, Ingredient.fromNetwork(pBuffer));
            }

            int sourceCost = pBuffer.readInt();

            return new BeyondAltarRecipe(pRecipeId, output, input, pedestalItems, sourceCost);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, BeyondAltarRecipe pRecipe) {
            pBuffer.writeItemStack(pRecipe.output, false);
            pRecipe.input.toNetwork(pBuffer);
            for (Ingredient ing : pRecipe.pedestalItems) {
                ing.toNetwork(pBuffer);
            }
            pBuffer.writeInt(pRecipe.sourceCost);
        }
    }
}
