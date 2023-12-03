package com.exaltedzoro.aeternautils.handler;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class SingularityJarItemStackHandler extends ItemStackHandler {
    protected NonNullList<ItemStack> stacks;

    public SingularityJarItemStackHandler() {
        this(1);
    }

    public SingularityJarItemStackHandler(int size) {
        super(size);
    }

    public SingularityJarItemStackHandler(NonNullList<ItemStack> stacks) {
        super(stacks);
    }

    @Override
    public int getSlotLimit(int slot) {
        return 256;
    }
}
