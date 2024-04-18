package com.exaltedzoro.aeternautils.block;

import com.exaltedzoro.aeternautils.AeternaUtils;
import com.exaltedzoro.aeternautils.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, AeternaUtils.MOD_ID);

    public static final RegistryObject<Block> BEYOND_ALTAR = registerBlock("beyond_altar", () -> new BeyondAltarBlock(BlockBehaviour.Properties.of(Material.STONE).strength(2, 2).requiresCorrectToolForDrops()), CreativeModeTab.TAB_BUILDING_BLOCKS);

    public static final RegistryObject<Block> SINGULARITY_JAR = registerBlock("singularity_jar", () -> new SingularityJarBlock(BlockBehaviour.Properties.of(Material.GLASS).strength(2, 1).sound(SoundType.GLASS)), CreativeModeTab.TAB_BUILDING_BLOCKS);

    public static final RegistryObject<Block> SOLAR_SOURCELINK = registerBlock("solar_sourcelink", () -> new SolarSourcelinkBlock(BlockBehaviour.Properties.of(Material.STONE).strength(2, 1)), CreativeModeTab.TAB_BUILDING_BLOCKS);
    public static final RegistryObject<Block> LUNAR_SOURCELINK = registerBlock("lunar_sourcelink", () -> new LunarSourcelinkBlock(BlockBehaviour.Properties.of(Material.STONE).strength(2, 1)), CreativeModeTab.TAB_BUILDING_BLOCKS);
    public static final RegistryObject<Block> SCULKING_SOURCELINK = registerBlock("sculking_sourcelink", () -> new SculkingSourcelinkBlock(BlockBehaviour.Properties.of(Material.STONE).strength(2, 1)), CreativeModeTab.TAB_BUILDING_BLOCKS);
    public static final RegistryObject<Block> GATEWAY_ALTAR = registerBlock("gateway_altar", () -> new GatewayAltarBlock(BlockBehaviour.Properties.of(Material.STONE).strength(2, 1).requiresCorrectToolForDrops()), CreativeModeTab.TAB_BUILDING_BLOCKS);
    public static final RegistryObject<Block> METALLURGIC_CATALYST = registerBlock("metallurgic_catalyst", () -> new MetallurgicCatalystBlock(BlockBehaviour.Properties.of(Material.STONE).strength(2, 1).requiresCorrectToolForDrops()), CreativeModeTab.TAB_BUILDING_BLOCKS);

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, CreativeModeTab tab) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn, tab);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem (String name, RegistryObject<T> block, CreativeModeTab tab) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
