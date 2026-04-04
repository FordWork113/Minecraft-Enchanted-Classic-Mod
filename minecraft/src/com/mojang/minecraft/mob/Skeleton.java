package com.mojang.minecraft.mob;

import com.mojang.minecraft.level.item.Arrow;
import com.mojang.minecraft.level.item.Item;
import com.mojang.minecraft.Entity;
import com.mojang.minecraft.item.ItemStack;
import com.mojang.minecraft.item.Items;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.mob.Skeleton$1;

public class Skeleton extends HumanoidMob {

   public static final long serialVersionUID = 0L;

   public Skeleton(Level var1, float var2, float var3, float var4) {
      super(var1, var2, var3, var4);
      this.modelName = "skeleton";
      this.textureName = "/mob/skeleton.png";
      Skeleton$1 var5 = new Skeleton$1(this);
      this.heightOffset = 1.62F;
      this.deathScore = 120;
      var5.runSpeed = 0.3F;
      var5.damage = 8;
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

   public void shootArrow(Level var1) {
      var1.addEntity(new Arrow(var1, this, this.x, this.y, this.z, this.yRot + 180.0F + (float)(Math.random() * 45.0D - 22.5D), this.xRot - (float)(Math.random() * 45.0D - 10.0D), 1.0F));
   }

   static void shootRandomArrow(Skeleton var0) {
      var0 = var0;
      int var1 = (int)((Math.random() + Math.random()) * 3.0D + 4.0D);

      for(int var2 = 0; var2 < var1; ++var2) {
         var0.level.addEntity(new Arrow(var0.level, var0.level.getPlayer(), var0.x, var0.y - 0.2F, var0.z, (float)Math.random() * 360.0F, -((float)Math.random()) * 60.0F, 0.4F));
      }

   }

   public void die(Entity var1) {

      int var2 = (int)(Math.random() + Math.random() + 1.0D);

      for(int var3 = 0; var3 < var2; ++var3) {
    	  this.level.addEntity(new Item(this.level, this.x, this.y, this.z, new ItemStack(Items.bone.shiftedIndex)));
      }

      super.die(var1);
   }
}
