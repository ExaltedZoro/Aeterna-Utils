package com.exaltedzoro.aeternautils.recipe;

import com.exaltedzoro.aeternautils.AeternaUtils;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class MetallurgicFuelRecipe implements Recipe<SimpleContainer> {
    private final ResourceLocation id;
    private final Ingredient inputItem;
    private final int fuel;

    public MetallurgicFuelRecipe(ResourceLocation id, Ingredient inputItem, int fuel) {
        this.id = id;
        this.inputItem = inputItem;
        this.fuel = fuel;
    }

    @Override
    public boolean matches(SimpleContainer pContainer, Level pLevel) {
        return inputItem.test(pContainer.getItem(0));
    }

    @Override
    public ItemStack assemble(SimpleContainer pContainer) {
        return null;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return null;
    }

    public Ingredient getInputItem() {
        return inputItem;
    }

    public int getFuel() {
        return fuel;
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

    public static class Type implements RecipeType<MetallurgicFuelRecipe> {
        private Type() { }
        public static final MetallurgicFuelRecipe.Type INSTANCE = new MetallurgicFuelRecipe.Type();
        public static final String ID = "metallurgic_fuel";
    }

    public static class Serializer implements RecipeSerializer<MetallurgicFuelRecipe> {
        public static final MetallurgicFuelRecipe.Serializer INSTANCE = new MetallurgicFuelRecipe.Serializer();
        public static final ResourceLocation ID = new ResourceLocation(AeternaUtils.MOD_ID, "metallurgic_fuel");

        private Serializer() {

        }

        @Override
        public MetallurgicFuelRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            Ingredient inputItem = Ingredient.fromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "inputItem"));
            int fuel = GsonHelper.getAsInt(pSerializedRecipe, "fuel");

            return new MetallurgicFuelRecipe(pRecipeId, inputItem, fuel);
        }

        @Override
        public @Nullable MetallurgicFuelRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            Ingredient inputItem = Ingredient.fromNetwork(pBuffer);
            int fuel = pBuffer.readInt();

            return new MetallurgicFuelRecipe(pRecipeId, inputItem, fuel);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, MetallurgicFuelRecipe pRecipe) {
            pRecipe.inputItem.toNetwork(pBuffer);
            pBuffer.writeInt(pRecipe.fuel);
        }
    }
}
