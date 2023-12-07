package com.exaltedzoro.aeternautils.block;

import com.exaltedzoro.aeternautils.block.entity.ModBlockEntities;
import com.exaltedzoro.aeternautils.block.entity.BeyondAltarBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
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

public class BeyondAltarBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public BeyondAltarBlock(Properties properties) {
        super(properties);
    }

    private static final VoxelShape SHAPE = makeShape();

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pGetter, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    public static VoxelShape makeShape() {
        VoxelShape shape = Shapes.empty();
        //Base and pedestal
        shape = Shapes.join(shape, box(1, 0, 1, 15, 1, 15), BooleanOp.OR);
        shape = Shapes.join(shape, box(2, 1, 2, 14, 3, 14), BooleanOp.OR);
        shape = Shapes.join(shape, box(4, 3, 4, 12, 10, 12), BooleanOp.OR);
        //Platform
        shape = Shapes.join(shape, box(2, 10, 2, 14, 13, 14), BooleanOp.OR);
        //Candles
        shape = Shapes.join(shape, box(0, 11, 0, 3, 16, 3), BooleanOp.OR);
        shape = Shapes.join(shape, box(0, 11, 13, 3, 16, 16), BooleanOp.OR);
        shape = Shapes.join(shape, box(13, 11, 0, 16, 16, 3), BooleanOp.OR);
        shape = Shapes.join(shape, box(13, 11, 13, 16, 16, 16), BooleanOp.OR);
        return shape;
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
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
        double blockX = pPos.getX();
        double blockY = pPos.getY();
        double blockZ = pPos.getZ();
        pLevel.addParticle(ParticleTypes.SMALL_FLAME, blockX + (1.5/16), blockY + 1, blockZ + (1.5/16), 0, 0, 0);
        pLevel.addParticle(ParticleTypes.SMOKE, blockX + (1.5/16), blockY + 1, blockZ + (1.5/16), 0, 0, 0);

        pLevel.addParticle(ParticleTypes.SMALL_FLAME, blockX + (14.5/16), blockY + 1, blockZ + (1.5/16), 0, 0, 0);
        pLevel.addParticle(ParticleTypes.SMOKE, blockX + (14.5/16), blockY + 1, blockZ + (1.5/16), 0, 0, 0);

        pLevel.addParticle(ParticleTypes.SMALL_FLAME, blockX + (1.5/16), blockY + 1, blockZ + (14.5/16), 0, 0, 0);
        pLevel.addParticle(ParticleTypes.SMOKE, blockX + (1.5/16), blockY + 1, blockZ + (14.5/16), 0, 0, 0);

        pLevel.addParticle(ParticleTypes.SMALL_FLAME, blockX + (14.5/16), blockY + 1, blockZ + (14.5/16), 0, 0, 0);
        pLevel.addParticle(ParticleTypes.SMOKE, blockX + (14.5/16), blockY + 1, blockZ + (14.5/16), 0, 0, 0);
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
}
