package com.mojang.minecraft.level.tile;

import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.item.Arrow;
import com.mojang.minecraft.level.tile.Block;
import com.mojang.minecraft.player.Player;
import com.mojang.minecraft.item.Items;
import com.mojang.minecraft.item.ItemStack;

public final class DungeonChestBlock extends Block {

   public DungeonChestBlock(int texture, int var2) {
      super(texture);
      this.textureId = var2;
   }

   protected final int getTextureId(int texture) {
	   return texture == 1 ? this.textureId - 1 : (texture == 0 ? this.textureId - 1 : (texture == 3 ? this.textureId + 2 : this.textureId));
   }
   
   public int getDropCount() {
      return (int)(Math.random() + Math.random() + 1.0D);
   }
   
   public final void dropItems(Level var1, int var2, int var3, int var4, float var5) {}
   
   public boolean specialBlockActivated(ItemStack var1, Level var2, int var3, int var4, int var5, Player var6) {
	   if(var1.itemID == Items.key.shiftedIndex && var2.rendererContext$5cd64a7f.gamemode.isSurvival()) {
	       var2.rendererContext$5cd64a7f.hud.addChat("Chest Activated");
	       var2.playSound("random.bow", var6.x, var6.y, var6.z, 1.0F, 1.0F / (this.random.nextFloat() * 0.4F + 0.8F));
	       this.dropRandomItems(var1, var2, var3, var4 + 1, var5, var5);
	       /*int var10 = (int)((Math.random() + Math.random()) * 3.0D + 4.0D);
	    	   for(int var9 = 0; var9 < var10; ++var9) {  	
	 		      var6.level.addEntity(new Arrow(var6.level, var6, var3, var4 + 1.4F, var5, (float)Math.random() * 360.0F, -((float)Math.random()) * 60.0F, 0.4F));
	 		   }*/
	    	   
	    	   return true;
	      }
	   
	   return false;

   }
}
