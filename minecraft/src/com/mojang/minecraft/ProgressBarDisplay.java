package com.mojang.minecraft;

import com.mojang.minecraft.gui.ScaledResolution;
import com.mojang.minecraft.render.ShapeRenderer;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public final class ProgressBarDisplay {
   private String text = "";
   private Minecraft minecraft;
   private String title = "";
   private long start = System.currentTimeMillis();

   public ProgressBarDisplay(Minecraft var1) {
      this.minecraft = var1;
   }

   public final void setTitle(String var1) {
      if (!this.minecraft.running) {
         throw new StopGameException();
      } else {
         this.title = var1;
         ScaledResolution var69 = new ScaledResolution(this.minecraft.width, this.minecraft.height);
         int var2 = var69.getScaledWidth();
         int var3 = var69.getScaledHeight();
         GL11.glClear(256);
         GL11.glMatrixMode(5889);
         GL11.glLoadIdentity();
         GL11.glOrtho(0.0D, (double)var2, (double)var3, 0.0D, 100.0D, 300.0D);
         GL11.glMatrixMode(5888);
         GL11.glLoadIdentity();
         GL11.glTranslatef(0.0F, 0.0F, -200.0F);
      }
   }

   public final void setText(String var1) {
      if (!this.minecraft.running) {
         throw new StopGameException();
      } else {
         this.text = var1;
         this.setProgress(-1);
      }
   }

   public final void setProgress(int var1) {
      if (!this.minecraft.running) {
         throw new StopGameException();
      } else {
         long var2;
         if ((var2 = System.currentTimeMillis()) - this.start < 0L || var2 - this.start >= 20L) {
            this.start = var2;
            ScaledResolution var69 = new ScaledResolution(this.minecraft.width, this.minecraft.height);
            int var4 = var69.getScaledWidth();
            int var5 = var69.getScaledHeight();
            GL11.glClear(16640);
            ShapeRenderer var6 = ShapeRenderer.instance;
            int var7 = this.minecraft.textureManager.load("/cobble.png");
            GL11.glBindTexture(3553, var7);
            float var8 = 32.0F;
            var6.begin();
            var6.color(4210752);
            var6.vertexUV(0.0F, (float)var5, 0.0F, 0.0F, (float)var5 / var8);
            var6.vertexUV((float)var4, (float)var5, 0.0F, (float)var4 / var8, (float)var5 / var8);
            var6.vertexUV((float)var4, 0.0F, 0.0F, (float)var4 / var8, 0.0F);
            var6.vertexUV(0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
            var6.end();
            if (var1 >= 0) {
               var7 = var4 / 2 - 50;
               int var9 = var5 / 2 + 16;
               GL11.glDisable(3553);
               var6.begin();
               var6.color(8421504);
               var6.vertex((float)var7, (float)var9, 0.0F);
               var6.vertex((float)var7, (float)(var9 + 2), 0.0F);
               var6.vertex((float)(var7 + 100), (float)(var9 + 2), 0.0F);
               var6.vertex((float)(var7 + 100), (float)var9, 0.0F);
               var6.color(8454016);
               var6.vertex((float)var7, (float)var9, 0.0F);
               var6.vertex((float)var7, (float)(var9 + 2), 0.0F);
               var6.vertex((float)(var7 + var1), (float)(var9 + 2), 0.0F);
               var6.vertex((float)(var7 + var1), (float)var9, 0.0F);
               var6.end();
               GL11.glEnable(3553);
            }

            this.minecraft.fontRenderer.render(this.title, (var4 - this.minecraft.fontRenderer.getWidth(this.title)) / 2, var5 / 2 - 4 - 16, 16777215);
            this.minecraft.fontRenderer.render(this.text, (var4 - this.minecraft.fontRenderer.getWidth(this.text)) / 2, var5 / 2 - 4 + 8, 16777215);
            Display.update();

            try {
               Thread.yield();
            } catch (Exception var10) {
            }
         }

      }
   }
}
