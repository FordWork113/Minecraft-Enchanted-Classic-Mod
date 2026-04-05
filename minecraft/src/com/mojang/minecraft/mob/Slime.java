package com.mojang.minecraft.mob;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.item.ItemStack;
import com.mojang.minecraft.level.item.Item;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.tile.Block;
import com.mojang.minecraft.mob.Mob;
import com.mojang.minecraft.mob.ai.BasicAI;

import java.util.Random;

public class Slime extends Mob {

   public static final long serialVersionUID = 0L;
   public Random random = new Random();
   
   public Slime(Level var1, float var2, float var3, float var4) {
      super(var1);
      this.modelName = "slime";
      this.textureName = "/mob/slime.png";
      this.setPos(var2, var3, var4);
      BasicAI var5 = new BasicAI();
      this.heightOffset = 1.62F;
      this.bobStrength = 0.0F;
      this.deathScore = 65;
      var5.jumping = true;
      this.size = 1 << this.random.nextInt(3);
      this.setSlimeSize(this.size);
   }

   public void tick() {
	      if (this.health <= 0 && this.size <= 2) {
	         this.heightOffset = 1.62F;
	      } else {
	         this.heightOffset = (float)this.size * 1.5F - 1.4375F + 1.62F;
	      }

	      super.tick();

	      this.fallDistance = 0.0F;
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
   
	public void setSlimeSize(int var1) {
		this.size = var1;
		this.setSize(0.6F * (float)var1, 0.6F * (float)var1);
		this.health = var1 * var1;
		this.setPos(this.x, this.y, this.z);
	}
	
   public void die(Entity var1) {

	      int var2 = (int)(Math.random() + Math.random()  + Math.random() + 1.0D);

	      /*for(int var3 = 0; var3 < var2; ++var3) {
	         this.level.addEntity(new Slime(this.level, this.x, this.y, this.z));
	      }*/
	      
	      for(int var3 = 0; var3 < var2; ++var3) {
		      this.level.addEntity(new Item(this.level, this.x, this.y, this.z, new ItemStack(Block.SLIME_BLOCK.id)));
		  }

	      super.die(var1);
   }
      
}

