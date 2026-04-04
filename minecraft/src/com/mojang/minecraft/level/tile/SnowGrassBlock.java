package com.mojang.minecraft.level.tile;

import com.mojang.minecraft.item.ItemStack;
import com.mojang.minecraft.item.Items;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.tile.Block;
import java.util.Random;

public final class SnowGrassBlock extends Block {

   protected SnowGrassBlock(int var1, int var2) {
      super(var1);
      this.textureId = var2;
      this.setPhysics(true);
   }

   protected final int getTextureId(int texture) {
	   return texture == 1?82:(texture == 0?2:83);
   }

   public final int getDrop(Level level, Random rand) {
	   if(rand.nextInt(3) != 0) {
		  return Block.DIRT.id;
	   } else {
	      return Block.SNOW_BLOCK.id;
	   }
   }
}
