package com.mojang.minecraft.level;

import com.mojang.minecraft.Minecraft;
import com.mojang.minecraft.Timer;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.player.Player;
import com.mojang.minecraft.render.Renderer;
import com.mojang.minecraft.render.ShapeRenderer;
import com.mojang.minecraft.render.TextureManager;
import com.mojang.minecraft.particle.WaterDropParticle;
import com.mojang.util.MathHelper;

import java.io.Serializable;
import java.util.Random;

import org.lwjgl.opengl.GL11;

public class Weather implements Serializable {
	   public static final long serialVersionUID = 0L;
	   public transient Random random = new Random();
       public long weatherStart = System.currentTimeMillis() + 120000L;
	   public String rain = "/environment/rain.png";
	   public String snow = "/environment/snow.png";
	   
	   public Weather(Minecraft var1, TextureManager var2, Level var3) {
		   this.render(var1, var2);
		   this.changeSkyColor(var1, var3);
	   }   
	   
	   public final void render(Minecraft var1, TextureManager var2) {
		   float var114 = var1.timer.delta;
           int var19;
           int var58;
           int var59;
           int var60;
           float var38 = 0.0F;
           Player var25;
           Renderer var16 = var1.renderer;
           float var26 = (var25 = var16.minecraft.player).xRotO + (var25.xRot - var25.xRotO) * var114;
           float var31 = MathHelper.cos(-var26 * 0.017453292F);
           float var116;
           Level var118;
           ShapeRenderer var119;
           float var122;
           float var124;
           Renderer var23 = var16;
           Renderer var24;
           var116 = var114;
           var24 = var23;
           var25 = var23.minecraft.player;
           var118 = var23.minecraft.level;
           int var53 = (int)var25.x;
           int var115 = (int)var25.y;
           int var57 = (int)var25.z;
           var119 = ShapeRenderer.instance;
           GL11.glDisable(2884);
           GL11.glNormal3f(0.0F, 1.0F, 0.0F);
           GL11.glEnable(3042);
           GL11.glBlendFunc(770, 771);
           
  		   if(var1.raining && var118.levelType != 2) {
              GL11.glBindTexture(3553, var2.load(this.rain));
		   } else if(var1.snowing || var118.levelType == 2) {
			 GL11.glBindTexture(3553, var2.load(this.snow));
		   }
           int var55 = var53 - 5;

           while(true) {
              if (var55 > var53 + 5) {
                 GL11.glEnable(2884);
                 GL11.glDisable(3042);
                 break;
              }

              for(var59 = var57 - 5; var59 <= var57 + 5; ++var59) {
                 var60 = var118.getHighestTile(var55, var59);
                 var19 = var115 - 5;
                 var58 = var115 + 5;
                 if (var19 < var60) {
                    var19 = var60;
                 }

                 if (var58 < var60) {
                    var58 = var60;
                 }

                 if (var19 != var58) {
                	if(var1.raining) {
                    var31 = ((float)((var24.levelTicks + var55 * 3121 + var59 * 418711) % 32) + var116) / 32.0F;
                	} if(var1.snowing || var118.levelType == 2) {
                	var31 = ((float)((var24.levelTicks + var55 * 3121 + var59 * 418711) % 32) + var116) / 512.0F;
                	}
                	
                    var122 = (float)var55 + 0.5F - var25.x;
                    var38 = (float)var59 + 0.5F - var25.z;
                    var124 = MathHelper.sqrt(var122 * var122 + var38 * var38) / 5.0F;
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, (1.0F - var124 * var124) * 0.7F);
                    var119.begin();
                    var119.vertexUV((float)var55, (float)var19, (float)var59, 0.0F, (float)var19 * 2.0F / 8.0F + var31 * 2.0F);
                    var119.vertexUV((float)(var55 + 1), (float)var19, (float)(var59 + 1), 2.0F, (float)var19 * 2.0F / 8.0F + var31 * 2.0F);
                    var119.vertexUV((float)(var55 + 1), (float)var58, (float)(var59 + 1), 2.0F, (float)var58 * 2.0F / 8.0F + var31 * 2.0F);
                    var119.vertexUV((float)var55, (float)var58, (float)var59, 0.0F, (float)var58 * 2.0F / 8.0F + var31 * 2.0F);
                    var119.vertexUV((float)var55, (float)var19, (float)(var59 + 1), 0.0F, (float)var19 * 2.0F / 8.0F + var31 * 2.0F);
                    var119.vertexUV((float)(var55 + 1), (float)var19, (float)var59, 2.0F, (float)var19 * 2.0F / 8.0F + var31 * 2.0F);
                    var119.vertexUV((float)(var55 + 1), (float)var58, (float)var59, 2.0F, (float)var58 * 2.0F / 8.0F + var31 * 2.0F);
                    var119.vertexUV((float)var55, (float)var58, (float)(var59 + 1), 0.0F, (float)var58 * 2.0F / 8.0F + var31 * 2.0F);
                    var119.end();
                 }
              }

              ++var55;
           }
	   }

	   public void changeSkyColor(Minecraft var1, Level var2) {
		 if(var1.raining) {
			 var2.skyColor = 7699847;
			 var2.fogColor = 5069403;
			 var2.cloudColor = 5069403;
		 } else if(var1.snowing || var2.levelType == 2) {
	  	     var2.fogColor = 13033215;
	  	     var2.skyColor = 16777215;
	  	     var2.cloudColor = 15658751;
		 }

       
		 
	   }
}
