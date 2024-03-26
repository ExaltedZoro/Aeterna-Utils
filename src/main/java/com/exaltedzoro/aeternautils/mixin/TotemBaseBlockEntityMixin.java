package com.exaltedzoro.aeternautils.mixin;

import com.sammy.malum.common.block.curiosities.totem.TotemBaseBlockEntity;
import com.sammy.malum.common.block.curiosities.totem.TotemPoleBlockEntity;
import com.sammy.malum.core.helper.SpiritHelper;
import com.sammy.malum.core.systems.rites.MalumRiteType;
import com.sammy.malum.registry.common.SpiritRiteRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import team.lodestar.lodestone.helpers.block.BlockStateHelper;
import team.lodestar.lodestone.systems.blockentity.LodestoneBlockEntity;

@Mixin(value = TotemBaseBlockEntity.class, remap = false)
public class TotemBaseBlockEntityMixin extends LodestoneBlockEntity {
    public TotemBaseBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    /**
     * @author ExaltedZoro
     * @reason Testing purposes
     */
    @Overwrite
    public void tick() {
        TotemBaseBlockEntity entity = (TotemBaseBlockEntity) (Object) this;
        super.tick();
        if (!this.level.isClientSide) {
            BlockPos polePos;
            TotemPoleBlockEntity pole;
            BlockEntity var3;
            if (entity.rite != null) {
                ++entity.progress;
                Player nearestPlayer = level.getNearestPlayer(this.worldPosition.getX(), this.worldPosition.getY(), this.worldPosition.getZ(), 64, false);
                if (entity.progress >= entity.rite.getRiteTickRate(entity.corrupted)) {
                    if (entity.direction == null) {
                        polePos = this.worldPosition.above(entity.height);
                        var3 = this.level.getBlockEntity(polePos);
                        if (var3 instanceof TotemPoleBlockEntity) {
                            pole = (TotemPoleBlockEntity)var3;
                            entity.direction = pole.direction;
                        }
                    }

                    entity.rite.executeRite(entity);
                    entity.progress = 0;
                    BlockStateHelper.updateAndNotifyState(this.level, this.worldPosition);
                }
            } else if (entity.active) {
                --entity.progress;
                if (entity.progress <= 0) {
                    ++entity.height;
                    polePos = this.worldPosition.above(entity.height);
                    var3 = this.level.getBlockEntity(polePos);
                    if (var3 instanceof TotemPoleBlockEntity) {
                        pole = (TotemPoleBlockEntity)var3;
                        entity.addPole(pole);
                    } else {
                        MalumRiteType rite = SpiritRiteRegistry.getRite(entity.spirits);
                        if (rite == null) {
                            entity.endRite();
                        } else {
                            entity.completeRite(rite);
                            this.setChanged();
                        }
                    }

                    entity.progress = 20;
                    BlockStateHelper.updateState(this.level, this.worldPosition);
                }
            }
        }
    }
}
