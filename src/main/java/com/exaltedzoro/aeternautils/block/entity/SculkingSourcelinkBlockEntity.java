package com.exaltedzoro.aeternautils.block.entity;

import com.exaltedzoro.aeternautils.AeternaUtils;
import com.hollingsworth.arsnouveau.api.source.ISpecialSourceProvider;
import com.hollingsworth.arsnouveau.api.util.SourceUtil;
import com.hollingsworth.arsnouveau.client.particle.ParticleUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = AeternaUtils.MOD_ID)
public class SculkingSourcelinkBlockEntity extends BlockEntity {
    private int source = 0;
    private final int max_source = 5000;

    public SculkingSourcelinkBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.SCULKING_SOURCELINK.get(), pPos, pBlockState);
    }

    public void activate(int experience) {
        level.playSound(null, worldPosition, SoundEvents.SCULK_BLOCK_PLACE, SoundSource.BLOCKS, 1, 1);
        addSource(experience * 10);
    }

    public void addSource(int source) {
        if(this.source + source <= max_source) {
            this.source += source;
        } else {
            this.source = max_source;
        }
    }

    public static void tick(Level level, BlockPos pos, BlockState state, SculkingSourcelinkBlockEntity entity) {
        if(level.isClientSide) return;

        if(entity.source > 0) {
            List<ISpecialSourceProvider> nearbyJars = SourceUtil.canGiveSource(pos, level, 5);
            if(!nearbyJars.isEmpty()) {
                int sourceTransferred = nearbyJars.get(0).getSource().addSource(entity.source);
                ParticleUtil.spawnFollowProjectile(level, pos, nearbyJars.get(0).getCurrentPos());
                if(entity.source - sourceTransferred < 0) {
                    entity.source = 0;
                } else {
                    entity.source -= sourceTransferred;
                }
            }
        }
    }
}
