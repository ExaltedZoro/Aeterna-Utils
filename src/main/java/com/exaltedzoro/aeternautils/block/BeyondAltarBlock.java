package com.exaltedzoro.aeternautils.block;

import com.exaltedzoro.aeternautils.block.entity.ModBlockEntities;
import com.exaltedzoro.aeternautils.block.entity.BeyondAltarBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class BeyondAltarBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public BeyondAltarBlock(Properties properties) {
        super(properties);
    }

    private static final VoxelShape SHAPE =
            Block.box(0, 0, 0, 16, 8, 16);

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pGetter, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new BeyondAltarBlockEntity(pPos, pState);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if(pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if(blockEntity instanceof BeyondAltarBlockEntity) {
                ((BeyondAltarBlockEntity) blockEntity).drops();
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pType) {
        return createTickerHelper(pType, ModBlockEntities.BEYOND_ALTAR.get(), BeyondAltarBlockEntity::tick);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if(pHand != InteractionHand.MAIN_HAND) {
            return InteractionResult.PASS;
        }

        if(!pLevel.isClientSide() && pLevel.getBlockEntity(pPos) instanceof BeyondAltarBlockEntity entity) {
            Inventory inventory = pPlayer.getInventory();
            if(!entity.getStack(1).isEmpty()) {
                if(pPlayer.getItemInHand(pHand).isEmpty()) {
                    pPlayer.setItemInHand(pHand, entity.getStack(1));
                } else {
                    pPlayer.getInventory().add(entity.getStack(1));
                }
                entity.setStack(1, ItemStack.EMPTY);
            } else if(!pPlayer.getItemInHand(pHand).isEmpty() && entity.getStack(0).isEmpty()) {
                entity.setStack(0, inventory.removeItem(inventory.selected, inventory.getSelected().getCount()));
                pPlayer.swing(pHand);
            } else {
                if(pPlayer.getItemInHand(pHand).getItem() == entity.getStack(0).getItem()) {
                    pPlayer.setItemInHand(pHand, new ItemStack(entity.getStack(0).getItem(), entity.getStack(0).getCount() + pPlayer.getItemInHand(pHand).getCount()));
                } else if(pPlayer.getItemInHand(pHand).isEmpty()) {
                    pPlayer.setItemInHand(pHand, entity.getStack(0));
                } else {
                    inventory.add(entity.getStack(0));
                }
                entity.setStack(0, ItemStack.EMPTY);
                pPlayer.swing(pHand);
            }
        }
        return InteractionResult.SUCCESS;
    }
}
