package com.mojang.minecraft.item;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.player.Player;
import com.mojang.minecraft.player.Inventory;

public final class ItemRottenFlesh extends Items {

	public ItemRottenFlesh(int var1) {
		super(var1);
	}

	public final boolean useItem(ItemStack var1, Player var2) {
		--var1.stackSize;
		Player var10000 = var2;
		Player var3 = var10000;
		var3.hurt((Entity)null, 5);
		return true;
	}
}
