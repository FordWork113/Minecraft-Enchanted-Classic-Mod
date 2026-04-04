package com.mojang.minecraft.level.tile;

import java.util.Random;

import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.tile.Block;

public final class StoneBlock extends Block {

   public StoneBlock(int var1, int var2) {
      super(var1, var2);
   }

   public final int getDrop(Level level, Random rand) {
      return Block.COBBLESTONE.id;
   }
}
