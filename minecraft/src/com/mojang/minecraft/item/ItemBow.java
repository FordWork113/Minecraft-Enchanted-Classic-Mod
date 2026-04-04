package com.mojang.minecraft.item;

import java.util.Random;

import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.item.Arrow;
import com.mojang.minecraft.player.Player;

public final class ItemBow extends Items {
	public transient Random random = new Random();
	   
	public ItemBow(int var1) {
		super(var1);
		this.limitStack = 1;
	}

	public final boolean onItemRightClick(ItemStack var1, Player var2, Level var3) {
		if(var2.arrows > 0) {
			var3.addEntity(new Arrow(var3, var2, var2.x, var2.y, var2.z, var2.yRot, var2.xRot, 1.2F));
			var3.playSound("random.bow", var2.x, var2.y, var2.z, 1.0F, 1.0F / (this.random.nextFloat() * 0.4F + 0.8F));
			--var2.arrows;
	    } else {
	    	return false;
	    }
		
		return true;
	}
}
