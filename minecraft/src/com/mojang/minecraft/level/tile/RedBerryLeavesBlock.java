package com.mojang.minecraft.level.tile;

import java.util.Random;

import com.mojang.minecraft.item.Items;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.tile.Block;
import com.mojang.minecraft.level.tile.LeavesBaseBlock;

public final class RedBerryLeavesBlock extends LeavesBaseBlock {

   protected RedBerryLeavesBlock(int var1, int var2) {
      super(var1, var2, true);
   }

   public final int getDropCount() {
	  return (int)(Math.random() + Math.random() + Math.random() + 1.0D);
   }

   public final int getDrop(Level level, Random rand) {
	  return Items.redberry.shiftedIndex;
   }
   
   public final void setFancy(boolean var1) {
	  this.showNeighborSides = var1;
	  this.textureId = this.oldTexture + (var1 ? 0 : 23);
   }
  
}
