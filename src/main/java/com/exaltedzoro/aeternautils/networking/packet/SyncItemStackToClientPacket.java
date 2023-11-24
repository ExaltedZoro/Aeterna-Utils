package com.exaltedzoro.aeternautils.networking.packet;

import com.exaltedzoro.aeternautils.block.entity.BeyondAltarBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public class SyncItemStackToClientPacket {
    private final ItemStackHandler itemStackHandler;
    private final BlockPos blockPos;

    public SyncItemStackToClientPacket(ItemStackHandler itemStackHandler, BlockPos blockPos) {
        this.itemStackHandler = itemStackHandler;
        this.blockPos = blockPos;
    }

    public SyncItemStackToClientPacket(FriendlyByteBuf buf) {
        List<ItemStack> collection = buf.readCollection(ArrayList::new, FriendlyByteBuf::readItem);
        itemStackHandler = new ItemStackHandler(collection.size());
        for(int i = 0; i < collection.size(); i++) {
            itemStackHandler.insertItem(i, collection.get(i), false);
        }

        this.blockPos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        Collection<ItemStack> list = new ArrayList<>();
        for(int i = 0; i < itemStackHandler.getSlots(); i++) {
            list.add(itemStackHandler.getStackInSlot(i));
        }

        buf.writeCollection(list, FriendlyByteBuf::writeItem);
        buf.writeBlockPos(blockPos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            if(Minecraft.getInstance().level.getBlockEntity(blockPos) instanceof BeyondAltarBlockEntity blockEntity) {
                blockEntity.setHandler(this.itemStackHandler);
            }
        });

        return true;
    }
}
