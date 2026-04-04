package com.mojang.minecraft.gui;

import com.mojang.minecraft.GameSettings;
import com.mojang.minecraft.Minecraft;
import org.lwjgl.opengl.GL11;

public class Slider extends Button {
   public float sliderValue = 1.0F;
   public boolean dragging = false;
   public int type = 0;

   public Slider(int var1, int var2, int var3, String var4, int var5) {
      super(var1, var2, var3, var4);
      this.width = 150;
      this.height = 20;
      this.slider = true;
      this.type = var5;
   }

   public Slider(int var1, int var2, int var3, String var4, int var5, int var6, int var7) {
      super(var1, var2, var3, var4);
      this.width = var6;
      this.height = var7;
      this.slider = true;
      this.type = var5;
   }

   protected void mouseDragged(Minecraft minecraft, int i, int j) {}

   public boolean mousePressed(Minecraft minecraft, int i, int j) {
	   return false;
   }

   public void mouseReleased(int i, int j) {
      this.dragging = false;
   }
}
