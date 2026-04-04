package com.mojang.minecraft.level.item;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.item.ItemStack;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.liquid.LiquidType;
import com.mojang.minecraft.level.tile.Block;
import com.mojang.minecraft.player.Player;
import com.mojang.minecraft.render.ShapeRenderer;
import com.mojang.minecraft.render.TextureManager;
import com.mojang.util.MathHelper;
import java.util.Random;
import org.lwjgl.opengl.GL11;

public class Item extends Entity {
   public ItemStack item;
   private float playerViewY;
   public Entity entity;
   public transient Random random = new Random();
   public float hoverStart = (float)(Math.random() * Math.PI * 2.0D);
   public static final long serialVersionUID = 0L;
   private float xd;
   private float yd;
   private float zd;
   private float rot;
   private int Item;
   private int tickCount;
   private int age = 0;
   public int delayBeforeCanPickup;

   public Item(Level level, float x, float y, float z, ItemStack items) {
		super(level);

		setSize(0.25F, 0.25F);

		heightOffset = bbHeight / 2.0F;

		setPos(x, y, z);

		item = items;

		rot = (float)(Math.random() * 360.0D);

		xd = (float)(Math.random() * 0.20000000298023224D - 0.10000000149011612D);
		yd = 0.2F;
		zd = (float)(Math.random() * 0.20000000298023224D - 0.10000000149011612D);

		makeStepSound = false;
   }

   public void tick() {
      if (this.delayBeforeCanPickup > 0) {
         --this.delayBeforeCanPickup;
      }

      this.xo = this.x;
      this.yo = this.y;
      this.zo = this.z;
      this.yd -= 0.04F;

		float var4 = this.zd;
		float var3 = this.yd;
		float var2 = this.xd;
		int var5 = (int)var2;
		int var6 = (int)var3;
		int var7 = (int)var4;
		var2 -= (float)var5;
		var3 -= (float)var6;
		var4 -= (float)var7;
		
		if(Block.opaque[this.level.getTile(var5, var6, var7)]) {
			boolean var8 = !Block.opaque[this.level.getTile(var5 - 1, var6, var7)];
			boolean var9 = !Block.opaque[this.level.getTile(var5 + 1, var6, var7)];
			boolean var10 = !Block.opaque[this.level.getTile(var5, var6 - 1, var7)];
			boolean var11 = !Block.opaque[this.level.getTile(var5, var6 + 1, var7)];
			boolean var12 = !Block.opaque[this.level.getTile(var5, var6, var7 - 1)];
			boolean var13 = !Block.opaque[this.level.getTile(var5, var6, var7 + 1)];
			byte var14 = -1;
			float var15 = 9999.0F;
			if(var8 && var2 < 9999.0F) {
				var15 = var2;
				var14 = 0;
			}

			if(var9 && 1.0F - var2 < var15) {
				var15 = 1.0F - var2;
				var14 = 1;
			}

			if(var10 && var3 < var15) {
				var15 = var3;
				var14 = 2;
			}

			if(var11 && 1.0F - var3 < var15) {
				var15 = 1.0F - var3;
				var14 = 3;
			}

			if(var12 && var4 < var15) {
				var15 = var4;
				var14 = 4;
			}

			if(var13 && 1.0F - var4 < var15) {
				var14 = 5;
			}

			var2 = this.rand.nextFloat() * 0.2F + 0.1F;
			if(var14 == 0) {
				this.xd = -var2;
			}

			if(var14 == 1) {
				this.xd = var2;
			}

			if(var14 == 2) {
				this.xd = -var2;
			}

			if(var14 == 3) {
				this.yd = var2;
			}

			if(var14 == 4) {
				this.zd = -var2;
			}

			if(var14 == 5) {
				this.zd = var2;
			}
		}
		
      this.move(this.xd, this.yd, this.zd);
      this.xd *= 0.98F;
      this.yd *= 0.98F;
      this.zd *= 0.98F;
      if (this.onGround) {
         this.xd *= 0.7F;
         this.zd *= 0.7F;
         this.yd *= -0.5F;
      }

      ++this.tickCount;
      ++this.age;
      if (this.age >= 6000) {
         this.remove();
      }

   }

