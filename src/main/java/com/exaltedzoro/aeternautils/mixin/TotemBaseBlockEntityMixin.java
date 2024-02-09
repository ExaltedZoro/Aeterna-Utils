package com.exaltedzoro.aeternautils.mixin;

import com.sammy.malum.common.block.curiosities.totem.TotemBaseBlockEntity;
import com.sammy.malum.core.helper.SpiritHelper;
import com.sammy.malum.registry.common.SpiritRiteRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import team.lodestar.lodestone.systems.blockentity.LodestoneBlockEntity;

@Mixin(TotemBaseBlockEntity.class)
public class TotemBaseBlockEntityMixin extends LodestoneBlockEntity {
    public TotemBaseBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    /**
     * @author ExaltedZoro
     * @reason Rite effects don't persist between world loads
     */
    @Overwrite
    public void load(CompoundTag compound) {
        TotemBaseBlockEntity entity = (TotemBaseBlockEntity) (Object) this;
        entity.rite = SpiritRiteRegistry.getRite(compound.getString("rite"));
        int size = compound.getInt("spiritCount");
        entity.spirits.clear();

        int i;
        for(i = 0; i < size; ++i) {
            entity.spirits.add(SpiritHelper.getSpiritType(compound.getString("spirit_" + i)));
        }

        entity.active = compound.getBoolean("active");
        entity.height = compound.getInt("height");
        entity.poles.clear();
        entity.filters.clear();
        entity.cachedTotemPoleInstances.clear();

        for(i = 1; i <= entity.height; ++i) {
            entity.poles.add(new BlockPos(entity.getBlockPos().getX(), entity.getBlockPos().getY() + i, entity.getBlockPos().getZ()));
        }

        entity.direction = Direction.byName(compound.getString("direction"));
        entity.corrupted = compound.getBoolean("corrupted");
        entity.progress = compound.getInt("progress");
        super.load(compound);
    }
}
