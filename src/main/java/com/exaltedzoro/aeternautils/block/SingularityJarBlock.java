package com.exaltedzoro.aeternautils.block;

import com.exaltedzoro.aeternautils.block.entity.BeyondAltarBlockEntity;
import com.exaltedzoro.aeternautils.block.entity.ModBlockEntities;
import com.exaltedzoro.aeternautils.block.entity.SingularityJarBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class SingularityJarBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    private static final VoxelShape SHAPE =
            Block.box(3, 0, 3, 13, 16, 13);

    protected SingularityJarBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    public static VoxelShape makeShape() {
        VoxelShape shape = Shapes.empty();
        return shape;
    }

    @Nullable
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
        return new SingularityJarBlockEntity(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pType) {
        return createTickerHelper(pType, ModBlockEntities.SINGULARITY_JAR.get(), SingularityJarBlockEntity::tick);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if(pHand == InteractionHand.OFF_HAND) {
            return InteractionResult.PASS;
        }

        Inventory inventory = pPlayer.getInventory();

        if(!pLevel.isClientSide() && pLevel.getBlockEntity(pPos) instanceof SingularityJarBlockEntity entity) {
            if(entity.ticksSinceRightClick <= 20) {
                int amountInInventory = 0;
                int jarCapacityLeft = entity.getMaxStackSize() - entity.getStack().getCount();
                Item itemInJar = entity.getStack().getItem();
                for(int i = 0; i < inventory.getContainerSize(); i++) {
                    if(jarCapacityLeft == 0) {
                        break;
                    }
                    if(inventory.getItem(i).getItem() == itemInJar && jarCapacityLeft - inventory.getItem(i).getCount() >= 0) {
                        amountInInventory = amountInInventory + inventory.getItem(i).getCount();
                        jarCapacityLeft = jarCapacityLeft - inventory.getItem(i).getCount();
                        inventory.removeItem(i, inventory.getItem(i).getCount());
                    } else if(inventory.getItem(i).getItem() == itemInJar) {
                        amountInInventory = amountInInventory + jarCapacityLeft;
                        inventory.removeItem(i, jarCapacityLeft);
                        jarCapacityLeft = 0;
                    }
                }
                entity.setStack(new ItemStack(itemInJar, entity.getStack().getCount() + amountInInventory));
            }

            if(!pPlayer.getItemInHand(pHand).isEmpty()) {
                if(entity.getStack().isEmpty()) {
                    entity.setStack(new ItemStack(pPlayer.getItemInHand(pHand).getItem(), pPlayer.getItemInHand(pHand).getCount()));
                    pPlayer.setItemInHand(pHand, ItemStack.EMPTY);
                } else if (entity.getStack().getItem() == pPlayer.getItemInHand(pHand).getItem()) {
                    if(entity.getStack().getCount() + pPlayer.getItemInHand(pHand).getCount() <= entity.getMaxStackSize()) {
                        entity.setStack(new ItemStack(entity.getStack().getItem(), entity.getStack().getCount() + pPlayer.getItemInHand(pHand).getCount()));
                        pPlayer.setItemInHand(pHand, ItemStack.EMPTY);
                    } else {
                        int inputAmount = entity.getMaxStackSize() - entity.getStack().getCount();
                        entity.setStack(new ItemStack(entity.getStack().getItem(), entity.getMaxStackSize()));
                        pPlayer.setItemInHand(pHand, new ItemStack(pPlayer.getItemInHand(pHand).getItem(), pPlayer.getItemInHand(pHand).getCount() - inputAmount));
                    }
                }
            }
            entity.ticksSinceRightClick = 0;
            pPlayer.swing(pHand);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void attack(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer) {
        if(pLevel.getBlockEntity(pPos) instanceof SingularityJarBlockEntity entity && !pLevel.isClientSide) {
            ItemStack stack = entity.getStack();
            if(!stack.isEmpty()) {
                if(!pPlayer.isCrouching()) {
                    if(pPlayer.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
                        pPlayer.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(stack.getItem(), 1));
                    } else {
                        pPlayer.getInventory().add(new ItemStack(stack.getItem(), 1));
                    }
                    entity.setStack(new ItemStack(stack.getItem(), stack.getCount() - 1));
                } else {
                    if(entity.getStack().getCount() <= 64) {
                        if(pPlayer.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
                            pPlayer.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(stack.getItem(), stack.getCount()));
                        } else {
                            pPlayer.getInventory().add(new ItemStack(stack.getItem(), stack.getCount()));
                        }
                        entity.setStack(ItemStack.EMPTY);
                    } else {
                        if(pPlayer.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
                            pPlayer.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(stack.getItem(), 64));
                        } else {
                            pPlayer.getInventory().add(new ItemStack(stack.getItem(), 64));
                        }
                        entity.setStack(new ItemStack(stack.getItem(), stack.getCount() - 64));
                    }
                }
            }
        }
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if(pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if(blockEntity instanceof SingularityJarBlockEntity) {
                ((SingularityJarBlockEntity) blockEntity).drops();
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }
}