   public void renderItem(Entity var1, TextureManager var2, float var3, float var4, float var5, float var6, float var7) {
	      Item var8 = (Item)var1;
	      ItemStack var9 = var8.item;
	      GL11.glPushMatrix();
	      float var10 = MathHelper.sin(((float)var8.age + var7) / 10.0F + var8.hoverStart) * 0.1F + 0.1F;
	      //var7 = (((float)var8.age + var7) / 20.0F + var8.hoverStart) * (180.0F / (float)Math.PI);
	      var7 = rot + ((float)tickCount + var7) * 3.0F;
	      byte var11 = 1;
	      if (var8.item.stackSize > 1) {
	         var11 = 2;
	      }

	      if (var8.item.stackSize > 5) {
	         var11 = 3;
	      }

	      if (var8.item.stackSize > 20) {
	         var11 = 4;
	      }

	      GL11.glTranslatef(var3, var4 + var10, var5);
	      GL11.glEnable(2977);
	      ShapeRenderer var12 = ShapeRenderer.instance;
	      float var14;
	      float var15;
	      int var16;      
	      if (var9.itemID < 256) {
	         GL11.glEnable(2896);
	         GL11.glPushMatrix();
	         GL11.glScalef(0.25F, 0.25F, 0.25F);
	         GL11.glRotatef(var7, 0.0F, 1.0F, 0.0F);
	         this.textureId = var2.load("/terrain.png");
	         GL11.glBindTexture(3553, this.textureId);
	         Block var13 = Block.blocks[var9.itemID];
	         var13.renderFullbright(var12);

	         for(var16 = 0; var16 < var11; ++var16) {
	            if (var16 > 0) {
	               var5 = (var8.random.nextFloat() * 2.0F - 1.0F) * 0.2F / var3;
	               var6 = (var8.random.nextFloat() * 2.0F - 1.0F) * 0.2F / var3;
	               var7 = (var8.random.nextFloat() * 2.0F - 1.0F) * 0.2F / var3;
	               GL11.glTranslatef(var5, var6, var7);
	            }
	         }
	         
	         GL11.glPopMatrix();
	         GL11.glDisable(2896);
	      } else {
	         GL11.glScalef(0.5F, 0.5F, 0.5F);
	         this.textureId = var2.load("/items.png");
	         GL11.glBindTexture(3553, this.textureId);
	         var16 = var9.getItem().getIconIndex();
	         var14 = (float)(var16 % 16 << 4) / 256.0F;
	         var3 = (float)((var16 % 16 << 4) + 16) / 256.0F;
	         var4 = (float)(var16 / 16 << 4) / 256.0F;
	         var15 = (float)((var16 / 16 << 4) + 16) / 256.0F;
	         GL11.glRotatef(-this.playerViewY, 0.0F, 1.0F, 0.0F);
	         var12.begin();
	         var12.color(1.0F, 1.0F, 1.0F);
	         var12.normal(0.0F, 1.0F, 0.0F);
	         var12.vertexUV(-0.5F, -0.25F, 0.0F, var14, var15);
	         var12.vertexUV(0.5F, -0.25F, 0.0F, var3, var15);
	         var12.vertexUV(0.5F, 0.75F, 0.0F, var3, var4);
	         var12.vertexUV(-0.5F, 0.75F, 0.0F, var14, var4);
	         var12.end();
	         var12.end();
	      }

	      GL11.glDisable(2977);
	      GL11.glPopMatrix();
	   }

   public void PlayerTouch(Entity var1) {
      Player var2 = (Player)var1;
      if (var2.addResource(this.item)) {
    	 //this.level.playSound("random.pop", var2.x, var2.y, var2.z, 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
         TakeEntityAnim var3 = new TakeEntityAnim(this.level, this, var2);
         this.level.addEntity(var3);
         this.remove();
      }

   }

   public final void setPlayerViewY(float var1) {
      Player var2 = (Player)this.level.getPlayer();
      this.playerViewY = var2.yRot + (var2.yRot - var2.yRotO) * var1;
   }
}
