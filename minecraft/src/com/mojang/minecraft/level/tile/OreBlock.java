package com.mojang.minecraft.level.tile;

import java.util.Random;

import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.tile.Block;

public final class OreBlock extends Block {

   public OreBlock(int var1, int var2) {
      super(var1, var2);
   }

   public final int getDrop(Level level, Random rand) {
      return this == Block.COAL_ORE?Block.SLAB.id:(this == Block.GOLD_ORE?Block.GOLD_BLOCK.id:(this == Block.IRON_ORE?Block.IRON_BLOCK.id:(this == Block.DIAMOND_ORE?Block.DIAMOND_BLOCK.id:(this == Block.RUBY_ORE?Block.RUBY_BLOCK.id:this.id))));
   }

   public final int getDropCount() {
      return random.nextInt(3) + 1;
   }
}
