package com.mojang.minecraft.level.tile;

import java.util.Random;

import com.mojang.minecraft.item.Items;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.item.PrimedTnt;
import com.mojang.minecraft.level.item.SandFall;
import com.mojang.minecraft.level.liquid.LiquidType;
import com.mojang.minecraft.level.tile.Block;

public final class GravelBlock extends Block {

   public GravelBlock(int var1, int var2) {
      super(var1, var2);
   }

   public final void onPlace(Level level, int x, int y, int z) {
      this.fall(level, x, y, z);
   }

   public final void onNeighborChange(Level var1, int var2, int var3, int var4, int var5) {
      this.fall(var1, var2, var3, var4);
   }

   public final int getDrop(Level level, Random rand) {
	  int var6 = level.getTile(x, y, z);
	  if(var6 != Block.GRAVEL.id && rand.nextInt(10) != 0) {
	     return Block.GRAVEL.id;
	  } else {
		 return Items.flint.shiftedIndex;
	  }
   }
   
   private void fall(Level var1, int var2, int var3, int var4) {
      int var11 = var2;
      int var5 = var3;
      int var6 = var4;

      while(true) {
         int var8 = var5 - 1;
         int var10;
         LiquidType var12;
         if(!((var10 = var1.getTile(var11, var8, var6)) == 0?true:((var12 = Block.blocks[var10].getLiquidType()) == LiquidType.WATER?true:var12 == LiquidType.LAVA)) || var5 <= 0) {
            if(var5 != var3) {
               if((var10 = var1.getTile(var11, var5, var6)) > 0 && Block.blocks[var10].getLiquidType() != LiquidType.NOT_LIQUID) {
            	   var1.setTileNoUpdate(var11, var5, var6, 0);
               /*} else {
                   var1.addEntity(new SandFall(var1, (float)var2 + 0.5F, (float)var3 + 0.5F, (float)var4 + 0.5F, this.id));
                   var1.setTile(var2, var3, var4, 0);*/
               }
                   var1.swap(var2, var3, var4, var11, var5, var6);
            }

            return;
         }

         --var5;
      }
   }
}

