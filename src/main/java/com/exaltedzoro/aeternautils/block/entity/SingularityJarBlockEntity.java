package com.exaltedzoro.aeternautils.block.entity;

import com.exaltedzoro.aeternautils.handler.SingularityJarItemStackHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
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

public class SingularityJarBlockEntity extends BlockEntity {
    private final SingularityJarItemStackHandler itemHandler = new SingularityJarItemStackHandler() {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    public int ticksSinceRightClick = 0;

    public SingularityJarBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.SINGULARITY_JAR.get(), pPos, pBlockState);
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.put("inventory", itemHandler.serializeNBT());

        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemHandler.deserializeNBT(nbt.getCompound("inventory"));
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

    public void setItemHandler(ItemStackHandler itemStackHandler) {
        for(int i = 0; i < itemHandler.getSlots(); i++) {
            itemHandler.setStackInSlot(i, itemStackHandler.getStackInSlot(i));
        }
    }

    public static void tick(Level level, BlockPos blockpos, BlockState blockState, SingularityJarBlockEntity pEntity) {
        if(pEntity.ticksSinceRightClick < 20 && pEntity.ticksSinceRightClick >= 0) {
            pEntity.ticksSinceRightClick++;
        } else {
            pEntity.ticksSinceRightClick = -1;
        }
    }

    public ItemStack getStack() {
        return itemHandler.getStackInSlot(0);
    }

    public void setStack(ItemStack stack) {
        itemHandler.setStackInSlot(0, stack);
    }

    public int getMaxStackSize() {
        return itemHandler.getSlotLimit(0);
    }

    public void setHandlerTier(int tier) {
        itemHandler.tier = tier;
    }

    public SingularityJarItemStackHandler getItemHandler() {
        return itemHandler;
    }
}
