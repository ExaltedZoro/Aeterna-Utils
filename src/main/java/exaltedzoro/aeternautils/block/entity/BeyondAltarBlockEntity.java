package exaltedzoro.aeternautils.block.entity;

import com.sammy.malum.common.block.storage.ItemPedestalBlockEntity;
import com.sammy.malum.registry.common.block.BlockRegistry;
import exaltedzoro.aeternautils.recipe.BeyondAltarRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
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
    private final ItemStackHandler itemHandler = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
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

    public void setStack(ItemStack pStack, Boolean extracting) {
        if(extracting){
            itemHandler.extractItem(0, itemHandler.getStackInSlot(0).getCount(), false);
        } else {
            itemHandler.insertItem(0, pStack, false);
        }
    }

    public boolean validateMultiblock(Level level, BlockPos pos) {
        return level.getBlockState(pos.below()).getBlock() == com.hollingsworth.arsnouveau.setup.BlockRegistry.ARCANE_CORE_BLOCK &&
                level.getBlockState(pos.offset(1, -1, 0)).getBlock() == BlockRegistry.RUNEWOOD_PLANKS_STAIRS.get() &&
                level.getBlockState(pos.offset(0, -1, 1)).getBlock() == BlockRegistry.RUNEWOOD_PLANKS_STAIRS.get() &&
                level.getBlockState(pos.offset(-1, -1, 0)).getBlock() == BlockRegistry.RUNEWOOD_PLANKS_STAIRS.get() &&
                level.getBlockState(pos.offset(0, -1, -1)).getBlock() == BlockRegistry.RUNEWOOD_PLANKS_STAIRS.get() &&
                level.getBlockState(pos.offset(1, -1, 1)).getBlock() == BlockRegistry.RUNEWOOD_PLANKS_STAIRS.get() &&
                level.getBlockState(pos.offset(-1, -1, 1)).getBlock() == BlockRegistry.RUNEWOOD_PLANKS_STAIRS.get() &&
                level.getBlockState(pos.offset(-1, -1, -1)).getBlock() == BlockRegistry.RUNEWOOD_PLANKS_STAIRS.get() &&
                level.getBlockState(pos.offset(1, -1, -1)).getBlock() == BlockRegistry.RUNEWOOD_PLANKS_STAIRS.get() &&
                level.getBlockState(pos.offset(2, -1, 0)).getBlock() == BlockRegistry.RUNEWOOD_PLANKS.get() &&
                level.getBlockState(pos.offset(0, -1, 2)).getBlock() == BlockRegistry.RUNEWOOD_PLANKS.get() &&
                level.getBlockState(pos.offset(-2, -1, 0)).getBlock() == BlockRegistry.RUNEWOOD_PLANKS.get() &&
                level.getBlockState(pos.offset(0, -1, -2)).getBlock() == BlockRegistry.RUNEWOOD_PLANKS.get() &&
                level.getBlockState(pos.offset(2, -1, 2)).getBlock() == BlockRegistry.RUNEWOOD_PLANKS.get() &&
                level.getBlockState(pos.offset(-2, -1, 2)).getBlock() == BlockRegistry.RUNEWOOD_PLANKS.get() &&
                level.getBlockState(pos.offset(2, -1, -2)).getBlock() == BlockRegistry.RUNEWOOD_PLANKS.get() &&
                level.getBlockState(pos.offset(-2, -1, -2)).getBlock() == BlockRegistry.RUNEWOOD_PLANKS.get() &&
                level.getBlockState(pos.offset(2, -1, 1)).getBlock() == BlockRegistry.RUNEWOOD_PLANKS.get() &&
                level.getBlockState(pos.offset(2, -1, -1)).getBlock() == BlockRegistry.RUNEWOOD_PLANKS.get() &&
                level.getBlockState(pos.offset(1, -1, 2)).getBlock() == BlockRegistry.RUNEWOOD_PLANKS.get() &&
                level.getBlockState(pos.offset(-1, -1, 2)).getBlock() == BlockRegistry.RUNEWOOD_PLANKS.get() &&
                level.getBlockState(pos.offset(-2, -1, 1)).getBlock() == BlockRegistry.RUNEWOOD_PLANKS.get() &&
                level.getBlockState(pos.offset(-2, -1, -1)).getBlock() == BlockRegistry.RUNEWOOD_PLANKS.get() &&
                level.getBlockState(pos.offset(1, -1, -2)).getBlock() == BlockRegistry.RUNEWOOD_PLANKS.get() &&
                level.getBlockState(pos.offset(-1, -1, -2)).getBlock() == BlockRegistry.RUNEWOOD_PLANKS.get() &&
                level.getBlockState(pos.offset(2, -1, 0)).getBlock() == BlockRegistry.RUNEWOOD_ITEM_PEDESTAL.get() &&
                level.getBlockState(pos.offset(0, -1, 2)).getBlock() == BlockRegistry.RUNEWOOD_ITEM_PEDESTAL.get() &&
                level.getBlockState(pos.offset(-2, -1, 0)).getBlock() == BlockRegistry.RUNEWOOD_ITEM_PEDESTAL.get() &&
                level.getBlockState(pos.offset(0, -1, -2)).getBlock() == BlockRegistry.RUNEWOOD_ITEM_PEDESTAL.get() &&
                level.getBlockState(pos.offset(2, -1, 2)).getBlock() == BlockRegistry.RUNEWOOD_ITEM_PEDESTAL.get() &&
                level.getBlockState(pos.offset(-2, -1, 2)).getBlock() == BlockRegistry.RUNEWOOD_ITEM_PEDESTAL.get() &&
                level.getBlockState(pos.offset(2, -1, -2)).getBlock() == BlockRegistry.RUNEWOOD_ITEM_PEDESTAL.get() &&
                level.getBlockState(pos.offset(-2, -1, -2)).getBlock() == BlockRegistry.RUNEWOOD_ITEM_PEDESTAL.get();
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

        SimpleContainer inventory = new SimpleContainer(1);
        inventory.addItem(pEntity.itemHandler.getStackInSlot(0));
        Optional<BeyondAltarRecipe> recipe = level.getRecipeManager().getRecipeFor(BeyondAltarRecipe.Type.INSTANCE, inventory, level);
        if(recipe.isPresent()) {
            List<ItemStack> pedestals = getPedestalItems(level, blockpos,pEntity);
            if(recipe.get().doPedestalsMatch(pedestals) && canOutput(recipe.get(), pEntity)) {
                pEntity.itemHandler.extractItem(0, 1, false);
                pEntity.itemHandler.insertItem(1, new ItemStack(recipe.get().getResultItem().getItem(), pEntity.itemHandler.getStackInSlot(1).getCount() + 1), false);
                for(BlockPos pos : pEntity.pedestalOffsets) {
                    ((ItemPedestalBlockEntity) level.getBlockEntity(pos)).inventory.extractItem(0, 1, false);
                }
                setChanged(level, blockpos, blockState);
            }
        }

    }

    private static boolean hasRecipe(Level level, BlockPos pos, BeyondAltarBlockEntity entity) {
        if(entity.validateMultiblock(level, pos)) {
            SimpleContainer inventory = new SimpleContainer(entity.itemHandler.getSlots());
            for (int i = 0; i < entity.itemHandler.getSlots(); i++) {
                inventory.setItem(i, entity.itemHandler.getStackInSlot(i));
            }
            Optional<BeyondAltarRecipe> recipe = level.getRecipeManager().getRecipeFor(BeyondAltarRecipe.Type.INSTANCE, inventory, level);
            return recipe.isPresent() && canOutput(recipe.get(), entity) && recipe.get().doPedestalsMatch(getPedestalItems(level, pos, entity));
        } else {
            return false;
        }
    }

    private static boolean canOutput(BeyondAltarRecipe recipe, BeyondAltarBlockEntity entity) {
        if(entity.itemHandler.getStackInSlot(1).isEmpty()) {
            return true;
        } else return recipe.getResultItem().getItem() == entity.itemHandler.getStackInSlot(1).getItem() && entity.itemHandler.getStackInSlot(1).getCount() + recipe.getResultItem().getCount() <= 64;
    }

    private static List<ItemStack> getPedestalItems(Level level, BlockPos pos, BeyondAltarBlockEntity entity) {
        List<ItemStack> stacks = new ArrayList<>();
        if(entity.validateMultiblock(level, pos)) {
            for(int i = 0; i < entity.pedestalOffsets.size(); i++) {
                if(!((ItemPedestalBlockEntity) level.getBlockEntity(entity.pedestalOffsets.get(i))).inventory.getStackInSlot(0).isEmpty()) {
                    stacks.add(((ItemPedestalBlockEntity) level.getBlockEntity(entity.pedestalOffsets.get(i))).inventory.getStackInSlot(0));
                }
            }
        }
        return stacks;
    }
}
