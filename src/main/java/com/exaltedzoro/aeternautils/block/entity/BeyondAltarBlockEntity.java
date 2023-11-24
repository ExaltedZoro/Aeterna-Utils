package com.exaltedzoro.aeternautils.block.entity;

import com.exaltedzoro.aeternautils.networking.ModMessages;
import com.exaltedzoro.aeternautils.networking.packet.SyncItemStackToClientPacket;
import com.sammy.malum.common.block.storage.ItemPedestalBlockEntity;
import com.sammy.malum.registry.common.SoundRegistry;
import com.sammy.malum.registry.common.block.BlockRegistry;
import com.exaltedzoro.aeternautils.recipe.BeyondAltarRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.item.ItemEntity;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BeyondAltarBlockEntity extends BlockEntity {
    private final List<BlockPos> pedestalOffsets = new ArrayList<>();
    private final ItemStackHandler itemHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if(!level.isClientSide) {
                ModMessages.sendToClients(new SyncItemStackToClientPacket(this, worldPosition));
            }
        }
    };

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    public BeyondAltarBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.BEYOND_ALTAR.get(), pPos, pBlockState);

        this.pedestalOffsets.add(pPos.offset(2, 0, 0));
        this.pedestalOffsets.add(pPos.offset(2, 0, 2));
        this.pedestalOffsets.add(pPos.offset(0, 0, 2));
        this.pedestalOffsets.add(pPos.offset(-2, 0, 2));
        this.pedestalOffsets.add(pPos.offset(-2, 0, 0));
        this.pedestalOffsets.add(pPos.offset(-2, 0, -2));
        this.pedestalOffsets.add(pPos.offset(0, 0, -2));
        this.pedestalOffsets.add(pPos.offset(2, 0, -2));
    }

    public ItemStack getStack() {
        return itemHandler.getStackInSlot(0);
    }

    public void setStack(ItemStack pStack) {
        itemHandler.setStackInSlot(0, pStack);
    }

    public boolean validateMultiblock(Level level, BlockPos altarPos) {
        BlockPos pos = altarPos.below();
        return level.getBlockState(pos).getBlock() == com.hollingsworth.arsnouveau.setup.BlockRegistry.ARCANE_CORE_BLOCK &&
                level.getBlockState(pos.north(1).east(0)).getBlock() == BlockRegistry.RUNEWOOD_PLANKS_STAIRS.get() &&
                level.getBlockState(pos.north(1).east(1)).getBlock() == BlockRegistry.RUNEWOOD_PLANKS_STAIRS.get() &&
                level.getBlockState(pos.north(0).east(1)).getBlock() == BlockRegistry.RUNEWOOD_PLANKS_STAIRS.get() &&
                level.getBlockState(pos.north(-1).east(1)).getBlock() == BlockRegistry.RUNEWOOD_PLANKS_STAIRS.get() &&
                level.getBlockState(pos.north(-1).east(0)).getBlock() == BlockRegistry.RUNEWOOD_PLANKS_STAIRS.get() &&
                level.getBlockState(pos.north(-1).east(-1)).getBlock() == BlockRegistry.RUNEWOOD_PLANKS_STAIRS.get() &&
                level.getBlockState(pos.north(0).east(-1)).getBlock() == BlockRegistry.RUNEWOOD_PLANKS_STAIRS.get() &&
                level.getBlockState(pos.north(1).east(-1)).getBlock() == BlockRegistry.RUNEWOOD_PLANKS_STAIRS.get() &&
                level.getBlockState(pos.north(2).east(-2)).getBlock() == BlockRegistry.RUNEWOOD_PLANKS.get() &&
                level.getBlockState(pos.north(2).east(-1)).getBlock() == BlockRegistry.RUNEWOOD_PLANKS.get() &&
                level.getBlockState(pos.north(2).east(0)).getBlock() == BlockRegistry.RUNEWOOD_PLANKS.get() &&
                level.getBlockState(pos.north(2).east(1)).getBlock() == BlockRegistry.RUNEWOOD_PLANKS.get() &&
                level.getBlockState(pos.north(2).east(2)).getBlock() == BlockRegistry.RUNEWOOD_PLANKS.get() &&
                level.getBlockState(pos.north(1).east(2)).getBlock() == BlockRegistry.RUNEWOOD_PLANKS.get() &&
                level.getBlockState(pos.north(0).east(2)).getBlock() == BlockRegistry.RUNEWOOD_PLANKS.get() &&
                level.getBlockState(pos.north(-1).east(2)).getBlock() == BlockRegistry.RUNEWOOD_PLANKS.get() &&
                level.getBlockState(pos.north(-2).east(2)).getBlock() == BlockRegistry.RUNEWOOD_PLANKS.get() &&
                level.getBlockState(pos.north(-2).east(1)).getBlock() == BlockRegistry.RUNEWOOD_PLANKS.get() &&
                level.getBlockState(pos.north(-2).east(0)).getBlock() == BlockRegistry.RUNEWOOD_PLANKS.get() &&
                level.getBlockState(pos.north(-2).east(-1)).getBlock() == BlockRegistry.RUNEWOOD_PLANKS.get() &&
                level.getBlockState(pos.north(-2).east(-2)).getBlock() == BlockRegistry.RUNEWOOD_PLANKS.get() &&
                level.getBlockState(pos.north(-1).east(-2)).getBlock() == BlockRegistry.RUNEWOOD_PLANKS.get() &&
                level.getBlockState(pos.north(0).east(-2)).getBlock() == BlockRegistry.RUNEWOOD_PLANKS.get() &&
                level.getBlockState(pos.north(1).east(-2)).getBlock() == BlockRegistry.RUNEWOOD_PLANKS.get() &&
                level.getBlockState(altarPos.north(2).east(-2)).getBlock() == BlockRegistry.RUNEWOOD_ITEM_PEDESTAL.get() &&
                level.getBlockState(altarPos.north(2).east(0)).getBlock() == BlockRegistry.RUNEWOOD_ITEM_PEDESTAL.get() &&
                level.getBlockState(altarPos.north(2).east(2)).getBlock() == BlockRegistry.RUNEWOOD_ITEM_PEDESTAL.get() &&
                level.getBlockState(altarPos.north(0).east(2)).getBlock() == BlockRegistry.RUNEWOOD_ITEM_PEDESTAL.get() &&
                level.getBlockState(altarPos.north(-2).east(2)).getBlock() == BlockRegistry.RUNEWOOD_ITEM_PEDESTAL.get() &&
                level.getBlockState(altarPos.north(-2).east(0)).getBlock() == BlockRegistry.RUNEWOOD_ITEM_PEDESTAL.get() &&
                level.getBlockState(altarPos.north(-2).east(-2)).getBlock() == BlockRegistry.RUNEWOOD_ITEM_PEDESTAL.get() &&
                level.getBlockState(altarPos.north(0).east(-2)).getBlock() == BlockRegistry.RUNEWOOD_ITEM_PEDESTAL.get();
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

        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemHandler.deserializeNBT(nbt.getCompound("inventory"));
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for(int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public static void tick(Level level, BlockPos blockpos, BlockState blockState, BeyondAltarBlockEntity pEntity) {
        if(level.isClientSide) {
            return;
        }

        if(level.getGameTime() % 20L == 0L) {
            if(pEntity.validateMultiblock(level, blockpos)) {
                SimpleContainer inventory = new SimpleContainer(1);
                inventory.setItem(0, pEntity.itemHandler.getStackInSlot(0));
                Optional<BeyondAltarRecipe> recipe = level.getRecipeManager().getRecipeFor(BeyondAltarRecipe.Type.INSTANCE, inventory, level);
                if(recipe.isPresent()) {
                    if(canOutput(recipe.get(), pEntity) && recipe.get().doPedestalsMatch(getPedestalItems(level, blockpos, pEntity))) {
                        pEntity.itemHandler.extractItem(0, 1, false);
                        level.addFreshEntity(new ItemEntity(level, blockpos.getX() + 0.5f, blockpos.getY() + 1, blockpos.getZ() + 0.5f, recipe.get().getResultItem()));
                        for(BlockPos pos : pEntity.pedestalOffsets) {
                            ((ItemPedestalBlockEntity) level.getBlockEntity(pos)).inventory.extractItem(0, 1, false);
                        }
                        level.playSound(null, blockpos, SoundRegistry.ALTAR_CRAFT.get(), SoundSource.BLOCKS, 1, 1);
                        setChanged(level, blockpos, blockState);
                    }
                }
            }
        }
    }

    private static boolean canOutput(BeyondAltarRecipe recipe, BeyondAltarBlockEntity entity) {
        if(entity.itemHandler.getStackInSlot(1).isEmpty()) {
            return true;
        } else return recipe.getResultItem().getItem() == entity.itemHandler.getStackInSlot(1).getItem() && entity.itemHandler.getStackInSlot(1).getCount() + recipe.getResultItem().getCount() <= 64;
    }

    private static List<ItemStack> getPedestalItems(Level level, BlockPos pos, BeyondAltarBlockEntity entity) {
        NonNullList<ItemStack> stacks = NonNullList.withSize(8, ItemStack.EMPTY);
        for(int i = 0; i < entity.pedestalOffsets.size(); i++) {
            if(!((ItemPedestalBlockEntity) level.getBlockEntity(entity.pedestalOffsets.get(i))).inventory.getStackInSlot(0).isEmpty()) {
                stacks.set(i, ((ItemPedestalBlockEntity) level.getBlockEntity(entity.pedestalOffsets.get(i))).inventory.getStackInSlot(0));
            }
        }
        return stacks;
    }

    public void setHandler(ItemStackHandler itemStackHandler) {
        for(int i = 0; i < itemHandler.getSlots(); i++) {
            itemHandler.setStackInSlot(i, itemStackHandler.getStackInSlot(i));
        }
    }
}
