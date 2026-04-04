package com.mojang.minecraft.level.tile;

import java.util.Random;

import com.mojang.minecraft.item.Items;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.tile.Block;

public final class WoodBlock extends Block {
	   
   protected WoodBlock(int var1, int var2) {
      super(var1);
      this.textureId = var2;
   }

   public final int getDropCount() {
      return random.nextInt(3) + 3;
   }

   public final int getDrop(Level level, Random rand) {
	   return this == Block.LOG?Block.WOOD.id:(this == Block.BIRCH_LOG?Block.BIRCH_WOOD.id:(this == Block.SPRUCE_LOG?Block.SPRUCE_WOOD.id:this.id));
   }

   protected final int getTextureId(int texture) {
      return texture == 1?this.textureId + 1:(texture == 0?this.textureId + 1:this.textureId);
   }
}
