package com.mojang.minecraft.player;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.Minecraft;
import com.mojang.minecraft.Timer;
import com.mojang.minecraft.item.ItemStack;
import com.mojang.minecraft.level.item.Arrow;
import com.mojang.minecraft.level.item.Item;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.mob.Mob;
import com.mojang.minecraft.mob.Skeleton;
import com.mojang.minecraft.model.HumanoidModel;
import com.mojang.minecraft.player.InputHandler;
import com.mojang.minecraft.player.Inventory;
import com.mojang.minecraft.player.Player$1;
import com.mojang.minecraft.render.Renderer;
import com.mojang.minecraft.render.TextureManager;
import com.mojang.minecraft.level.tile.Block;
import com.mojang.util.MathHelper;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class Player extends Mob {

   public static final long serialVersionUID = 0L;
   public static final int MAX_HEALTH = 20;
   public static final int MAX_ARROWS = 99;
   public transient InputHandler input;
   public Inventory inventory = new Inventory(this);
   public byte userType = 0;
   public float oBob;
   public float bob;
   public int score = 0;
   public int arrows = 20;
   private static int newTextureId = -1;
   public static BufferedImage newTexture;
   public transient Random random = new Random();
   
   public Player(Level var1) {
      super(var1);
      if(var1 != null) {
         var1.player = this;
         var1.removeEntity(this);
         var1.addEntity(this);
      }

      this.heightOffset = 1.62F;
      this.health = MAX_HEALTH;
      this.modelName = "humanoid";
      this.rotOffs = 180.0F;
      this.ai = new Player$1(this);
   }

   public void resetPos() {
      this.heightOffset = 1.62F;
      this.setSize(0.6F, 1.8F);
      super.resetPos();
      if(this.level != null) {
         this.level.player = this;
      }

      this.health = this.MAX_HEALTH;
      this.deathTime = 0;
      this.score = 0;
   }
   
   public void aiStep() {
      this.oBob = this.bob;
      this.input.updateMovement();
	  Inventory var6 = this.inventory;

		for(int var7 = 0; var7 < var6.Inventory.length; ++var7) {
			if(var6.Inventory[var7] != null && var6.Inventory[var7].animationsToGo > 0) {
				--var6.Inventory[var7].animationsToGo;
			}
		}
		
      super.aiStep();
      float var1 = MathHelper.sqrt(this.xd * this.xd + this.zd * this.zd);
      float var2 = (float)Math.atan((double)(-this.yd * 0.2F)) * 15.0F;
      if(var1 > 0.1F) {
         var1 = 0.1F;
      }

      if(!this.onGround || this.health <= 0) {
         var1 = 0.0F;
      }

      if(this.onGround || this.health <= 0) {
         var2 = 0.0F;
      }

      this.bob += (var1 - this.bob) * 0.4F;
      this.tilt += (var2 - this.tilt) * 0.8F;
      
      List var3;
      if(this.health > 0 && (var3 = this.level.findEntities(this, this.bb.grow(1.0F, 0.0F, 1.0F))) != null) {
			if(var3 != null) {
				for(int var16 = 0; var16 < var3.size(); ++var16) {
					Entity var7 = (Entity)var3.get(var16);
					
					if(var7 instanceof Arrow) {
						((Entity)var3.get(var16)).playerTouch(this);
					} else if (var7 instanceof Item) {
						Item var8 = (Item)var7;
						if(var8.delayBeforeCanPickup == 0 && this.inventory.addResource(var8.item)) {
							((Entity)var3.get(var16)).playerTouch(var8);
						}
					}
				}
			}
      }
   }

   public void render(TextureManager var1, float var2) {}

   public void releaseAllKeys() {
      this.input.resetKeys();
   }

   public void setKey(int var1, boolean var2) {
      this.input.setKeyState(var1, var2);
   }

   public int getScore() {
      return this.score;
   }

   public HumanoidModel getModel() {
      return (HumanoidModel)modelCache.getModel(this.modelName);
   }

   public boolean addResource(ItemStack var1) {
	  return this.inventory.addResource(var1);
  }
   
   public void die(Entity var1) {
      this.setSize(0.2F, 0.2F);
      this.setPos(this.x, this.y, this.z);
      this.yd = 0.1F;
      if(var1 != null) {
         this.xd = -MathHelper.cos((this.hurtDir + this.yRot) * 3.1415927F / 180.0F) * 0.1F;
         this.zd = -MathHelper.sin((this.hurtDir + this.yRot) * 3.1415927F / 180.0F) * 0.1F;
      } else {
         this.xd = this.zd = 0.0F;
      }

      this.heightOffset = 0.1F;
      //this.level.playSound("random.hurt", this, 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
      this.inventory.dropAll();
      this.shootArrows(this);
      this.arrows = 0;
   }

   public void remove() {}

   public void awardKillScore(Entity var1, int var2) {
      this.score += var2;
   }

   public boolean isShootable() {
      return true;
   }

   public void bindTexture(TextureManager var1) {
      if(newTexture != null) {
         newTextureId = var1.load(newTexture);
         newTexture = null;
      }

      int var2;
      if(newTextureId < 0) {
         var2 = var1.load("/char.png");
         GL11.glBindTexture(3553, var2);
      } else {
         var2 = newTextureId;
         GL11.glBindTexture(3553, var2);
      }
   }

   public void hurt(Entity var1, int var2) {
         super.hurt(var1, var2);

   }
   
   public boolean isCreativeModeAllowed() {
      return true;
   }
   
	public final float canHarvestBlock(Block var1) {
		Block var2 = var1;
		Inventory var4 = this.inventory;
		float var3 = 1.0F;
		if(var4.Inventory[var4.selected] != null) {
			ItemStack var5 = var4.Inventory[var4.selected];
			var3 = 1.0F * var5.getItem().getStrVsBlock(var2);
		}

		return var3;
	}
	   
	public final void drop(ItemStack var1) {
		if(var1 != null) {
			Item var6 = new Item(this.level, this.x, this.y - 0.3F, this.z, var1);
			var6.delayBeforeCanPickup = 40;
			var6.x = MathHelper.sin(this.rot / 180.0F * (float)Math.PI) * 0.2F;
			var6.z = -MathHelper.cos(this.rot / 180.0F * (float)Math.PI) * 0.2F;
			var6.y = 0.2F;
			float var7 = this.random.nextFloat() * (float)Math.PI * 2.0F;
			float var8 = this.random.nextFloat() * 0.1F;
			var6.x = (float)((double)var6.x + Math.cos((double)var7) * (double)var8);
			var6.y += (this.random.nextFloat() - this.random.nextFloat()) * 0.1F;
			var6.z = (float)((double)var6.z + Math.sin((double)var7) * (double)var8);
			this.level.releaseSkin(var6);
	    }
	}
	
	public void shootArrows(Player var0) {
		   for(int var2 = 0; var2 < this.arrows; ++var2) {
		      var0.level.addEntity(new Arrow(var0.level, var0.level.getPlayer(), var0.x, var0.y - 0.2F, var0.z, (float)Math.random() * 360.0F, -((float)Math.random()) * 60.0F, 0.4F));
		   }

	}
	
	  public void travel(float var1, float var2) {
	      float var3;
	      
	      if (this.minecraft.isFlying) {
	          this.fallDistance = 0.0F;
	          this.yd = 0.0F;
	          
	          this.moveRelative(var1, var2, this.onGround?0.1F:0.02F);
		      this.move(this.xd, this.yd, this.zd);
		      
	          if (Keyboard.isKeyDown(42) && Minecraft.currentScreen == null) {
	             this.yd = -0.25F;
	          }

	          if (Keyboard.isKeyDown(57) && Minecraft.currentScreen == null) {
	             this.yd = 0.25F;
	          }  
	           
	      }

	      if(this.isInWater() || this.isInCobWeb() && !this.minecraft.isFlying) {
	         var3 = this.y;
	         this.moveRelative(var1, var2, 0.02F);
	         this.move(this.xd, this.yd, this.zd);
	         this.xd *= 0.8F;
	         this.yd *= 0.8F;
	         this.zd *= 0.8F;
	         this.yd = (float)((double)this.yd - 0.02D);
	         if(this.horizontalCollision && this.isFree(this.xd, this.yd + 0.6F - this.y + var3, this.zd)) {
	            this.yd = 0.3F;
	         }

	      } else if(this.isInLava() && !this.minecraft.isFlying) {
	         var3 = this.y;
	         this.moveRelative(var1, var2, 0.02F);
	         this.move(this.xd, this.yd, this.zd);
	         this.xd *= 0.5F;
	         this.yd *= 0.5F;
	         this.zd *= 0.5F;
	         this.yd = (float)((double)this.yd - 0.02D);
	         if(this.horizontalCollision && this.isFree(this.xd, this.yd + 0.6F - this.y + var3, this.zd)) {
	            this.yd = 0.3F;
	      } else if (this.onIce() && !this.minecraft.isFlying) {
	         var3 = this.y;
	         this.moveRelative(var1, var2, 0.02F);
	         this.move(this.xd, this.yd, this.zd);
	         this.xd *= 0.10F;
	         this.yd *= 0.98F;
	         this.zd *= 0.10F;
	         this.yd = (float)((double)this.yd - 0.08D);
	      } else if (this.onSlime() && !this.minecraft.isFlying) {
	         var3 = this.y;
	         this.moveRelative(var1, var2, 0.02F);
	         this.move(this.xd, this.yd, this.zd);
	         this.xd *= 0.93F;
	         this.yd *= 0.98F;
	         this.zd *= 0.93F;
	         ++this.yd;
	      }

	      } else {
	         this.moveRelative(var1, var2, this.onGround?0.1F:0.02F);
	         this.move(this.xd, this.yd, this.zd);
	         this.xd *= 0.91F;
	         this.yd *= 0.98F;
	         this.zd *= 0.91F;
	         this.yd = (float)((double)this.yd - 0.08D);
	         if(this.onGround) {
	            var3 = 0.6F;
	            this.xd *= var3;
	            this.zd *= var3;
	         }

	      }
	   }

}

