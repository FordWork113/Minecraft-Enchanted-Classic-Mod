package com.mojang.minecraft.mob;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.tile.Block;
import com.mojang.minecraft.level.tile.slab.*;
import com.mojang.minecraft.mob.ai.AI;
import com.mojang.minecraft.mob.ai.BasicAI;
import com.mojang.minecraft.model.HumanoidModel;
import com.mojang.minecraft.model.ModelManager;
import com.mojang.minecraft.player.Player;
import com.mojang.minecraft.render.ShapeRenderer;
import com.mojang.minecraft.render.TextureManager;
import com.mojang.util.MathHelper;

import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.lwjgl.input.Keyboard;

public class Mob extends Entity {

   public static final long serialVersionUID = 0L;
   public static final int ATTACK_DURATION = 5;
   public static final int TOTAL_AIR_SUPPLY = 300;
   public static ModelManager modelCache;
   public int invulnerableDuration = 20;
   public float rot;
   public float timeOffs;
   public float speed;
   public float rotA = (float)(Math.random() + 1.0D) * 0.01F;
   public float yBodyRot = 0.0F;
   protected float yBodyRotO = 0.0F;
   protected float oRun;
   protected float run;
   protected float animStep;
   protected float animStepO;
   protected int tickCount = 0;
   public boolean hasHair = true;
   protected String textureName = "/char.png";
   public boolean allowAlpha = true;
   public float rotOffs = 0.0F;
   public String modelName = null;
   protected float bobStrength = 1.0F;
   protected int deathScore = 0;
   public float renderOffset = 0.0F;
   public int health = 20;
   public int lastHealth;
   public int invulnerableTime = 0;
   public int airSupply = 300;
   public int hurtTime;
   public int hurtDuration;
   public float hurtDir = 0.0F;
   public int deathTime = 0;
   public int attackTime = 0;
   public float oTilt;
   public float tilt;
   protected boolean dead = false;
   public AI ai;
   public HumanoidModel humanoidModel;
   Player mob;
   Entity entity;
   TextureManager textureManager;
   public int randomTextureId;
   public transient Random rand = new Random();
   protected float shadowSize = 0.65F;
   protected float shadowOpaque = 1.0F;
   private int livingSound;

   public Mob(Level var1) {
      super(var1);
      this.setPos(this.x, this.y, this.z);
      this.timeOffs = (float)Math.random() * 12398.0F;
      this.rot = (float)(Math.random() * 3.1415927410125732D * 2.0D);
      this.speed = 1.0F;
      this.ai = new BasicAI();
      this.footSize = 0.5F;
      this.randomTextureId = this.rand.nextInt(2);
   }

   public boolean isPickable() {
      return !this.removed;
   }

   public boolean isPushable() {
      return !this.removed;
   }

   public void tick() {
      super.tick();
      
      /*if (this.rand.nextInt(1000) < this.livingSound++) {
          this.livingSound = -80;
          String livingSound;
          if ((livingSound = this.getLivingSound()) != null) {
             this.level.playSound(livingSound, this, 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
          }
      }*/
      
      this.oTilt = this.tilt;
      if(this.attackTime > 0) {
         --this.attackTime;
      }

      if(this.hurtTime > 0) {
         --this.hurtTime;
      }

      if(this.invulnerableTime > 0) {
         --this.invulnerableTime;
      }

      if(this.health <= 0) {
         ++this.deathTime;
         if(this.deathTime > 20) {
            if(this.ai != null) {
               this.ai.beforeRemove();
            }

            this.remove();
         }
      }

      if(this.isUnderWater()) {
         if(this.airSupply > 0) {
            --this.airSupply;
         } else {
            this.hurt((Entity)null, 2);
         }
      } else {
         this.airSupply = 300;
      }

      if(this.isInWater() || this.isInCobWeb()) {
         this.fallDistance = 0.0F;
      }

      if(this.isInLava()) {
         this.hurt((Entity)null, 6);
      }

      if (this.onMagma()) {
          this.hurt((Entity)null, 2);
       }
      
      if (this.onCactus()) {
         this.hurt((Entity)null, 4);
      }

      this.animStepO = this.animStep;
      this.yBodyRotO = this.yBodyRot;
      this.yRotO = this.yRot;
      this.xRotO = this.xRot;
      ++this.tickCount;
      this.aiStep();
      float var1 = this.x - this.xo;
      float var2 = this.z - this.zo;
      float var3 = MathHelper.sqrt(var1 * var1 + var2 * var2);
      float var4 = this.yBodyRot;
      float var5 = 0.0F;
      this.oRun = this.run;
      float var6 = 0.0F;
      if(var3 > 0.05F) {
         var6 = 1.0F;
         var5 = var3 * 3.0F;
         var4 = (float)Math.atan2((double)var2, (double)var1) * 180.0F / 3.1415927F - 90.0F;
      }

      if(!this.onGround) {
         var6 = 0.0F;
      }

      this.run += (var6 - this.run) * 0.3F;

      for(var1 = var4 - this.yBodyRot; var1 < -180.0F; var1 += 360.0F) {
         ;
      }

      while(var1 >= 180.0F) {
         var1 -= 360.0F;
      }

      this.yBodyRot += var1 * 0.1F;

      for(var1 = this.yRot - this.yBodyRot; var1 < -180.0F; var1 += 360.0F) {
         ;
      }

      while(var1 >= 180.0F) {
         var1 -= 360.0F;
      }

      boolean var7 = var1 < -90.0F || var1 >= 90.0F;
      if(var1 < -75.0F) {
         var1 = -75.0F;
      }

      if(var1 >= 75.0F) {
         var1 = 75.0F;
      }

      this.yBodyRot = this.yRot - var1;
      this.yBodyRot += var1 * 0.1F;
      if(var7) {
         var5 = -var5;
      }

      while(this.yRot - this.yRotO < -180.0F) {
         this.yRotO -= 360.0F;
      }

      while(this.yRot - this.yRotO >= 180.0F) {
         this.yRotO += 360.0F;
      }

      while(this.yBodyRot - this.yBodyRotO < -180.0F) {
         this.yBodyRotO -= 360.0F;
      }

      while(this.yBodyRot - this.yBodyRotO >= 180.0F) {
         this.yBodyRotO += 360.0F;
      }

      while(this.xRot - this.xRotO < -180.0F) {
         this.xRotO -= 360.0F;
      }

      while(this.xRot - this.xRotO >= 180.0F) {
         this.xRotO += 360.0F;
      }

      this.animStep += var5;
   }

