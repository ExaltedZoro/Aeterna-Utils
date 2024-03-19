package com.exaltedzoro.aeternautils.block.entity;

import com.exaltedzoro.aeternautils.networking.ModMessages;
import com.exaltedzoro.aeternautils.networking.packet.SyncItemStackToClientPacket;
import com.sammy.malum.registry.common.block.BlockRegistry;
import com.stal111.forbidden_arcanus.core.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import shadows.gateways.GatewayObjects;
import shadows.gateways.entity.GatewayEntity;
import shadows.gateways.gate.Gateway;
import shadows.gateways.gate.GatewayManager;

import java.util.List;

public class GatewayAltarBlockEntity extends BlockEntity {
    private final ItemStackHandler itemHandler = new ItemStackHandler() {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if(!level.isClientSide) {
                ModMessages.sendToClients(new SyncItemStackToClientPacket(this, worldPosition));
            }
        }
    };
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    public GatewayAltarBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.GATEWAY_ALTAR.get(), pPos, pBlockState);
    }

    public ItemStack getStack() {
        return itemHandler.getStackInSlot(0);
    }

    public void setStack(ItemStack pStack) {
        itemHandler.setStackInSlot(0, pStack);
    }

    public boolean validateMultiblock() {
        assert level != null;

        return level.getBlockState(worldPosition.below()).getBlock() == ModBlocks.ARCANE_POLISHED_DARKSTONE_PILLAR.get() &&
                level.getBlockState(worldPosition.below(2)).getBlock() == BlockRegistry.BLOCK_OF_CTHONIC_GOLD.get() &&
                level.getBlockState(worldPosition.below(3)).getBlock() == ModBlocks.ARCANE_POLISHED_DARKSTONE_PILLAR.get() &&
                level.getBlockState(worldPosition.below().north()).getBlock() == ModBlocks.ARCANE_CRYSTAL_OBELISK.get() &&
                level.getBlockState(worldPosition.below().south()).getBlock() == ModBlocks.ARCANE_CRYSTAL_OBELISK.get() &&
                level.getBlockState(worldPosition.below().east()).getBlock() == ModBlocks.ARCANE_CRYSTAL_OBELISK.get() &&
                level.getBlockState(worldPosition.below().west()).getBlock() == ModBlocks.ARCANE_CRYSTAL_OBELISK.get() &&
                level.getBlockState(worldPosition.below(2).north()).getBlock() == ModBlocks.ARCANE_CRYSTAL_OBELISK.get() &&
                level.getBlockState(worldPosition.below(2).south()).getBlock() == ModBlocks.ARCANE_CRYSTAL_OBELISK.get() &&
                level.getBlockState(worldPosition.below(2).east()).getBlock() == ModBlocks.ARCANE_CRYSTAL_OBELISK.get() &&
                level.getBlockState(worldPosition.below(2).west()).getBlock() == ModBlocks.ARCANE_CRYSTAL_OBELISK.get() &&
                level.getBlockState(worldPosition.below(3).north()).getBlock() == ModBlocks.ARCANE_CRYSTAL_OBELISK.get() &&
                level.getBlockState(worldPosition.below(3).south()).getBlock() == ModBlocks.ARCANE_CRYSTAL_OBELISK.get() &&
                level.getBlockState(worldPosition.below(3).east()).getBlock() == ModBlocks.ARCANE_CRYSTAL_OBELISK.get() &&
                level.getBlockState(worldPosition.below(3).west()).getBlock() == ModBlocks.ARCANE_CRYSTAL_OBELISK.get();
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

        Containers.dropContents(level, worldPosition, inventory);
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, GatewayAltarBlockEntity pEntity) {
        if(level.isClientSide()) {
            return;
        }

        if(level.getGameTime() % 100 == 0) {
            if(pEntity.validateMultiblock() && !pEntity.getStack().isEmpty() && pEntity.getStack().is(GatewayObjects.GATE_PEARL.get()) && pEntity.getStack().hasTag() && pEntity.getStack().getTag().contains("gateway")) {
                String type = pEntity.getStack().getTag().getString("gateway");
                Gateway gate = GatewayManager.INSTANCE.getValue(new ResourceLocation(type));
                List<GatewayEntity> gateways = level.getEntitiesOfClass(GatewayEntity.class, new AABB(blockPos.above()));
                Player player = level.getNearestPlayer(blockPos.getX(), blockPos.getY(), blockPos.getZ(), 64, false);
                if(gateways.isEmpty() && gate != null && player != null) {
                    GatewayEntity entity = new GatewayEntity(level, player, gate);
                    entity.setPos(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5);
                    level.addFreshEntity(entity);
                    entity.onGateCreated();
                    pEntity.itemHandler.extractItem(0, 1, false);
                }
            }
        }
    }
}
