package com.mojang.minecraft.item;

import java.util.Random;

import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.item.Arrow;
import com.mojang.minecraft.level.tile.Block;
import com.mojang.minecraft.player.Player;

public final class ItemKey extends Items {
	public transient Random random = new Random();
	   
	public ItemKey(int var1) {
		super(var1);
		this.limitStack = 1;
	}
	
	public final void onPlaced(ItemStack var1, Level var2, int var3, int var4, int var5, int var6) {
		int selected = var2.getTile(var3, var4, var5);
		
		if(selected == Block.DUNGEON_CHEST.id) {
		    --var1.stackSize;
		}
	}

}
