package com.exaltedzoro.aeternautils.block.entity.renderer;

import com.exaltedzoro.aeternautils.block.entity.SingularityJarBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;

public class SingularityJarBlockEntityRenderer implements BlockEntityRenderer<SingularityJarBlockEntity> {
    public SingularityJarBlockEntityRenderer(BlockEntityRendererProvider.Context context) {

    }
    @Override
    public void render(SingularityJarBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        Level level = Minecraft.getInstance().level;
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        ItemStack itemStack = pBlockEntity.getStack();
        pPoseStack.pushPose();
        pPoseStack.translate(0.5f, 0.3f, 0.5f);
        pPoseStack.scale(1f, 1f, 1f);
        pPoseStack.mulPose(Vector3f.YP.rotationDegrees(level.getGameTime() % 360L + pPartialTick));
        itemRenderer.render(itemStack, ItemTransforms.TransformType.GROUND, true, pPoseStack, pBufferSource, getLightLevel(pBlockEntity.getLevel(),
                pBlockEntity.getBlockPos()), OverlayTexture.NO_OVERLAY, itemRenderer.getModel(itemStack, null, null, 0));
        pPoseStack.popPose();
    }

    private int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }
}
