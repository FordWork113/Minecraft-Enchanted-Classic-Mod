package com.mojang.minecraft.level.item;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.mob.Mob;
import com.mojang.minecraft.particle.TerrainParticle;
import com.mojang.minecraft.gui.ChatInputScreen;
import com.mojang.minecraft.gui.FontRenderer;
import com.mojang.minecraft.item.ItemStack;
import com.mojang.minecraft.item.Items;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.tile.Block;
import com.mojang.minecraft.player.Player;
import com.mojang.minecraft.render.TextureManager;
import com.mojang.util.MathHelper;
import com.mojang.minecraft.Minecraft;

import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class Sign extends Entity {
   public static final long serialVersionUID = 0L;
   private static SignModel model = new SignModel();
   private float xd;
   private float yd;
   private float zd;
   private float rot;
   public String[] messages = new String[]{"This is a test", "of the signs.", "Each line can", "be 15 chars!"};
   public int selectedLine = -1;
   public Random random = new Random();

   public Sign(Level level, float x, float y, float z, float rot, String[] messages) {
	  super(level);
	  this.setSize(0.5F, 1.5F);
	  this.heightOffset = this.bbHeight / 2.0F;
	  this.setPos(x, y, z);
	  this.rot = -rot;
	  this.heightOffset = 1.5F;
	  this.xd = -((float)Math.sin((double)this.rot * 3.141592653589793D / 180.0D)) * 0.05F;
	  this.yd = 0.2F;
	  this.zd = -((float)Math.cos((double)this.rot * 3.141592653589793D / 180.0D)) * 0.05F;
	  this.makeStepSound = false;
	  this.messages = messages;
   }

   public boolean isPickable() {
      return !this.removed;
   }

   public void tick() {
	  this.xo = this.x;
	  this.yo = this.y;
	  this.zo = this.z;
      this.yd -= 0.04F;
      this.move(this.xd, this.yd, this.zd);
      this.xd *= 0.98F;
      this.yd *= 0.98F;
      this.zd *= 0.98F;
      if (this.onGround) {
         this.xd *= 0.7F;
         this.zd *= 0.7F;
         this.yd *= -0.5F;
      }

   }
   
   public void hurt(Entity entity, int damage) {
	    if (entity != null) {
	        this.level.playSound("step.wood", entity, 1.0F, 0.8F);
	    }
	      
		if(!removed)
		{
			super.hurt(entity, damage);
			
			if(entity instanceof Player) {
								
				Item item = new Item(this.level, this.x, this.y, this.z, new ItemStack(Items.sign.shiftedIndex));
				item.delayBeforeCanPickup = 20;
			    this.level.releaseSkin(item);
				
		        if (level.rendererContext$5cd64a7f.settings.particles) {
			    for(int var21 = 0; var21 < 64; ++var21) {
			          float var3 = (float)this.random.nextGaussian() / 4.0F;
			          float var4 = (float)this.random.nextGaussian() / 4.0F;
			          float var5 = (float)this.random.nextGaussian() / 4.0F;
			          float var6 = MathHelper.sqrt(var3 * var3 + var4 * var4 + var5 * var5);
			          float var7 = var3 / var6 / var6;
			          float var8 = var4 / var6 / var6;
			          var6 = var5 / var6 / var6;
			          this.level.particleEngine.spawnParticle(new TerrainParticle(this.level, this.x + var3, this.y + var4, this.z + var5, var7, var8, var6, Block.WOOD));
			     }
		        }
				
				remove();
			}

		}
		
	}

	
   public void render(TextureManager textureManager, float translation) {
      GL11.glEnable(3553);
      textureId = textureManager.load("/item/sign.png");
	  GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
      float brightness = level.getBrightness((int)x, (int)y, (int)z);
      GL11.glPushMatrix();
      GL11.glColor4f(brightness, brightness, brightness, 1.0F);
      GL11.glTranslatef(xo + (x - xo) * translation, this.yo + (this.y - this.yo) * translation - this.heightOffset / 2.0F, this.zo + (this.z - this.zo) * translation);
      GL11.glRotatef(this.rot, 0.0F, 1.0F, 0.0F);
      GL11.glPushMatrix();
      GL11.glScalef(1.0F, -1.0F, -1.0F);
      SignModel var5 = model;
      model.signBoard.render(0.0625F);
      var5.signStick.render(0.0625F);
      GL11.glPopMatrix();
      float f5 = 0.016666668F;
      GL11.glTranslatef(0.0F, 0.5F, 0.09F);
      GL11.glScalef(f5, -f5, f5);
      GL11.glNormal3f(0.0F, 0.0F, -1.0F * f5);
      GL11.glEnable(3042);
      FontRenderer var8 = this.level.font;
   

      for(int var7 = 0; var7 < this.messages.length; ++var7) {
    	 String var6 = this.messages[var7];
         var8.renderNoShadow(var6, -var8.getWidth(var6) / 2, var7 * 10 - this.messages.length * 5, 2105376);
      }

      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glPopMatrix();
   }
}