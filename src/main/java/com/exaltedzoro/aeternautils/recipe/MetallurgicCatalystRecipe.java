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

public class MetallurgicCatalystRecipe implements Recipe<SimpleContainer> {
    private final ResourceLocation id;
    private final ItemStack output;
    private final Ingredient inputItem;
    private final ItemStack inputBlock;

    public MetallurgicCatalystRecipe(ResourceLocation id, ItemStack output, Ingredient inputItem, ItemStack inputBlock) {
        this.id = id;
        this.output = output;
        this.inputItem = inputItem;
        this.inputBlock = inputBlock;
    }

    @Override
    public boolean matches(SimpleContainer pContainer, Level pLevel) {
        if(pLevel.isClientSide) {
            return false;
        }

        if(pContainer.getContainerSize() == 1) {
            return inputItem.test(pContainer.getItem(0));
        } else {
            return inputItem.test(pContainer.getItem(0)) & inputBlock.is(pContainer.getItem(1).getItem());
        }

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
        return output;
    }

    public Ingredient getInputItem() {
        return inputItem;
    }


    public ItemStack getInputBlock() {
        return inputBlock;
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

    public static class Type implements RecipeType<MetallurgicCatalystRecipe> {
        private Type() { }
        public static final MetallurgicCatalystRecipe.Type INSTANCE = new MetallurgicCatalystRecipe.Type();
        public static final String ID = "metallurgic_catalyst";
    }

    public static class Serializer implements RecipeSerializer<MetallurgicCatalystRecipe> {
        public static final MetallurgicCatalystRecipe.Serializer INSTANCE = new MetallurgicCatalystRecipe.Serializer();
        public static final ResourceLocation ID = new ResourceLocation(AeternaUtils.MOD_ID, "metallurgic_catalyst");

        private Serializer() {

        }

        @Override
        public MetallurgicCatalystRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "output"));
            Ingredient inputItem = Ingredient.fromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "inputItem"));
            ItemStack inputBlock = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "inputBlock"));

            return new MetallurgicCatalystRecipe(pRecipeId, output, inputItem, inputBlock);
        }

        @Override
        public @Nullable MetallurgicCatalystRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            ItemStack output = pBuffer.readItem();
            Ingredient inputItem = Ingredient.fromNetwork(pBuffer);
            ItemStack inputBlock = pBuffer.readItem();

            return new MetallurgicCatalystRecipe(pRecipeId, output, inputItem, inputBlock);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, MetallurgicCatalystRecipe pRecipe) {
            pBuffer.writeItemStack(pRecipe.output, false);
            pRecipe.inputItem.toNetwork(pBuffer);
            pBuffer.writeItemStack(pRecipe.getInputBlock(), false);
        }
    }
}
