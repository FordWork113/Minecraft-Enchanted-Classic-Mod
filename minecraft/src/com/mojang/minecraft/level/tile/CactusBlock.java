package com.mojang.minecraft.level.tile;

import java.util.Random;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.level.item.Item;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.tile.Block;
import com.mojang.minecraft.phys.AABB;
import com.mojang.minecraft.player.Player;

public final class CactusBlock extends Block {
   
   public CactusBlock(int var1, int var2) {
      super(50, 51);
   }

   protected final int getTextureId(int texture) {
	  return texture == 0?this.textureId + 2:(texture == 1?this.textureId + 1:this.textureId);
   }

   public void update(Level level, int x, int y, int z, Random rand) {
	      if(!level.growTrees) {
	         int var6 = level.getTile(x, y - 1, z);
	         if(!level.isLit(x, y, z) || var6 != Block.SAND.id && var6 != Block.CACTUS.id) {
	            level.setTile(x, y, z, 0);
	         }

	      }
   }

}
