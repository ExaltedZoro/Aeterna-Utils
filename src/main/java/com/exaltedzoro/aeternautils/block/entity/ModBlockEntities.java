package com.exaltedzoro.aeternautils.block.entity;

import com.exaltedzoro.aeternautils.AeternaUtils;
import com.exaltedzoro.aeternautils.block.LunarSourcelinkBlock;
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

    public static final RegistryObject<BlockEntityType<SingularityJarBlockEntity>> SINGULARITY_JAR =
            BLOCK_ENTITIES.register("singularity_jar", () ->
                    BlockEntityType.Builder.of(SingularityJarBlockEntity::new, ModBlocks.SINGULARITY_JAR.get()).build(null));

    public static final RegistryObject<BlockEntityType<SolarSourcelinkBlockEntity>> SOLAR_SOURCELINK =
            BLOCK_ENTITIES.register("solar_sourcelink", () ->
                    BlockEntityType.Builder.of(SolarSourcelinkBlockEntity::new, ModBlocks.SOLAR_SOURCELINK.get()).build(null));

    public static final RegistryObject<BlockEntityType<LunarSourcelinkBlockEntity>> LUNAR_SOURCELINK =
            BLOCK_ENTITIES.register("lunar_sourcelink", () ->
                    BlockEntityType.Builder.of(LunarSourcelinkBlockEntity::new, ModBlocks.LUNAR_SOURCELINK.get()).build(null));

    public static final RegistryObject<BlockEntityType<SculkingSourcelinkBlockEntity>> SCULKING_SOURCELINK =
            BLOCK_ENTITIES.register("sculking_sourcelink", () ->
                    BlockEntityType.Builder.of(SculkingSourcelinkBlockEntity::new, ModBlocks.SCULKING_SOURCELINK.get()).build(null));

    public static final RegistryObject<BlockEntityType<GatewayAltarBlockEntity>> GATEWAY_ALTAR =
            BLOCK_ENTITIES.register("gateway_altar", () ->
                    BlockEntityType.Builder.of(GatewayAltarBlockEntity::new, ModBlocks.GATEWAY_ALTAR.get()).build(null));

    public static final RegistryObject<BlockEntityType<MetallurgicCatalystBlockEntity>> METALLURGIC_CATALYST =
            BLOCK_ENTITIES.register("metallurgic_catalyst", () ->
                    BlockEntityType.Builder.of(MetallurgicCatalystBlockEntity::new, ModBlocks.METALLURGIC_CATALYST.get()).build(null));

    public static void register (IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
