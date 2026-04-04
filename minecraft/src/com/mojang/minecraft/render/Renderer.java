package com.mojang.minecraft.render;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.Minecraft;
import com.mojang.minecraft.gui.ScaledResolution;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.liquid.LiquidType;
import com.mojang.minecraft.level.tile.Block;
import com.mojang.minecraft.model.Vec3D;
import com.mojang.minecraft.player.Player;
import com.mojang.minecraft.render.ItemRenderer;
import com.mojang.util.MathHelper;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.text.DecimalFormat;
import java.util.Random;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public final class Renderer {

   public Minecraft minecraft;
   public float fogColorMultiplier = 1.0F;
   public boolean displayActive = false;
   public float fogEnd = 0.0F;
   public ItemRenderer itemRenderer;
   public int levelTicks;
   public Entity entity = null;
   public Random random = new Random();
   private volatile int unused1 = 0;
   private volatile int unused2 = 0;
   private FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
   public float fogRed;
   public float fogBlue;
   public float fogGreen;
   public float playerViewY;
   private ByteBuffer entityByteBuffer;
   private FloatBuffer entityFloatBuffer = BufferUtils.createFloatBuffer(16);
   private DecimalFormat entityDecimalFormat = new DecimalFormat("0000");

   public Renderer(Minecraft var1) {
      this.minecraft = var1;
      this.itemRenderer = new ItemRenderer(var1);
   }

   public Vec3D getPlayerVector(float var1) {
      Player var4;
      float var2 = (var4 = this.minecraft.player).xo + (var4.x - var4.xo) * var1;
      float var3 = var4.yo + (var4.y - var4.yo) * var1;
      float var5 = var4.zo + (var4.z - var4.zo) * var1;
      return new Vec3D(var2, var3, var5);
   }

   public void hurtEffect(float var1) {
      Player var3;
      float var2 = (float)(var3 = this.minecraft.player).hurtTime - var1;
      if(var3.health <= 0) {
         var1 += (float)var3.deathTime;
         GL11.glRotatef(40.0F - 8000.0F / (var1 + 200.0F), 0.0F, 0.0F, 1.0F);
      }

      if(var2 >= 0.0F) {
         var2 = MathHelper.sin((var2 /= (float)var3.hurtDuration) * var2 * var2 * var2 * 3.1415927F);
         var1 = var3.hurtDir;
         GL11.glRotatef(-var3.hurtDir, 0.0F, 1.0F, 0.0F);
         GL11.glRotatef(-var2 * 14.0F, 0.0F, 0.0F, 1.0F);
         GL11.glRotatef(var1, 0.0F, 1.0F, 0.0F);
      }
   }

   public void applyBobbing(float var1) {
      Player var4;
      float var2 = (var4 = this.minecraft.player).walkDist - var4.walkDistO;
      var2 = var4.walkDist + var2 * var1;
      float var3 = var4.oBob + (var4.bob - var4.oBob) * var1;
      float var5 = var4.oTilt + (var4.tilt - var4.oTilt) * var1;
      GL11.glTranslatef(MathHelper.sin(var2 * 3.1415927F) * var3 * 0.5F, -Math.abs(MathHelper.cos(var2 * 3.1415927F) * var3), 0.0F);
      GL11.glRotatef(MathHelper.sin(var2 * 3.1415927F) * var3 * 3.0F, 0.0F, 0.0F, 1.0F);
      GL11.glRotatef(Math.abs(MathHelper.cos(var2 * 3.1415927F + 0.2F) * var3) * 5.0F, 1.0F, 0.0F, 0.0F);
      GL11.glRotatef(var5, 1.0F, 0.0F, 0.0F);
   }

   public final void setLighting(boolean var1) {
      if(!var1) {
         GL11.glDisable(2896);
         GL11.glDisable(16384);
      } else {
         GL11.glEnable(2896);
         GL11.glEnable(16384);
         GL11.glEnable(2903);
         GL11.glColorMaterial(1032, 5634);
         float var4 = 0.7F;
         float var2 = 0.3F;
         Vec3D var3 = (new Vec3D(0.0F, -1.0F, 0.5F)).normalize();
         GL11.glLight(16384, 4611, this.createBuffer(var3.x, var3.y, var3.z, 0.0F));
         GL11.glLight(16384, 4609, this.createBuffer(var2, var2, var2, 1.0F));
         GL11.glLight(16384, 4608, this.createBuffer(0.0F, 0.0F, 0.0F, 1.0F));
         GL11.glLightModel(2899, this.createBuffer(var4, var4, var4, 1.0F));
      }
   }

   public final void enableGuiMode() {
       ScaledResolution var69 = new ScaledResolution(this.minecraft.width, this.minecraft.height);
       int var1 = var69.getScaledWidth();
       int var2 = var69.getScaledHeight();

      if(this.minecraft.enableShader) {
         Shaders.instance.processScene(this.minecraft, this.fogEnd, this.fogRed, this.fogBlue, this.fogGreen);
         Shaders.instance.useProgram(Shaders.instance.baseProgram);
         Shaders.copyDepthTexture(Shaders.instance.depthTextureId(this.minecraft), this.minecraft);
      }

      GL11.glClear(256);
      GL11.glMatrixMode(5889);
      GL11.glLoadIdentity();
      GL11.glOrtho(0.0D, (double)var1, (double)var2, 0.0D, 100.0D, 300.0D);
      GL11.glMatrixMode(5888);
      GL11.glLoadIdentity();

      if(this.minecraft.enableShader) {
         Shaders.copyDepthTexture(Shaders.instance.depthTexture2Id(this.minecraft), this.minecraft);
         Shaders.instance.useProgram(0);
      }

      GL11.glTranslatef(0.0F, 0.0F, -200.0F);
   }

   public void updateFog() {
      Level var1 = this.minecraft.level;
      Player var2 = this.minecraft.player;
      GL11.glFog(2918, this.createBuffer(this.fogRed, this.fogBlue, this.fogGreen, 1.0F));
      GL11.glNormal3f(0.0F, -1.0F, 0.0F);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      Block var5;
      if((var5 = Block.blocks[var1.getTile((int)var2.x, (int)(var2.y + 0.12F), (int)var2.z)]) != null && var5.getLiquidType() != LiquidType.NOT_LIQUID) {
         LiquidType var6 = var5.getLiquidType();
         GL11.glFogi(2917, 2048);
         float var3;
         float var4;
         float var7;
         float var8;
         if(var6 == LiquidType.WATER) {
            GL11.glFogf(2914, 0.1F);
            var7 = 0.4F;
            var8 = 0.4F;
            var3 = 0.9F;
            if(this.minecraft.settings.anaglyph) {
               var4 = (var7 * 30.0F + var8 * 59.0F + var3 * 11.0F) / 100.0F;
               var8 = (var7 * 30.0F + var8 * 70.0F) / 100.0F;
               var3 = (var7 * 30.0F + var3 * 70.0F) / 100.0F;
               var7 = var4;
               var8 = var8;
               var3 = var3;
            }

            GL11.glLightModel(2899, this.createBuffer(var7, var8, var3, 1.0F));
         } else if(var6 == LiquidType.LAVA) {
            GL11.glFogf(2914, 2.0F);
            var7 = 0.4F;
            var8 = 0.3F;
            var3 = 0.3F;
            if(this.minecraft.settings.anaglyph) {
               var4 = (var7 * 30.0F + var8 * 59.0F + var3 * 11.0F) / 100.0F;
               var8 = (var7 * 30.0F + var8 * 70.0F) / 100.0F;
               var3 = (var7 * 30.0F + var3 * 70.0F) / 100.0F;
               var7 = var4;
               var8 = var8;
               var3 = var3;
            }

            GL11.glLightModel(2899, this.createBuffer(var7, var8, var3, 1.0F));
         }
      } else {
         GL11.glFogi(2917, 9729);
         GL11.glFogf(2915, 0.0F);
         GL11.glFogf(2916, this.fogEnd);
         GL11.glLightModel(2899, this.createBuffer(1.0F, 1.0F, 1.0F, 1.0F));
      }

      GL11.glEnable(2903);
      GL11.glColorMaterial(1028, 4608);
   }

   private FloatBuffer createBuffer(float var1, float var2, float var3, float var4) {
      this.buffer.clear();
      this.buffer.put(var1).put(var2).put(var3).put(var4);
      this.buffer.flip();
      return this.buffer;
   }
   
   public final void grabIsometricScreenshot() {
	      this.minecraft.progressBar.setTitle("Grabbing isometric screenshot");
	      File var1 = new File("screenshots");
	      var1.mkdir();

	      int var2;
	      File var3;
	      for(var2 = 0; (var3 = new File(var1, "mc_map_" + this.entityDecimalFormat.format((long)var2) + ".png")).exists(); ++var2) {
	      }

	      var3 = var3.getAbsoluteFile();

	      try {
	         int var19 = (this.minecraft.level.width << 4) + (this.minecraft.level.height << 4);
	         var2 = (this.minecraft.level.depth << 4) + var19 / 2;
	         BufferedImage var4;
	         Graphics var5 = (var4 = new BufferedImage(var19, var2, 1)).getGraphics();
	         int var6 = this.minecraft.width;
	         int var7 = this.minecraft.height;
	         int var8 = (var19 / var6 + 1) * (var2 / var7 + 1);
	         int var9 = 0;

	         for(int var10 = 0; var10 < var19; var10 += var6) {
	            for(int var11 = 0; var11 < var2; var11 += var7) {
	               ++var9;
	               this.minecraft.progressBar.setProgress(var9 * 100 / var8);
	               int var10001 = var10 - var19 / 2;
	               int var10002 = var11 - var2 / 2;
	               if (this.entityByteBuffer == null) {
	                  this.entityByteBuffer = BufferUtils.createByteBuffer(this.minecraft.width * this.minecraft.height << 2);
	               }

	               Player var15 = this.minecraft.player;
	               Level var16 = this.minecraft.level;
	               LevelRenderer var17 = this.minecraft.levelRenderer;
	               GL11.glViewport(0, 0, this.minecraft.width, this.minecraft.height);
	               this.updateFog();
	               GL11.glClear(16640);
	               this.fogColorMultiplier = 1.0F;
	               GL11.glEnable(2884);
	               this.fogEnd = (float)(512 >> (this.minecraft.settings.viewDistance << 1));
	               GL11.glMatrixMode(5889);
	               GL11.glLoadIdentity();
	               GL11.glOrtho(0.0D, (double)this.minecraft.width, 0.0D, (double)this.minecraft.height, 10.0D, 10000.0D);
	               GL11.glMatrixMode(5888);
	               GL11.glLoadIdentity();
	               GL11.glTranslatef((float)(-var10001), (float)(-var10002), -5000.0F);
	               GL11.glScalef(16.0F, -16.0F, -16.0F);
	               this.entityFloatBuffer.clear();
	               this.entityFloatBuffer.put(1.0F).put(-0.5F).put(0.0F).put(0.0F);
	               this.entityFloatBuffer.put(0.0F).put(1.0F).put(-1.0F).put(0.0F);
	               this.entityFloatBuffer.put(1.0F).put(0.5F).put(0.0F).put(0.0F);
	               this.entityFloatBuffer.put(0.0F).put(0.0F).put(0.0F).put(1.0F);
	               this.entityFloatBuffer.flip();
	               GL11.glMultMatrix(this.entityFloatBuffer);
	               GL11.glRotatef(0.0F, 0.0F, 1.0F, 0.0F);
	               GL11.glTranslatef((float)(-var16.width) / 2.0F, (float)(-var16.depth) / 2.0F, (float)(-var16.height) / 2.0F);
	               Frustrum var24 = FrustrumImpl.update();
	               this.minecraft.levelRenderer.clipRenderersByFrustrum(var24);
	               this.minecraft.levelRenderer.updateRenderers(var15);
	               this.updateFog();
	               GL11.glEnable(2912);
	               GL11.glFogi(2917, 9729);
	               float var23 = (float)var16.depth * 8.0F;
	               GL11.glFogf(2915, 5000.0F - var23);
	               GL11.glFogf(2916, 5000.0F + var23 * 8.0F);
	               ItemGuiRenderer.enableStandardItemLighting();
	               ItemGuiRenderer.disableStandardItemLighting();
	               var17.sortChunks(var15, 0);
	               GL11.glEnable(3042);
	               GL11.glBlendFunc(770, 771);
	               GL11.glColorMask(false, false, false, false);
	               int var13 = var17.sortChunks(var15, 1);
	               GL11.glColorMask(true, true, true, true);
	               if (var13 > 0) {
	                  var17.renderAllRenderLists();
	               }

	               GL11.glDepthMask(true);
	               GL11.glDisable(3042);
	               GL11.glDisable(2912);
	               this.entityByteBuffer.clear();
	               GL11.glPixelStorei(3333, 1);
	               GL11.glReadPixels(0, 0, this.minecraft.width, this.minecraft.height, 6407, 5121, this.entityByteBuffer);
	               BufferedImage var21 = screenshotBuffer(this.entityByteBuffer, var6, var7);
	               var5.drawImage(var21, var10, var11, (ImageObserver)null);
	            }
	         }

	         var5.dispose();
	         this.minecraft.progressBar.setText("Saving as " + var3.getName());
	         this.minecraft.progressBar.setProgress(100);
	         this.minecraft.hud.addChat("Saved isometric screenshot as " + var3.getName());
	         FileOutputStream var20 = new FileOutputStream(var3);
	         ImageIO.write(var4, "png", var20);
	         var20.close();
	      } catch (Throwable var23) {
	         var23.printStackTrace();
	      }

	   }

	   private static BufferedImage screenshotBuffer(ByteBuffer var0, int var1, int var2) {
	      var0.position(0).limit(var1 * var2 << 2);
	      BufferedImage var3;
	      int[] var4 = ((DataBufferInt)(var3 = new BufferedImage(var1, var2, 1)).getRaster().getDataBuffer()).getData();

	      for(int var5 = 0; var5 < var1 * var2; ++var5) {
	         int var6 = var0.get(var5 * 3) & 255;
	         int var7 = var0.get(var5 * 3 + 1) & 255;
	         int var8 = var0.get(var5 * 3 + 2) & 255;
	         var4[var5] = var6 << 16 | var7 << 8 | var8;
	      }

	      return var3;
	   }

}
