/*
 * This file is part of Applied Energistics 2.
 * Copyright (c) 2013 - 2014, AlgorithmX2, All rights reserved.
 *
 * Applied Energistics 2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Applied Energistics 2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Applied Energistics 2.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */

package appeng.block.misc;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import appeng.api.util.IOrientable;
import appeng.api.util.IOrientableBlock;
import appeng.block.AEBaseTileBlock;
import appeng.helpers.MetaRotation;
import appeng.tile.misc.LightDetectorBlockEntity;

public class LightDetectorBlock extends AEBaseTileBlock<LightDetectorBlockEntity> implements IOrientableBlock {

    // Used to alternate between two variants of the fixture on adjacent blocks
    public static final BooleanProperty ODD = BooleanProperty.create("odd");

    public LightDetectorBlock() {
        super(defaultProps(Material.MISCELLANEOUS).notSolid());

        this.setDefaultState(this.getDefaultState().with(BlockStateProperties.FACING, Direction.UP).with(ODD, false));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(BlockStateProperties.FACING);
        builder.add(ODD);
    }

    @Override
    public int getWeakPower(final BlockState state, final BlockView w, final BlockPos pos, final Direction side) {
        if (w instanceof World && this.getBlockEntity(w, pos).isReady()) {
            // FIXME: This is ... uhm... fishy
            return ((World) w).getLight(pos) - 6;
        }

        return 0;
    }

    @Override
    public void onNeighborChange(BlockState state, IWorldReader world, BlockPos pos, BlockPos neighbor) {
        super.onNeighborChange(state, world, pos, neighbor);

        final LightDetectorBlockEntity tld = this.getBlockEntity(world, pos);
        if (tld != null) {
            tld.updateLight();
        }
    }

    @Override
    public void animateTick(final BlockState state, final World worldIn, final BlockPos pos, final Random rand) {
        // cancel out lightning
    }

    @Override
    public boolean isValidOrientation(final WorldAccess w, final BlockPos pos, final Direction forward, final Direction up) {
        return this.canPlaceAt(w, pos, up.getOpposite());
    }

    private boolean canPlaceAt(final BlockView w, final BlockPos pos, final Direction dir) {
        final BlockPos test = pos.offset(dir);
        BlockState blockstate = w.getBlockState(test);
        return blockstate.isSolidSide(w, test, dir.getOpposite());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockView w, BlockPos pos, ShapeContext context) {

        // FIXME: We should / rather MUST use state here because at startup, this gets
        // called without a world

        final Direction up = this.getOrientable(w, pos).getUp();
        final double xOff = -0.3 * up.getOffsetX();
        final double yOff = -0.3 * up.getOffsetY();
        final double zOff = -0.3 * up.getOffsetZ();
        return VoxelShapes
                .create(new Box(xOff + 0.3, yOff + 0.3, zOff + 0.3, xOff + 0.7, yOff + 0.7, zOff + 0.7));
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView worldIn, BlockPos pos,
            ShapeContext context) {
        return VoxelShapes.empty();
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos,
            boolean isMoving) {
        final Direction up = this.getOrientable(world, pos).getUp();
        if (!this.canPlaceAt(world, pos, up.getOpposite())) {
            this.dropTorch(world, pos);
        }
    }

    private void dropTorch(final World w, final BlockPos pos) {
        final BlockState prev = w.getBlockState(pos);
        w.breakBlock(pos, true);
        w.updateListeners(pos, prev, w.getBlockState(pos), 3);
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader w, BlockPos pos) {
        for (final Direction dir : Direction.values()) {
            if (this.canPlaceAt(w, pos, dir)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public IOrientable getOrientable(final BlockView w, final BlockPos pos) {
        return new MetaRotation(w, pos, BlockStateProperties.FACING);
    }

}
