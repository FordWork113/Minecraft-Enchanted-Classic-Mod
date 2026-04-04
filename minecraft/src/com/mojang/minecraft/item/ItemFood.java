package com.mojang.minecraft.item;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.player.Player;
import com.mojang.minecraft.player.Inventory;

public final class ItemFood extends Items {
	private int foodEffective;

	public ItemFood(int var1, int var2) {
		super(var1);
		this.foodEffective = var2;
	}

	public final boolean useItem(ItemStack var1, Player var2) {			
		Player var10000 = var2;
		int var4 = this.foodEffective;
		Player var3 = var10000;

		if(var3.health > var3.MAX_HEALTH) {
			return false;
		}
	
		if(var3.health > 0 && var3.health < var3.MAX_HEALTH) {
			--var1.stackSize;	
			var3.health += var4;
			if(var3.health > var2.MAX_HEALTH) {
				var3.health = var2.MAX_HEALTH;
			}

			var3.heal(foodEffective);
		} else {
	    	return false;
	    }
		
		
		return true;
	}
}
