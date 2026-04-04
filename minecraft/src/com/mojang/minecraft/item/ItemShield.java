package com.mojang.minecraft.item;

import com.mojang.minecraft.player.Player;

public final class ItemShield extends Items {

	public ItemShield(int var1) {
		super(var1);
		this.limitStack = 1;
	}
	
	public final boolean useItem(ItemStack var1, Player var2) {			
		return true;
	}
}
	