   public void aiStep() {
      if(this.ai != null) {
         this.ai.tick(this.level, this);
      }

   }

   protected void bindTexture(TextureManager var1) {
      this.textureId = var1.load(this.textureName);
      GL11.glBindTexture(3553, this.textureId);
   }
   
   public void render(TextureManager var1, float var2) {
      if(this.modelName != null) {
         float var3;
         if((var3 = (float)this.attackTime - var2) < 0.0F) {
            var3 = 0.0F;
         }

         while(this.yBodyRotO - this.yBodyRot < -180.0F) {
            this.yBodyRotO += 360.0F;
         }

         while(this.yBodyRotO - this.yBodyRot >= 180.0F) {
            this.yBodyRotO -= 360.0F;
         }

         while(this.xRotO - this.xRot < -180.0F) {
            this.xRotO += 360.0F;
         }

         while(this.xRotO - this.xRot >= 180.0F) {
            this.xRotO -= 360.0F;
         }

         while(this.yRotO - this.yRot < -180.0F) {
            this.yRotO += 360.0F;
         }

         while(this.yRotO - this.yRot >= 180.0F) {
            this.yRotO -= 360.0F;
         }

         float var4 = this.yBodyRotO + (this.yBodyRot - this.yBodyRotO) * var2;
         float var5 = this.oRun + (this.run - this.oRun) * var2;
         float var6 = this.yRotO + (this.yRot - this.yRotO) * var2;
         float var7 = this.xRotO + (this.xRot - this.xRotO) * var2;
         var6 -= var4;
         GL11.glPushMatrix();
         float var8 = this.animStepO + (this.animStep - this.animStepO) * var2;
         float var9;
         GL11.glColor3f(var9 = this.getBrightness(var2), var9, var9);
         var9 = 0.0625F;
         float var10 = -Math.abs(MathHelper.cos(var8 * 0.6662F)) * 5.0F * var5 * this.bobStrength - 23.0F;
         GL11.glTranslatef(this.xo + (this.x - this.xo) * var2, this.yo + (this.y - this.yo) * var2 - 1.62F + this.renderOffset, this.zo + (this.z - this.zo) * var2);
         float var11;
         if((var11 = (float)this.hurtTime - var2) > 0.0F || this.health <= 0) {
            if(var11 < 0.0F) {
               var11 = 0.0F;
            } else {
               var11 = MathHelper.sin((var11 /= (float)this.hurtDuration) * var11 * var11 * var11 * 3.1415927F) * 14.0F;
            }

            float var12 = 0.0F;
            if(this.health <= 0) {
               var12 = ((float)this.deathTime + var2) / 20.0F;
               if((var11 += var12 * var12 * 800.0F) > 90.0F) {
                  var11 = 90.0F;
               }
            }

            var12 = this.hurtDir;
            GL11.glRotatef(180.0F - var4 + this.rotOffs, 0.0F, 1.0F, 0.0F);
            GL11.glScalef(1.0F, 1.0F, 1.0F);
            GL11.glRotatef(-var12, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-var11, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(var12, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-(180.0F - var4 + this.rotOffs), 0.0F, 1.0F, 0.0F);
         }

         GL11.glTranslatef(0.0F, -var10 * var9, 0.0F);
         GL11.glScalef(1.0F, -1.0F, 1.0F);
         GL11.glRotatef(180.0F - var4 + this.rotOffs, 0.0F, 1.0F, 0.0F);
         if(!this.allowAlpha) {
            GL11.glDisable(3008);
         } else {
            GL11.glDisable(2884);
         }

         GL11.glScalef(-1.0F, 1.0F, 1.0F);
         modelCache.getModel(this.modelName).attackOffset = var3 / 5.0F;
         this.bindTexture(var1);
         this.renderModel(var1, var8, var2, var5, var6, var7, var9);
         if(this.invulnerableTime > this.invulnerableDuration - 10) {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.75F);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 1);
            this.bindTexture(var1);
            this.renderModel(var1, var8, var2, var5, var6, var7, var9);
            GL11.glDisable(3042);
            GL11.glBlendFunc(770, 771);
         }

         GL11.glEnable(3008);
         if(this.allowAlpha) {
            GL11.glEnable(2884);
         }

         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         GL11.glPopMatrix();
         
         if(this.level.rendererContext$5cd64a7f.settings.fancyGraphics && this.level.rendererContext$5cd64a7f.devMode) {
         this.renderShadow(this, this.xo + (this.x - this.xo) * var2, this.yo + (this.y - this.yo) * var2 - 0.7F, this.zo + (this.z - this.zo) * var2, var2);
         }
      }
   }
  
   public void renderModel(TextureManager var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      modelCache.getModel(this.modelName).render(var2, var4, (float)this.tickCount + var3, var5, var6, var7);
   }

   public void heal(int var1) {
      if(this.health > 0) {
         this.health += var1;
         if(this.health > 20) {
            this.health = 20;
         }

         this.invulnerableTime = this.invulnerableDuration / 2;
      }
   }
   
   protected String getLivingSound() {
	  return null;
   }

   protected String getHurtSound() {
	  return "random.hurt";
   }

   protected String getDeathSound() {
	  return "random.death";
   }

   public void hurt(Entity var1, int var2) {
         if(this.health > 0) {
            this.ai.hurt(var1, var2);
            if((float)this.invulnerableTime > (float)this.invulnerableDuration / 2.0F) {
               if(this.lastHealth - var2 >= this.health) {
                  return;
               }

               this.health = this.lastHealth - var2;
            } else {
               this.lastHealth = this.health;
               this.invulnerableTime = this.invulnerableDuration;
               this.health -= var2;
               this.hurtTime = this.hurtDuration = 10;
            }

            this.hurtDir = 0.0F;
            if(var1 != null) {
               float var3 = var1.x - this.x;
               float var4 = var1.z - this.z;
               this.hurtDir = (float)(Math.atan2((double)var4, (double)var3) * 180.0D / 3.1415927410125732D) - this.yRot;
               this.knockback(var1, var2, var3, var4);        
            } else {
               this.hurtDir = (float)((int)(Math.random() * 2.0D) * 180);
            }

            if(this.health <= 0) {
               //this.level.playSound(this.getDeathSound(), this, 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
               this.die(var1);
            } else if (this.dead) {
            	//this.level.playSound(this.getDeathSound(), this, 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
            } else {
            	//this.level.playSound(this.getHurtSound(), this, 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
            }
         
           //this.level.playSound(this.getHurtSound(), this, 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
         }
   }


   public void knockback(Entity var1, int var2, float var3, float var4) {
      float var5 = MathHelper.sqrt(var3 * var3 + var4 * var4);
      float var6 = 0.4F;
      this.xd /= 2.0F;
      this.yd /= 2.0F;
      this.zd /= 2.0F;
      this.xd -= var3 / var5 * var6;
      this.yd += 0.4F;
      this.zd -= var4 / var5 * var6;
      if(this.yd > 0.4F) {
         this.yd = 0.4F;
      }
   }

   public void die(Entity var1) {
	   if(this.level.rendererContext$5cd64a7f.gamemode.isCreative()) {
         if(this.deathScore > 0 && var1 != null) {
            var1.awardKillScore(this, this.deathScore);
         }

	   }
      
      this.dead = true;
   }

   protected void causeFallDamage(float var1) {
         int var2;
         if((var2 = (int)Math.ceil((double)(var1 - 3.0F))) > 0) {
            this.hurt((Entity)null, var2);
         }
   }

   public void travel(float var1, float var2) {
      float var3;
      
      if(this.isInWater() || this.isInCobWeb()) {
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

      } else if(this.isInLava()) {
         var3 = this.y;
         this.moveRelative(var1, var2, 0.02F);
         this.move(this.xd, this.yd, this.zd);
         this.xd *= 0.5F;
         this.yd *= 0.5F;
         this.zd *= 0.5F;
         this.yd = (float)((double)this.yd - 0.02D);
         if(this.horizontalCollision && this.isFree(this.xd, this.yd + 0.6F - this.y + var3, this.zd)) {
            this.yd = 0.3F;
      } else if (this.onIce()) {
             var3 = this.y;
             this.moveRelative(var1, var2, 0.02F);
             this.move(this.xd, this.yd, this.zd);
             this.xd *= 0.10F;
             this.yd *= 0.98F;
             this.zd *= 0.10F;
             this.yd = (float)((double)this.yd - 0.08D);
      } else if (this.onSlime()) {
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

   public boolean isShootable() {
      return true;
   }
   
   public void setSize(float var1, float var2) {
	  super.setSize(var1, var2);
   }
  
   public final void renderShadow(Entity entity, float float2, float float3, float float4, float float5) {
	      if (this.shadowSize > 0.0F && !this.isUnderWater() && !this.isInWater() && !this.isInCobWeb() && this.size == 1) {
	         float5 = entity.level.rendererContext$5cd64a7f.levelRenderer.getDistanceToCamera(float2, float3, float4);
	         if ((float5 = (1.0F - float5 / 256.0F) * this.shadowOpaque) > 0.0F) {
	            float n = float5;
	            float n2 = float4;
	            float n3 = float3;
	            GL11.glEnable(3042);
	            GL11.glBindTexture(3553, entity.level.rendererContext$5cd64a7f.textureManager.load("/shadow.png"));
	            Level worldObj = entity.level.rendererContext$5cd64a7f.levelRenderer.level;
	            GL11.glDepthMask(false);
	            float shadowSize = this.shadowSize;

	            for(int i = (int)(float2 - shadowSize); i <= (int)(float2 + shadowSize); ++i) {
	               for(int j = (int)(n3 - 2.0F); j <= (int)n3; ++j) {
	                  for(int k = (int)(n2 - shadowSize); k <= (int)(n2 + shadowSize); ++k) {
	                     int blockId;
	                     if ((blockId = worldObj.getTile(i, j - 1, k)) > 0) {
	                        Block block = Block.blocks[blockId];
	                        ShapeRenderer instance = ShapeRenderer.instance;
	                        float n15;
	                        if ((n15 = (n - (n3 - (float)j) / 2.0F) * 0.5F) >= 0.0F) {
	                           GL11.glColor4f(1.0F, 1.0F, 1.0F, n15);
	                           instance.begin();
	                           n15 = (float)i + block.x1;
	                           float n13 = (float)i + block.x2;
	                           float n16 = (float)j + block.y1;
	                           if (block instanceof SlabBlock && !((SlabBlock)block).doubleSlab) {
	                              n16 = (float)j + (block.y1 - 0.5F);
	                           } else if (block instanceof DirtSlabBlock) {
		                          n16 = (float)j + (block.y1 - 0.5F);
		                       } else if (block instanceof SandStoneSlabBlock) {
		                          n16 = (float)j + (block.y1 - 0.5F);
		                       } else if (block instanceof BrickSlabBlock) {
		                          n16 = (float)j + (block.y1 - 0.5F);
		                       } else if (block instanceof MossyCobbleSlabBlock) {
		                          n16 = (float)j + (block.y1 - 0.5F);
		                       } else if (block instanceof StoneSlabBlock) {
		                          n16 = (float)j + (block.y1 - 0.5F);
		                       } else if (block instanceof StoneBrickSlabBlock) {
		                          n16 = (float)j + (block.y1 - 0.5F);
		                       } else if (block instanceof WoodSlabBlock) {
		                          n16 = (float)j + (block.y1 - 0.5F);
		                       }          
	                           
	                           float n17 = (float)k + block.z1;
	                           float n18 = (float)k + block.z2;
	                           float n19 = (float2 - n15) / 4.0F / shadowSize + 0.5F;
	                           float n14 = (float2 - n13) / 4.0F / shadowSize + 0.5F;
	                           float n11 = (n2 - n17) / 4.0F / shadowSize + 0.5F;
	                           float n12 = (n2 - n18) / 4.0F / shadowSize + 0.5F;
	                           instance.vertexUV(n15, n16, n17, n19, n11);
	                           instance.vertexUV(n15, n16, n18, n19, n12);
	                           instance.vertexUV(n13, n16, n18, n14, n12);
	                           instance.vertexUV(n13, n16, n17, n14, n11);
	                           instance.end();
	                        }
	                     }
	                  }
	               }
	            }

	            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	            GL11.glDisable(3042);
	            GL11.glDepthMask(true);
	         }
	      }

	   }

}
