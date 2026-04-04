package com.mojang.minecraft.gamemode;

import com.mojang.minecraft.Minecraft;
import com.mojang.minecraft.SessionData;
import com.mojang.minecraft.gui.BlockSelectScreen;
import com.mojang.minecraft.item.ItemStack;
import com.mojang.minecraft.item.Items;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.tile.Block;
import com.mojang.minecraft.player.Player;

public class CreativeGameMode extends GameMode
{
	public CreativeGameMode(Minecraft minecraft)
	{
		super(minecraft);

		instantBreak = true;
	}

	public void apply(Level level)
	{
		super.apply(level);

		level.gamemode = 0;
		
		level.removeAllNonCreativeModeEntities();

		level.growTrees = false;
	}

	public void openInventory()
	{
		BlockSelectScreen blockSelectScreen = new BlockSelectScreen();

		minecraft.setCurrentScreen(blockSelectScreen);
	}
	
	public final void apply(Player var1) {
		for(int var2 = 0; var2 < 9; ++var2) {		
			if(var1.inventory.Inventory[var2] == null) {
			    this.minecraft.player.inventory.Inventory[var2] = new ItemStack(((Block)SessionData.allowedBlocks.get(var2)).id);
			} else {
				this.minecraft.player.inventory.Inventory[var2].stackSize = 1;
			} 
			
			if(var1.level != null && var1.level.rendererContext$5cd64a7f.networkManager != null) {
			    this.minecraft.player.inventory.Inventory[var2] = new ItemStack(((Block)SessionData.allowedBlocks.get(var2)).id);
			}
			
		}

	}
	
	public boolean isSurvival()
	{
		return false;
	}
	
	public boolean isCreative()
	{
		return true;
	}

}
