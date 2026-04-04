package com.mojang.minecraft.level.tile;

import com.mojang.minecraft.item.Items;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.tile.Block;
import com.mojang.minecraft.level.tile.FlowerBlock;
import java.util.Random;

public final class BushBlock extends FlowerBlock {

   protected BushBlock(int var1, int var2) {
      super(var1, var2);
      float var3 = 0.4F;
      this.setBounds(0.5F - var3, 0.0F, 0.5F - var3, var3 + 0.5F, var3 * 2.0F, var3 + 0.5F);
   }
   
   public final int getDropCount() {
	  return (int)(Math.random() + Math.random() + 1.0D);
   }
   
   public final int getDrop(Level level, Random rand) {
	  return Items.stick.shiftedIndex;
  }

   public final void update(Level level, int x, int y, int z, Random rand) {
	      if(!level.growTrees) {
	          int var6 = level.getTile(x, y - 1, z);
	          int var7 = level.getTile(x, y, z);
	          if(!level.isLit(x, y, z) || var6 != Block.DIRT.id && var6 != Block.GRASS.id && var6 != Block.SNOW_GRASS.id && var7 == Block.BUSH.id) {
	             level.setTile(x, y, z, 0);
	          }
	          
	          if(!level.isLit(x, y, z) || var6 != Block.SAND.id && var7 == Block.DEAD_BUSH.id) {
		             level.setTile(x, y, z, 0);
		      }

	       }
   }
}
