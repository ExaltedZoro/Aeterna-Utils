package com.exaltedzoro.aeternautils.block;

import com.exaltedzoro.aeternautils.block.entity.ModBlockEntities;
import com.exaltedzoro.aeternautils.block.entity.SolarSourcelinkBlockEntity;
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

public class SolarSourcelinkBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    private static final VoxelShape SHAPE = makeShape();
    public SolarSourcelinkBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    public static VoxelShape makeShape() {
        VoxelShape shape = Shapes.empty();

        //Body
        shape = Shapes.join(shape, box(4, 4, 4, 12, 12, 12), BooleanOp.OR);

        //North support
        shape = Shapes.join(shape, box(7, 12, 0, 9, 16, 3), BooleanOp.OR);
        shape = Shapes.join(shape, box(7, 1, 3, 9, 15, 4), BooleanOp.OR);
        shape = Shapes.join(shape, box(7, 0, 0, 9, 4, 3), BooleanOp.OR);

        //South support
        shape = Shapes.join(shape, box(7, 12, 13, 9, 16, 16), BooleanOp.OR);
        shape = Shapes.join(shape, box(7, 1, 12, 9, 15, 13), BooleanOp.OR);
        shape = Shapes.join(shape, box(7, 0, 13, 9, 4, 16), BooleanOp.OR);

        //East support
        shape = Shapes.join(shape, box(13, 12, 7, 16, 16, 9), BooleanOp.OR);
        shape = Shapes.join(shape, box(12, 1, 7, 13, 15, 9), BooleanOp.OR);
        shape = Shapes.join(shape, box(13, 0, 7, 16, 4, 9), BooleanOp.OR);

        //West support
        shape = Shapes.join(shape, box(0, 12, 7, 3, 16, 9), BooleanOp.OR);
        shape = Shapes.join(shape, box(3, 1, 7, 4, 15, 9), BooleanOp.OR);
        shape = Shapes.join(shape, box(0, 0, 7, 3, 4, 9), BooleanOp.OR);

        return shape;
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
        return new SolarSourcelinkBlockEntity(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pType) {
        return createTickerHelper(pType, ModBlockEntities.SOLAR_SOURCELINK.get(), SolarSourcelinkBlockEntity::tick);
    }
}
