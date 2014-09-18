package com.pauljoda.modularsystems.storage.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.pauljoda.modularsystems.core.ModularSystems;
import com.pauljoda.modularsystems.core.lib.Reference;
import com.pauljoda.modularsystems.core.managers.BlockManager;
import com.pauljoda.modularsystems.core.util.GeneralSettings;
import com.pauljoda.modularsystems.storage.tiles.TileEntityStorageCore;
import com.pauljoda.modularsystems.storage.tiles.TileEntityStorageExpansion;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockStorageExpansion extends BlockContainer {

	@SideOnly(Side.CLIENT)
	public IIcon errorIcon;

	public BlockStorageExpansion() {
		super(Material.wood);
		setBlockName("modularsystems:blockStorageExpansion");
		setStepSound(Block.soundTypeWood);
		setHardness(3.5f);
		setCreativeTab(ModularSystems.tabModularSystems);
	}

	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister)
	{
		blockIcon = par1IconRegister.registerIcon("modularsystems:chestSide");
		errorIcon = par1IconRegister.registerIcon("modularsystems:chestSideError");
	}
	
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta)
	{
		return meta == 0 ? blockIcon : errorIcon;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
	{
		if(player.isSneaking())
			return false;

		TileEntityStorageExpansion dummy = (TileEntityStorageExpansion)world.getTileEntity(x, y, z);

		if(dummy != null && dummy.getCore() != null)
		{
			TileEntityStorageCore core = dummy.getCore();
			return core.getBlockType().onBlockActivated(world, core.xCoord, core.yCoord, core.zCoord, player, par6, par7, par8, par9);
		}

		if(dummy.getCore() == null)
		{
			dummy.invalidateCore();
			if(world.isRemote)
				player.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.RED + "Could not locate core, please replace this block"));
		}
		return true;
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase par5EntityLivingBase, ItemStack itemstack)
	{
		TileEntityStorageExpansion expansion = (TileEntityStorageExpansion)world.getTileEntity(x, y, z);

		boolean registered = false;
		//Look for the Core
		for(int i = -1; i <= 1; i++)
		{
			for(int j = -1; j <= 1; j++)
			{
				for(int k = -1; k <= 1; k++)
				{
					//Checks to make sure we are looking at only adjacent blocks
					if(!(Math.abs(i) == 1 ? (Math.abs(j) == 0 && Math.abs(k) == 0) : ((Math.abs(j) == 1) ? Math.abs(k) == 0 : Math.abs(k) == 1)))
						continue;

					Block localBlock = world.getBlock(i + x, j + y, k + z);
					if(localBlock == BlockManager.storageCore)
					{
						TileEntityStorageCore core = (TileEntityStorageCore)world.getTileEntity(i + x, j + y, k + z);

						expansion.setCore(core);
						expansion.getCore().setInventoryRows(core.inventoryRows + 1);
						world.markBlockForUpdate(core.xCoord, core.yCoord, core.zCoord);
						registered = true;
						break;

					}
				}
			}
		}

		//Build off chain
		if(!registered)
		{
			for(int i = -1; i <= 1; i++)
			{
				for(int j = -1; j <= 1; j++)
				{
					for(int k = -1; k <= 1; k++)
					{
						//Checks to make sure we are looking at only adjacent blocks
						if(!(Math.abs(i) == 1 ? (Math.abs(j) == 0 && Math.abs(k) == 0) : ((Math.abs(j) == 1) ? Math.abs(k) == 0 : Math.abs(k) == 1)))
							continue;

						Block localBlock = world.getBlock(i + x, j + y, k + z);
						if(isStorageExpansion(localBlock)  && !registered)
						{
							TileEntityStorageExpansion next = (TileEntityStorageExpansion)world.getTileEntity(i + x, j + y, k + z);

							if(next.getCore() != null)
							{
								TileEntityStorageCore core = (TileEntityStorageCore)next.getCore();
								if(core.inventoryRows < GeneralSettings.maxExpansionSize)
								{
									expansion.setCore(core);
									expansion.getCore().setInventoryRows(core.inventoryRows + 1);
									world.markBlockForUpdate(core.xCoord, core.yCoord, core.zCoord);

									next.setNext(expansion);
									world.markBlockForUpdate(i + x, j + y, k + z);
									registered = true;
									break;
								}
							}
						}
					}
				}
			}
		}
		super.onBlockPlacedBy(world, x, y, z, par5EntityLivingBase, itemstack);
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block par5, int par6)
	{
		TileEntityStorageExpansion expansion = (TileEntityStorageExpansion)world.getTileEntity(x, y, z);
		expansion.invalidateExpansion();
		expansion.invalidateCore();
		super.breakBlock(world, x, y, z, par5, par6);
	}
	
	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		TileEntityStorageExpansion tile = new TileEntityStorageExpansion();
		tile.setTileType(Reference.BASIC_STORAGE_EXPANSION);
		return tile;
	}
	
	public static boolean isStorageExpansion(Block block)
	{
		return (block == BlockManager.storageExpansion || block == BlockManager.storageHoppingExpansion 
				|| block == BlockManager.storageArmorExpansion);
	}
}
