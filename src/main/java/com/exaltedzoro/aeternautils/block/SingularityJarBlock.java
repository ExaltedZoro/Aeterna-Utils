package com.exaltedzoro.aeternautils.block;

import com.exaltedzoro.aeternautils.block.entity.ModBlockEntities;
import com.exaltedzoro.aeternautils.block.entity.SingularityJarBlockEntity;
import com.exaltedzoro.aeternautils.handler.SingularityJarItemStackHandler;
import com.klikli_dev.occultism.registry.OccultismBlocks;
import com.stal111.forbidden_arcanus.core.init.ModBlocks;
import me.codexadrian.spirit.registry.SpiritBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SingularityJarBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final IntegerProperty TIER = IntegerProperty.create("tier", 1, 5);
    private static final VoxelShape SHAPE = makeShape();

    public SingularityJarBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.defaultBlockState().setValue(TIER, 1));
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    public static VoxelShape makeShape() {
        VoxelShape shape = Shapes.empty();
        //Body
        shape = Shapes.join(shape, box(3, 0, 3, 13, 13, 13), BooleanOp.OR);
        //Neck
        shape = Shapes.join(shape, box(5, 13, 5, 11, 14, 11), BooleanOp.OR);
        //Lid
        shape = Shapes.join(shape, box(4, 14, 4, 12, 16, 12), BooleanOp.OR);
        return shape;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        if(pContext.getItemInHand().hasTag()) {
            if(pContext.getItemInHand().getTag().contains("tier")) {
                int tier = pContext.getItemInHand().getTag().getInt("tier");
                if(tier <= 5 && tier >= 1) {
                    return this.defaultBlockState().setValue(TIER, tier).setValue(FACING, pContext.getHorizontalDirection().getOpposite());
                } else {
                    return this.defaultBlockState().setValue(TIER, 1).setValue(FACING, pContext.getHorizontalDirection().getOpposite());
                }
            } else {
                return this.defaultBlockState().setValue(TIER, 1).setValue(FACING, pContext.getHorizontalDirection().getOpposite());
            }
        } else {
            return this.defaultBlockState().setValue(TIER, 1).setValue(FACING, pContext.getHorizontalDirection().getOpposite());
        }
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        if(pState.hasBlockEntity()) {
            if(pLevel.getBlockEntity(pPos) instanceof SingularityJarBlockEntity entity && pStack.hasTag()) {
                CompoundTag itemNBT = pStack.getTag();
                SingularityJarItemStackHandler itemStackHandler = entity.getItemHandler();
                itemStackHandler.deserializeNBT(itemNBT.getCompound("inventory"));
                entity.setItemHandler(itemStackHandler);
            }
        }
        super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);
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
        builder.add(FACING).add(TIER);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        SingularityJarBlockEntity entity = new SingularityJarBlockEntity(pPos, pState);
        entity.setHandlerTier(pState.getValue(TIER));
        return entity;
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
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
            if(lidClicked(pHit.getLocation().subtract(pPos.getX(), pPos.getY(), pPos.getZ())) && isValidLid(pPlayer.getItemInHand(pHand).getItem())) {
                ItemStack itemInHand = pPlayer.getItemInHand(pHand);
                int tierInHand = getTierInHand(itemInHand);
                if(tierInHand > pState.getValue(TIER)) {
                    pPlayer.setItemInHand(pHand, new ItemStack(itemInHand.getItem(), itemInHand.getCount() - 1));
                    if(pPlayer.getItemInHand(pHand).isEmpty()) {
                        pPlayer.setItemInHand(pHand, new ItemStack(getBlockFromTier(pState.getValue(TIER))));
                    } else {
                        pPlayer.addItem(new ItemStack(getBlockFromTier(pState.getValue(TIER))));
                    }
                    pState = pState.setValue(TIER, tierInHand);
                    pLevel.setBlock(pPos, pState, 3);
                    entity.setHandlerTier(tierInHand);
                } else {
                    SingularityJarBlockEntity dummyEntity = entity;
                    dummyEntity.setHandlerTier(tierInHand);
                    if (dummyEntity.getMaxStackSize() >= entity.getStack().getCount()) {
                        pPlayer.setItemInHand(pHand, new ItemStack(itemInHand.getItem(), itemInHand.getCount() - 1));
                        if (pPlayer.getItemInHand(pHand).isEmpty()) {
                            pPlayer.setItemInHand(pHand, new ItemStack(getBlockFromTier(pState.getValue(TIER))));
                        } else {
                            pPlayer.addItem(new ItemStack(getBlockFromTier(pState.getValue(TIER))));
                        }
                        pState = pState.setValue(TIER, tierInHand);
                        pLevel.setBlock(pPos, pState, 3);
                        entity.setHandlerTier(tierInHand);
                    }
                }
            } else if(!lidClicked(pHit.getLocation().subtract(pPos.getX(), pPos.getY(), pPos.getZ()))) {
                if(entity.ticksSinceRightClick <= 20 && entity.ticksSinceRightClick >= 0) {
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
            }
            pPlayer.swing(pHand);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void attack(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer) {
        BlockHitResult pHit = traceEyes(pLevel, pPlayer, pPos);
        if(pLevel.getBlockEntity(pPos) instanceof SingularityJarBlockEntity entity && !pLevel.isClientSide && !lidClicked(pHit.getLocation().subtract(pPos.getX(), pPos.getY(), pPos.getZ()))) {
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

    private boolean lidClicked(Vec3 hit) {
        double hitX = hit.x();
        double hitY = hit.y();
        double hitZ = hit.z();
        return hitY >= 14/16f && hitY <= 1f && hitX >= 4/16f && hitX <= 12/16f && hitZ >= 4/16f && hitZ <= 12/16f;
    }

    private BlockHitResult traceEyes(Level level, Player player, BlockPos blockPos) {
        Vec3 eyePos = player.getEyePosition(1);
        Vec3 viewVector = player.getViewVector(1);
        Vec3 endPos = eyePos.add(viewVector.scale(eyePos.distanceTo(Vec3.atCenterOf(blockPos)) + 1));
        ClipContext context = new ClipContext(eyePos, endPos, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player);
        return level.clip(context);
    }

    private int getTierInHand(ItemStack itemInHand) {
        if(itemInHand.getItem() == Items.GOLD_BLOCK) {
            return 1;
        } else if(itemInHand.getItem() == SpiritBlocks.SOUL_STEEL_BLOCK.get().asItem()) {
            return 2;
        } else if(itemInHand.getItem() == OccultismBlocks.IESNIUM_BLOCK.get().asItem()) {
            return 3;
        } else if(itemInHand.getItem() == ModBlocks.DEORUM_BLOCK.get().asItem()) {
            return 4;
        } else if(itemInHand.getItem() == ModBlocks.DARK_NETHER_STAR_BLOCK.get().asItem()) {
            return 5;
        } else {
            return 0;
        }
    }

    private Item getBlockFromTier(int tier) {
        return switch (tier) {
            case 2 -> SpiritBlocks.SOUL_STEEL_BLOCK.get().asItem();
            case 3 -> OccultismBlocks.IESNIUM_BLOCK.get().asItem();
            case 4 -> ModBlocks.DEORUM_BLOCK.get().asItem();
            case 5 -> ModBlocks.DARK_NETHER_STAR_BLOCK.get().asItem();
            default -> Items.GOLD_BLOCK;
        };
    }

    private boolean isValidLid(Item item) {
        return item == Items.GOLD_BLOCK || item == SpiritBlocks.SOUL_STEEL_BLOCK.get().asItem() || item == OccultismBlocks.IESNIUM_BLOCK.get().asItem() ||
                item == ModBlocks.DEORUM_BLOCK.get().asItem() || item == ModBlocks.DARK_NETHER_STAR_BLOCK.get().asItem();
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if(pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if(blockEntity instanceof SingularityJarBlockEntity) {
                //((SingularityJarBlockEntity) blockEntity).drops();
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Override
    public List<ItemStack> getDrops(BlockState pState, LootContext.Builder pBuilder) {
        List<ItemStack> drops = super.getDrops(pState, pBuilder);
        BlockEntity blockEntity = pBuilder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if(blockEntity instanceof SingularityJarBlockEntity entity) {
            ItemStack stack = new ItemStack(this.asItem());
            if(pState.getValue(TIER) != 1) {
                stack.getOrCreateTag().putInt("tier", pState.getValue(TIER));
            }

            if(!entity.getStack().isEmpty()) {
                stack.getOrCreateTag().put("inventory", entity.getItemHandler().serializeNBT());
            }
            drops.add(stack);
        }
        return drops;
    }
}
