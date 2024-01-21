package com.exaltedzoro.aeternautils.block;

import com.exaltedzoro.aeternautils.block.entity.ModBlockEntities;
import com.exaltedzoro.aeternautils.block.entity.SculkingSourcelinkBlockEntity;
import com.exaltedzoro.aeternautils.block.entity.SolarSourcelinkBlockEntity;
import dev.latvian.mods.kubejs.core.ImageButtonKJS;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class SculkingSourcelinkBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    private static final VoxelShape SHAPE = makeShape();

    protected SculkingSourcelinkBlock(Properties pProperties) {
        super(pProperties);
    }

    public static VoxelShape makeShape() {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, box(3, 0, 3, 13, 4, 13), BooleanOp.OR);
        shape = Shapes.join(shape, box(6, 4, 6, 10, 12, 10), BooleanOp.OR);
        shape = Shapes.join(shape, box(7, 12, 7, 9, 16, 9), BooleanOp.OR);

        //Stubs
        shape = Shapes.join(shape, box(1, 0, 7, 3, 3, 9), BooleanOp.OR);
        shape = Shapes.join(shape, box(13, 0, 7, 15, 3, 9), BooleanOp.OR);
        shape = Shapes.join(shape, box(7, 0, 1, 9, 3, 3), BooleanOp.OR);
        shape = Shapes.join(shape, box(7, 0, 13, 9, 3, 15), BooleanOp.OR);

        //Supports
        shape = Shapes.join(shape, box(5, 9, 7, 6, 10, 9), BooleanOp.OR);
        shape = Shapes.join(shape, box(10, 9, 7, 11, 10, 9), BooleanOp.OR);
        shape = Shapes.join(shape, box(7, 9, 10, 9, 10, 11), BooleanOp.OR);
        shape = Shapes.join(shape, box(7, 9, 5, 9, 10, 6), BooleanOp.OR);

        //Top stubs
        shape = Shapes.join(shape, box(7, 10, 4, 9, 14, 6), BooleanOp.OR);
        shape = Shapes.join(shape, box(7, 10, 10, 9, 14, 12), BooleanOp.OR);
        shape = Shapes.join(shape, box(10, 10, 7, 12, 14, 9), BooleanOp.OR);
        shape = Shapes.join(shape, box(4, 10, 7, 6, 14, 9), BooleanOp.OR);

        return shape;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new SculkingSourcelinkBlockEntity(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pType) {
        return createTickerHelper(pType, ModBlockEntities.SCULKING_SOURCELINK.get(), SculkingSourcelinkBlockEntity::tick);
    }
}
