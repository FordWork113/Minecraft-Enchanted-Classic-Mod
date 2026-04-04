package com.mojang.minecraft.mob;

import org.lwjgl.opengl.GL11;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.item.ItemStack;
import com.mojang.minecraft.item.Items;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.item.Item;
import com.mojang.minecraft.mob.HumanoidMob;
import com.mojang.minecraft.mob.ai.BasicAI;
import com.mojang.minecraft.mob.ai.BasicAttackAI;
import com.mojang.minecraft.render.TextureManager;

public class GiantZombie extends Zombie {

   public static final long serialVersionUID = 0L;

   public GiantZombie(Level var1, float var2, float var3, float var4) {
      super(var1, var2, var3, var4);
      this.setSize(this.bbWidth * 6.0F, this.bbHeight * 6.0F);
      this.heightOffset *= 6.0F;
      this.modelName = "zombie";
      this.textureName = "/mob/zombie.png";
      BasicAI var5 = new BasicAI();
      this.deathScore = 80;
      var5.defaultLookAngle = 30;
      var5.runSpeed = 0.5F;
      this.ai = var5;
	  this.health *= 10;
   }
}
