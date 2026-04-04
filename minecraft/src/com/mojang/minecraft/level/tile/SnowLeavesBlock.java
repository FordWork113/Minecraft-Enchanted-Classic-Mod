package com.mojang.minecraft.level.tile;

import java.util.Random;

import com.mojang.minecraft.item.Items;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.tile.Block;
import com.mojang.minecraft.level.tile.LeavesBaseBlock;

public final class SnowLeavesBlock extends LeavesBaseBlock {

   protected SnowLeavesBlock(int var1, int var2) {
      super(var1, var2, true);
   }

   public final int getDropCount() {
      return random.nextInt(2) == 0?1:0;
   }

   public final int getDrop(Level level, Random rand) {
	   if(rand.nextInt(2) != 0) { 
		 return Items.stick.shiftedIndex;
	   } if(rand.nextInt(4) != 0) {
		   return this == Block.SNOW_LEAVES?Block.SAPLING.id:(this == Block.SNOW_BIRCH_LEAVES?Block.BIRCH_SAPLING.id:(this == Block.SNOW_SPRUCE_LEAVES?Block.SPRUCE_SAPLING.id:(this == Block.SNOW_OTHER_REDBERRY_LEAVES?Block.REDBERRY_SAPLING.id:(this == Block.SNOW_OTHER_BLACKBERRY_LEAVES?Block.BLACKBERRY_SAPLING.id:this.id))));
	   } else {
	     return Items.apple.shiftedIndex;
	   }
   }
   
   public final void setFancy(boolean var1) {
	  this.showNeighborSides = var1;
	  this.textureId = this.oldTexture + (var1 ? 0 : 7);
   }
}
