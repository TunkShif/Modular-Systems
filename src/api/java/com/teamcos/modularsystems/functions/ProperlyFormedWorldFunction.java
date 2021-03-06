package com.teamcos.modularsystems.functions;

import com.teamcos.modularsystems.helpers.FurnaceHelper;
import com.teamcos.modularsystems.notification.Notification;
import com.teamcos.modularsystems.notification.NotificationHelper;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class ProperlyFormedWorldFunction implements WorldFunction {

    boolean shouldContinue = true;
    boolean failed = false;

    @Override
    public void outerBlock(World world, int x, int y, int z) {
        Block blockId = world.getBlock(x, y, z);
        if (world.isAirBlock(x, y, z) || (FurnaceHelper.isBadBlock(blockId, world) && !FurnaceHelper.isModularBlock(blockId))) {
            shouldContinue = false;
        } else if (!FurnaceHelper.isValidBlock(blockId.getUnlocalizedName())) {
            shouldContinue = FurnaceHelper.isModularBlock(blockId);
        }
        if ((world.isAirBlock(x, y, z) || !shouldContinue) && world.isRemote) {
            NotificationHelper.addNotification(new Notification(new ItemStack(Blocks.furnace), EnumChatFormatting.RED + "ERROR: Invalid Block", x + ", " + y + ", " + z, Notification.DEFAULT_DURATION));
        }
    }

    @Override
    public void innerBlock(World world, int x, int y, int z) {
        shouldContinue = world.isAirBlock(x, y, z);
    }

    @Override
    public boolean shouldContinue() {
        return shouldContinue && !failed;
    }

    @Override
    public void reset() {
        shouldContinue = true;
        failed = false;
    }

    @Override
    public WorldFunction copy() {
        return new ProperlyFormedWorldFunction();
    }

    @Override
    public void fail() {
        failed = true;
    }
}
