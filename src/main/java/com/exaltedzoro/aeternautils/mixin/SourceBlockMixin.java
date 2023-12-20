package com.exaltedzoro.aeternautils.mixin;

import com.exaltedzoro.aeternautils.item.ModItems;
import com.hollingsworth.arsnouveau.api.source.AbstractSourceMachine;
import com.hollingsworth.arsnouveau.common.block.SourceBlock;
import com.hollingsworth.arsnouveau.setup.ItemsRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(SourceBlock.class)
public class SourceBlockMixin {
    /**
     * @author ExaltedZoro
     * @reason This adds the ability for my Source Bowl item to add Source to and remove Source from Source Jars
     */
    @Overwrite
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide && pHand == InteractionHand.MAIN_HAND) {
            if (pLevel.getBlockEntity(pPos) instanceof AbstractSourceMachine pEntity) {
                if (pPlayer.getItemInHand(pHand).getItem() == ItemsRegistry.BUCKET_OF_SOURCE.get()) {
                    if (pEntity.getMaxSource() - pEntity.getSource() >= 1000) {
                        pEntity.addSource(1000);
                        if (!pPlayer.isCreative()) {
                            if (pPlayer.getItemInHand(pHand).getCount() == 1) {
                                pPlayer.setItemInHand(pHand, new ItemStack(Items.BUCKET));
                            } else {
                                pPlayer.getItemInHand(pHand).shrink(1);
                                if (!pPlayer.addItem(new ItemStack(Items.BUCKET))) {
                                    pPlayer.drop(new ItemStack(Items.BUCKET), false, false);
                                }
                            }
                        }
                        return InteractionResult.SUCCESS;
                    }
                    return InteractionResult.SUCCESS;
                }

                if(pPlayer.getItemInHand(pHand).getItem() == ModItems.SOURCE_BOWL.get()) {
                    if (pEntity.getMaxSource() - pEntity.getSource() >= 250) {
                        pEntity.addSource(250);
                        if (!pPlayer.isCreative()) {
                            if (pPlayer.getItemInHand(pHand).getCount() == 1) {
                                pPlayer.setItemInHand(pHand, new ItemStack(Items.BOWL));
                            } else {
                                pPlayer.getItemInHand(pHand).shrink(1);
                                if (!pPlayer.addItem(new ItemStack(Items.BOWL))) {
                                    pPlayer.drop(new ItemStack(Items.BOWL), false, false);
                                }
                            }
                        }
                        return InteractionResult.SUCCESS;
                    }
                    return InteractionResult.SUCCESS;
                }

                if (pPlayer.getItemInHand(pHand).getItem() instanceof BucketItem && ((BucketItem)pPlayer.getItemInHand(pHand).getItem()).getFluid() == Fluids.EMPTY) {
                    if (pEntity.getSource() >= 1000) {
                        if (pPlayer.getItemInHand(pHand).getCount() == 1) {
                            pPlayer.setItemInHand(pHand, new ItemStack(ItemsRegistry.BUCKET_OF_SOURCE.get()));
                            pEntity.removeSource(1000);
                            return InteractionResult.SUCCESS;
                        }

                        if (pPlayer.addItem(new ItemStack(ItemsRegistry.BUCKET_OF_SOURCE.get()))) {
                            pPlayer.getItemInHand(pHand).shrink(1);
                            pEntity.removeSource(1000);
                            return InteractionResult.SUCCESS;
                        }
                    } else if (pEntity.getSource() >= 1000 && pPlayer.getItemInHand(pHand).getCount() == 1) {
                        pEntity.removeSource(1000);
                        pPlayer.setItemInHand(pPlayer.getUsedItemHand(), new ItemStack(ItemsRegistry.BUCKET_OF_SOURCE.get()));
                        return InteractionResult.SUCCESS;
                    }
                }

                if(pPlayer.getItemInHand(pHand).getItem() == Items.BOWL) {
                    if(pEntity.getSource() >= 250) {
                        if(pPlayer.getItemInHand(pHand).getCount() == 1) {
                            pPlayer.setItemInHand(pHand, new ItemStack(ModItems.SOURCE_BOWL.get()));
                            pEntity.removeSource(250);
                            return InteractionResult.SUCCESS;
                        }

                        if(pPlayer.addItem(new ItemStack(ModItems.SOURCE_BOWL.get()))) {
                            pPlayer.getItemInHand(pHand).shrink(1);
                            pEntity.removeSource(250);
                            return InteractionResult.SUCCESS;
                        }
                    }
                }
            }
        }
        return InteractionResult.SUCCESS;
    }
}
