package com.mojang.minecraft.mob;

import org.lwjgl.opengl.GL11;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.item.ItemStack;
import com.mojang.minecraft.item.Items;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.item.Item;
import com.mojang.minecraft.level.tile.Block;
import com.mojang.minecraft.mob.QuadrupedMob;
import com.mojang.minecraft.mob.ai.JumpAttackAI;
import com.mojang.minecraft.render.TextureManager;

public class Spider extends QuadrupedMob {

   public static final long serialVersionUID = 0L;
   public String defaultTexture = "/mob/spider.png";
   public String brownSpiderTexture = "/mob/spider_brown.png";
   public int randomTextureId;

   public Spider(Level var1, float var2, float var3, float var4) {
      super(var1, var2, var3, var4);
      this.heightOffset = 0.72F;
      this.modelName = "spider";
      this.textureName = this.defaultTexture;
      this.setSize(1.4F, 0.9F);
      this.setPos(var2, var3, var4);
      this.deathScore = 105;
      this.bobStrength = 0.0F;
      this.ai = new JumpAttackAI();
   }
   
   protected final String getLivingSound() {
	  return null;
   }

   protected final String getHurtSound() {
	  return null;
   }

   protected final String getDeathSound() {
	  return null;
   }
    
   public void die(Entity var1) {
	   
	      int var2 = (int)(Math.random() + Math.random() + 1.0D);

	      for(int var3 = 0; var3 < var2; ++var3) {
	         this.level.addEntity(new Item(this.level, this.x, this.y, this.z, new ItemStack(Items.silk.shiftedIndex)));
	      }
	      
	      int var4 = (int)(Math.random());

	      for(int var3 = 0; var3 < var4; ++var3) {
	         this.level.addEntity(new Item(this.level, this.x, this.y, this.z, new ItemStack(Block.COBWEB.id)));
	      }

	      super.die(var1);
   }
   
   
}
