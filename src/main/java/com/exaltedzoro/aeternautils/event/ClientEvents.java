package com.exaltedzoro.aeternautils.event;

import com.exaltedzoro.aeternautils.AeternaUtils;
import com.exaltedzoro.aeternautils.block.entity.ModBlockEntities;
import com.exaltedzoro.aeternautils.block.entity.renderer.BeyondAltarBlockEntityRenderer;
import com.exaltedzoro.aeternautils.block.entity.renderer.SingularityJarBlockEntityRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class ClientEvents {
    @Mod.EventBusSubscriber(modid = AeternaUtils.MOD_ID, value = Dist.CLIENT)
    public static class ClientForgeEvents {

    }

    @Mod.EventBusSubscriber(modid = AeternaUtils.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {
        @SubscribeEvent
        public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(ModBlockEntities.BEYOND_ALTAR.get(), BeyondAltarBlockEntityRenderer::new);
            event.registerBlockEntityRenderer(ModBlockEntities.SINGULARITY_JAR.get(), SingularityJarBlockEntityRenderer::new);
        }
    }
}
