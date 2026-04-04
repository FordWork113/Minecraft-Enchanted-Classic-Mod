package com.mojang.minecraft.mob;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.item.ItemStack;
import com.mojang.minecraft.item.Items;
import com.mojang.minecraft.level.item.Item;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.tile.Block;
import com.mojang.minecraft.mob.QuadrupedMob;
import com.mojang.minecraft.mob.ai.BasicAI;
import com.mojang.minecraft.mob.ai.NeutralAI;

public class Wolf extends Mob {

   public static final long serialVersionUID = 0L;

   public Wolf(Level var1, float var2, float var3, float var4) {
      super(var1);
      this.heightOffset = 1.72F;
      this.modelName = "wolf";
      this.textureName = "/mob/wolf.png";
      this.setSize(0.8F, 0.8F);
      this.setPos(var2, var3, var4);
      BasicAI var5 = new BasicAI();
      this.bobStrength = 0.5F;
      this.deathScore = 10;
      var5.jumping = true;
      this.health = 10;
      this.ai = var5;
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

}
