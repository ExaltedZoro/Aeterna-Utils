package com.exaltedzoro.aeternautils.handler;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class SingularityJarItemStackHandler extends ItemStackHandler {
    protected NonNullList<ItemStack> stacks;
    public int tier = 1;

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
        return switch (tier) {
            case 2 -> 65536;
            case 3 -> 2097152;
            case 4 -> 67108864;
            case 5 -> 2147483647;
            default -> 2048;
        };
    }
}
