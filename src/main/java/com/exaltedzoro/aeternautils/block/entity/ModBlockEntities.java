package com.exaltedzoro.aeternautils.block.entity;

import com.exaltedzoro.aeternautils.AeternaUtils;
import com.exaltedzoro.aeternautils.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, AeternaUtils.MOD_ID);

    public static final RegistryObject<BlockEntityType<BeyondAltarBlockEntity>> BEYOND_ALTAR =
            BLOCK_ENTITIES.register("beyond_altar", () ->
                    BlockEntityType.Builder.of(BeyondAltarBlockEntity::new, ModBlocks.BEYOND_ALTAR.get()).build(null));

    public static void register (IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
