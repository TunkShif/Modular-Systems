package com.teamcos.modularsystems.functions;

import com.teamcos.modularsystems.manager.ApiBlockManager;
import com.teamcos.modularsystems.utilities.tiles.DummyIOTile;
import com.teamcos.modularsystems.utilities.tiles.DummyTile;
import com.teamcos.modularsystems.utilities.tiles.FueledRecipeTile;
import net.minecraft.block.Block;
import net.minecraft.world.World;

public class RevertDummiesWorldFunction implements WorldFunction {
    private final FueledRecipeTile core;
    private boolean failed = false;

    public RevertDummiesWorldFunction(FueledRecipeTile core) {
        this.core = core;
    }

    @Override
    public void outerBlock(World world, int x, int y, int z) {
        Block blockId = world.getBlock(x,y,z);
        if (blockId == core.getDummyBlock()) {
            DummyTile dummyTE = (DummyTile) world.getTileEntity(x, y, z);
            world.setBlock(x, y, z, dummyTE.getBlock());
            world.setBlockMetadataWithNotify(x, y, z, dummyTE.getMetadata(), 2);
        } else if (blockId == ApiBlockManager.dummyIOBlock) {
            DummyIOTile te = (DummyIOTile) world.getTileEntity(x, y, z);
            te.unsetCore();
        }

        world.markBlockForUpdate(x, y, z);
    }

    @Override
    public void innerBlock(World world, int x, int y, int z) {

    }

    @Override
    public boolean shouldContinue() {
        return !failed;
    }

    @Override
    public void reset() {
        failed = false;
    }

    @Override
    public WorldFunction copy() {
        return new RevertDummiesWorldFunction(core);
    }

    @Override
    public void fail() {
        failed = true;
    }
}
