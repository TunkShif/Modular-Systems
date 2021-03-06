package com.teamcos.modularsystems.manager;

import com.teamcos.modularsystems.ModularSystemsAPI;
import com.teamcos.modularsystems.utilities.block.*;
import com.teamcos.modularsystems.utilities.tiles.*;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;

public class ApiModuleManager {

    public static void enableFurnaceModule(CreativeTabs tab, String textureName) {
        registerDummy(tab);
        registerDummyIO(tab);
        registerDummyRF(tab);
        if (ModularSystemsAPI.ic2Present) registerDummyEU(tab);
        registerTank(tab);
        if (textureName != null) {
            ApiBlockManager.furnaceOverlay = new BlockFurnaceOverlay(textureName, "furnaceOverlay", true);
        } else {
            ApiBlockManager.furnaceOverlay = new BlockFurnaceOverlay("modularsystems:custom_overlay", "furnaceOverlay", true);
        }

        GameRegistry.registerBlock(ApiBlockManager.furnaceOverlay, "furnaceOverlay");

        CraftingManager.getInstance().addRecipe(new ItemStack(ApiBlockManager.dummyIOBlock, 1),
                "XXX",
                "XxX",
                "XXX", 'X', Blocks.cobblestone, 'x', Blocks.dispenser);
    }

    public static void enableStorageModule() {

    }

    public static void enableEnchantingModule() {

    }

    public static void enableOreProcessing(CreativeTabs tab) {
        registerDummy(tab);
        registerDummyIO(tab);
        registerDummyRF(tab);
        if (ModularSystemsAPI.ic2Present) registerDummyEU(tab);
        registerTank(tab);

        ApiBlockManager.smelteryOverlay = new BlockSmelteryOverlay();

        GameRegistry.registerBlock(ApiBlockManager.smelteryOverlay, "smelteryOverlay");
    }

    private static boolean registeredDummy = false;
    private static boolean registeredDummyIO = false;
    private static boolean registeredDummyRF = false;
    private static boolean registeredDummyEU = false;

    public static void registerDummy(CreativeTabs tab) {
        if (!registeredDummy) {
            ApiBlockManager.dummyBlock = new DummyBlock(tab, Material.rock, true);
            GameRegistry.registerBlock(ApiBlockManager.dummyBlock, "modularDummy");
            GameRegistry.registerTileEntity(DummyTile.class, "dummyTile");
            registeredDummy = true;
        }
    }

    public static void registerDummyIO(CreativeTabs tab) {
        if (!registeredDummyIO) {
            ApiBlockManager.dummyIOBlock = new DummyIOBlock(tab, Material.rock, true);
            GameRegistry.registerBlock(ApiBlockManager.dummyIOBlock, "modularDummyIO");
            GameRegistry.registerTileEntity(DummyIOTile.class, "dummyIOTile");
            registeredDummyIO = true;
        }
    }

    public static void registerDummyRF(CreativeTabs tab) {
        if (!registeredDummyRF) {
            ApiBlockManager.dummyRFBlock = new DummyRFBlock(tab, Material.rock, true);
            GameRegistry.registerBlock(ApiBlockManager.dummyRFBlock, "modularDummyRF");
            GameRegistry.registerTileEntity(DummyRFTile.class, "dummyRFTile");
            registeredDummyRF = true;
        }
    }

    public static void registerDummyEU(CreativeTabs tab) {
        if (!registeredDummyEU) {
            ApiBlockManager.dummyEUBlock = new DummyEUBlock(tab, Material.rock, true);
            GameRegistry.registerBlock(ApiBlockManager.dummyEUBlock, "modularDummyEU");
            GameRegistry.registerTileEntity(DummyEUTile.class, "dummyEUTile");
            registeredDummyEU = true;
        }
    }

    public static void registerTank(CreativeTabs tab) {
        if (ApiBlockManager.fluidTank == null) {
            ApiBlockManager.fluidTank = new BlockTank(tab);
            GameRegistry.registerBlock(ApiBlockManager.fluidTank, "fluidTank");
            GameRegistry.registerTileEntity(TankLogic.class, "tankTile");
            CraftingManager.getInstance().addRecipe(new ItemStack(ApiBlockManager.fluidTank, 1),
                    "IGI",
                    "GGG",
                    "IGI", 'I', Items.iron_ingot, 'G', Blocks.glass);
        }
    }
}
