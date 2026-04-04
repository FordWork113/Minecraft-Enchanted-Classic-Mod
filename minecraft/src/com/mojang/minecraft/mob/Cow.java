package com.mojang.minecraft.mob;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.item.ItemStack;
import com.mojang.minecraft.item.Items;
import com.mojang.minecraft.level.item.Item;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.tile.Block;
import com.mojang.minecraft.mob.QuadrupedMob;
import com.mojang.minecraft.model.AnimalModel;
import com.mojang.minecraft.render.TextureManager;
import org.lwjgl.opengl.GL11;

public class Cow extends QuadrupedMob {

   public static final long serialVersionUID = 0L;

   public Cow(Level var1, float var2, float var3, float var4) {
      super(var1, var2, var3, var4);
      this.setSize(1.4F, 1.72F);
      this.setPos(var2, var3, var4);
      this.heightOffset = 1.72F;
      this.modelName = "cow";
      this.health = 10;
      this.textureName = "/mob/cow.png";
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
      if(var1 != null) {
         var1.awardKillScore(this, 15);
      }

      int var2 = (int)(Math.random() + Math.random() + 1.0D);

      for(int var3 = 0; var3 < var2; ++var3) {
         this.level.addEntity(new Item(this.level, this.x, this.y, this.z, new ItemStack(Items.beef.shiftedIndex)));
      }

      super.die(var1);
   }
}
