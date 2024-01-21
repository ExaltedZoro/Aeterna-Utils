package com.exaltedzoro.aeternautils.event;

import com.exaltedzoro.aeternautils.AeternaUtils;
import com.exaltedzoro.aeternautils.block.entity.SculkingSourcelinkBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class ModEvents {
    @Mod.EventBusSubscriber(modid = AeternaUtils.MOD_ID)
    public static class ForgeEvents {
        @SubscribeEvent
        public static void onBreakBreak(BlockEvent.BreakEvent event) {

        }

        @SubscribeEvent
        public static void onLivingEntityDeath(LivingDeathEvent event) {
            int experience = event.getEntity().getExperienceReward();
            Level level = event.getEntity().getLevel();
            BlockPos pos = event.getEntity().blockPosition();
            for(BlockPos pPos : BlockPos.betweenClosed(pos.above(5).north(5).east(5), pos.below(5).south(5).west(5))) {
                BlockEntity entity = level.getBlockEntity(pPos);
                if(entity instanceof SculkingSourcelinkBlockEntity sourceLink) {
                    sourceLink.activate(experience);
                    event.getEntity().skipDropExperience();
                    break;
                }
            }
        }
    }
}
