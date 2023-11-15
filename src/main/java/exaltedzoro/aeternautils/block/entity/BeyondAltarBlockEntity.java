package exaltedzoro.aeternautils.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
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

public class BeyondAltarBlockEntity extends BlockEntity {
    private final ItemStackHandler itemHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    public BeyondAltarBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.BEYOND_ALTAR.get(), pPos, pBlockState);
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

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, BeyondAltarBlockEntity pEntity) {

    }

    public void use(Player pPlayer, InteractionHand pHand) {
        if(!pPlayer.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
            this.itemHandler.insertItem(0, pPlayer.getItemInHand(InteractionHand.MAIN_HAND), false);
            pPlayer.getItemInHand(pHand).setCount(0);
            pPlayer.swing(InteractionHand.MAIN_HAND);
        } else {
            pPlayer.setItemInHand(pHand, this.itemHandler.getStackInSlot(0));
            this.itemHandler.extractItem(0, this.itemHandler.getStackInSlot(0).getCount(), false);
            pPlayer.swing(InteractionHand.MAIN_HAND);
        }
    }
}
