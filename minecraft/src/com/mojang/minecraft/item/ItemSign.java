package com.mojang.minecraft.item;

import java.util.Random;

import com.mojang.minecraft.gui.SignEditScreen;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.item.Arrow;
import com.mojang.minecraft.player.Player;

public final class ItemSign extends Items {
	   
	public ItemSign(int var1) {
		super(var1);
	}

	public final boolean onItemRightClick(ItemStack var1, Player var2, Level var3) {
		if(var3.rendererContext$5cd64a7f.selected != null && var3.rendererContext$5cd64a7f.networkManager == null) {
			var2.releaseAllKeys();
            var3.rendererContext$5cd64a7f.setCurrentScreen(new SignEditScreen());
		} else {
	    	return false;
	    }
        
		return true;
	}
}
