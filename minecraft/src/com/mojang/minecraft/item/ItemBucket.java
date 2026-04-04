package com.mojang.minecraft.item;

import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.tile.Block;
import com.mojang.minecraft.phys.AABB;
import com.mojang.minecraft.player.Player;

public final class ItemBucket extends Items {
   
   public ItemBucket(int var1) {
      super(var1);
   }
   
   public final void onPlaced(ItemStack var1, Level var2, int var3, int var4, int var5, int var6) {
	   
		if(var1.itemID == Items.waterbucket.shiftedIndex) {
		   var2.netSetTile(var3, var4, var5, Block.STATIONARY_WATER.id);
		}
					
		if(var1.itemID == Items.lavabucket.shiftedIndex) {
		   var2.netSetTile(var3, var4, var5, Block.STATIONARY_LAVA.id);
		}
		
		var1.itemID = Items.bucket.shiftedIndex;
		
   }
   
}