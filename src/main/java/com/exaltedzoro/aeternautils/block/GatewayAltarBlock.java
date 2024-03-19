package com.exaltedzoro.aeternautils.block;

import com.exaltedzoro.aeternautils.block.entity.GatewayAltarBlockEntity;
import com.exaltedzoro.aeternautils.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class GatewayAltarBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    private static final VoxelShape SHAPE = makeShape();

    public GatewayAltarBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pGetter, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    private static VoxelShape makeShape() {
        VoxelShape shape = Shapes.empty();

        //Base
        shape = Shapes.join(shape, box(2, 0, 2, 14, 2, 14), BooleanOp.OR);

        //Sides
        shape = Shapes.join(shape, box(2, 2, 2, 14, 4, 3), BooleanOp.OR);
        shape = Shapes.join(shape, box(2, 2, 13, 14, 4, 14), BooleanOp.OR);
        shape = Shapes.join(shape, box(13, 2, 2, 14, 4, 14), BooleanOp.OR);
        shape = Shapes.join(shape, box(2, 2, 2, 3, 4, 14), BooleanOp.OR);

        return shape;
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
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
        return new GatewayAltarBlockEntity(pPos, pState);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if(pHand != InteractionHand.MAIN_HAND) {
            return InteractionResult.PASS;
        }

        if(!pLevel.isClientSide() && pLevel.getBlockEntity(pPos) instanceof GatewayAltarBlockEntity entity) {
            Inventory inventory = pPlayer.getInventory();
            if(!pPlayer.getItemInHand(pHand).isEmpty() && entity.getStack().isEmpty()) {
                entity.setStack(inventory.removeItem(inventory.selected, inventory.getSelected().getCount()));
                pPlayer.swing(pHand);
            } else {
                if(pPlayer.getItemInHand(pHand).getItem() == entity.getStack().getItem() && pPlayer.getItemInHand(pHand).getCount() + entity.getStack().getCount() <= 64) {
                    pPlayer.setItemInHand(pHand, new ItemStack(entity.getStack().getItem(), entity.getStack().getCount() + pPlayer.getItemInHand(pHand).getCount()));
                } else if(pPlayer.getItemInHand(pHand).isEmpty()) {
                    pPlayer.setItemInHand(pHand, entity.getStack());
                } else {
                    inventory.add(entity.getStack());
                }
                entity.setStack(ItemStack.EMPTY);
                pPlayer.swing(pHand);
                pLevel.playSound(null, pPos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.25f, 2);
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pType) {
        return createTickerHelper(pType, ModBlockEntities.GATEWAY_ALTAR.get(), GatewayAltarBlockEntity::tick);
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if(pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if(blockEntity instanceof GatewayAltarBlockEntity) {
                ((GatewayAltarBlockEntity) blockEntity).drops();
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }
}
