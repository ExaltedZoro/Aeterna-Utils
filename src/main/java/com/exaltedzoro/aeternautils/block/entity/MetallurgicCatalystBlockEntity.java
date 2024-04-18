package com.exaltedzoro.aeternautils.block.entity;

import com.exaltedzoro.aeternautils.recipe.MetallurgicCatalystRecipe;
import com.exaltedzoro.aeternautils.recipe.MetallurgicFuelRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class MetallurgicCatalystBlockEntity extends BlockEntity {
    private Item currentFuel = Items.AIR;
    private int fuel = 0;
    private final ItemStackHandler itemHandler = new ItemStackHandler() {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    public MetallurgicCatalystBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.METALLURGIC_CATALYST.get(), pPos, pBlockState);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.put("inventory", itemHandler.serializeNBT());
        nbt.putInt("fuel", fuel);
        nbt.putString("fuelItem", Registry.ITEM.getKey(currentFuel).toString());

        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemHandler.deserializeNBT(nbt.getCompound("inventory"));
        fuel = nbt.getInt("fuel");
        currentFuel = Registry.ITEM.get(new ResourceLocation(nbt.getString("fuelItem")));
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for(int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(level, worldPosition, inventory);
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, MetallurgicCatalystBlockEntity pEntity) {
        if(level.isClientSide) {
            return;
        }

        if(level.getGameTime() % 20L == 0L) {
            if(pEntity.fuel == 0) {
                SimpleContainer inventory = new SimpleContainer(1);
                inventory.setItem(0, pEntity.itemHandler.getStackInSlot(0));
                Optional<MetallurgicFuelRecipe> recipe = level.getRecipeManager().getRecipeFor(MetallurgicFuelRecipe.Type.INSTANCE, inventory, level);
                if(recipe.isPresent()) {
                    pEntity.currentFuel = pEntity.itemHandler.getStackInSlot(0).getItem();
                    pEntity.itemHandler.extractItem(0, 1, false);
                    pEntity.fuel = recipe.get().getFuel();
                }
            } else {
                SimpleContainer recipeInventory = new SimpleContainer(2);
                recipeInventory.setItem(0, pEntity.currentFuel.getDefaultInstance());
                for (BlockPos pos : BlockPos.betweenClosed(blockPos.above().north().east(), blockPos.below().south().west())) {
                    recipeInventory.setItem(1, level.getBlockState(pos).getBlock().asItem().getDefaultInstance());
                    Optional<MetallurgicCatalystRecipe> recipe = level.getRecipeManager().getRecipeFor(MetallurgicCatalystRecipe.Type.INSTANCE, recipeInventory, level);
                    if(recipe.isPresent()) {
                        if(recipe.get().getResultItem().getItem() instanceof BlockItem blockItem) {
                            level.destroyBlock(pos, false);
                            level.setBlock(pos, blockItem.getBlock().defaultBlockState(), 2);
                            pEntity.fuel--;
                            break;
                        }
                    }
                    /*if(level.getBlockState(pos).getBlock().asItem() == pEntity.currentInputBlock) {
                        if(pEntity.currentOutputBlock instanceof BlockItem blockItem) {
                            level.destroyBlock(pos, false);
                            level.setBlock(pos, blockItem.getBlock().defaultBlockState(), 2);
                            pEntity.fuel--;
                            break;
                        }
                    }*/
                }
            }
        }
    }
}
