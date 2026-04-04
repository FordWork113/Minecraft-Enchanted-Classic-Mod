package com.mojang.minecraft;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import org.lwjgl.input.Keyboard;

import com.mojang.minecraft.render.texture.TextureLavaFX;
import com.mojang.minecraft.render.texture.TextureWaterFX;
import com.mojang.minecraft.render.texture.TextureWaterFlowFX;

public final class GameSettings {
   static final String[] renderDistances = new String[]{"FAR", "NORMAL", "SHORT", "TINY"};
   public boolean music = true;
   public boolean sound = true;
   public boolean invertMouse = false;
   public int viewDistance = 0;
   public boolean viewBobbing = true;
   public boolean limitFramerate = false;
   public boolean handleClick = false;
   
   public boolean shaders = false;
   public boolean anaglyph = false;
   public boolean fancyGraphics = true;
   public boolean viewClouds = true;
   public boolean animations = true;
   public boolean particles = true;
   
   public boolean debugScreen = false;
   public String skin = "Default";
   public String lastServer = "";
   public KeyBinding forwardKey = new KeyBinding("Forward", 17);
   public KeyBinding leftKey = new KeyBinding("Left", 30);
   public KeyBinding backKey = new KeyBinding("Back", 31);
   public KeyBinding rightKey = new KeyBinding("Right", 32);
   public KeyBinding jumpKey = new KeyBinding("Jump", 57);
   public KeyBinding inventoryKey = new KeyBinding("Inventory", 23);
   public KeyBinding chatKey = new KeyBinding("Chat", 20);
   public KeyBinding toggleFogKey = new KeyBinding("Toggle fog", 33);
   public KeyBinding saveLocationKey = new KeyBinding("Save location", 28);
   public KeyBinding loadLocationKey = new KeyBinding("Load location", 19);
   public KeyBinding playersListKey = new KeyBinding("Players list", 15);
   public KeyBinding dropKey = new KeyBinding("Drop", 16);
   public KeyBinding flyKey = new KeyBinding("Fly", 45);
   public KeyBinding zoomKey = new KeyBinding("Zoom", 45);
   public KeyBinding signKey = new KeyBinding("Sign", 35);
   public KeyBinding debugKey = new KeyBinding("Debug Menu", 61);
   public KeyBinding hideGuiKey = new KeyBinding("Hide GUI", 59);
   public KeyBinding screenshotKey = new KeyBinding("Screenshot", 60);
   public KeyBinding fullScreenKey = new KeyBinding("Toggle Fullscreen", 87);
   public KeyBinding bigScreenshotKey = new KeyBinding("Isometric screenshot", 65);
   public KeyBinding rainKey = new KeyBinding("Toggle rain", 62);
   public KeyBinding snowKey = new KeyBinding("Toggle snow", 63);
   public KeyBinding craftingKey = new KeyBinding("Crafting", 46);

   public KeyBinding[] bindings;
   private Minecraft minecraft;
   private File settingsFile;
   public int settingCount;

   public GameSettings(Minecraft var1, File var2) {
      this.bindings = new KeyBinding[]{this.forwardKey, this.leftKey, this.backKey, this.rightKey, this.jumpKey, this.inventoryKey, this.chatKey, this.toggleFogKey, this.saveLocationKey, this.loadLocationKey, this.playersListKey, this.dropKey, this.flyKey, this.signKey, this.debugKey, this.hideGuiKey, this.screenshotKey, this.fullScreenKey};
      this.settingCount = 14;
      this.minecraft = var1;
      this.lastServer = " ";
      this.settingsFile = new File(var2, "options_ec.txt");
      this.load();
   }

   public String getBinding(int var1) {
      return this.bindings[var1].name + ": " + Keyboard.getKeyName(this.bindings[var1].key);
   }

   public void setBinding(int var1, int var2) {
      this.bindings[var1].key = var2;
      this.save();
   }

   public void toggleSetting(int var1, int var2) {
      if (var1 == 0) {
         this.music = !this.music;
      }

      if (var1 == 1) {
         this.sound = !this.sound;
      }

      if (var1 == 2) {
         this.invertMouse = !this.invertMouse;
      }

      if (var1 == 3) {
         this.viewDistance = this.viewDistance + var2 & 3;
      }

      if (var1 == 4) {
         this.viewBobbing = !this.viewBobbing;
      }
      
      if (var1 == 5) {
          this.limitFramerate = !this.limitFramerate;
      }
      
      if (var1 == 6) {
          this.handleClick = !this.handleClick;
      }
      
      if (var1 == 7) {
          this.minecraft.fullscreen = !this.minecraft.fullscreen;
          this.minecraft.toggleFullScreen();
      }

      if (var1 == 8) {
         this.shaders = !this.shaders;
         this.minecraft.enableShader = !this.minecraft.enableShader;
      }

      if (var1 == 9) {
         this.anaglyph = !this.anaglyph;
         this.minecraft.textureManager.refreshTextures();
      }

      if (var1 == 10) {
         this.fancyGraphics = !this.fancyGraphics;
         this.minecraft.levelRenderer.refresh();
      }
      
      if (var1 == 11) {
          this.viewClouds = !this.viewClouds;
      }
      
      if (var1 == 12) {
          this.animations = !this.animations;
          if (this.animations) {
             this.minecraft.textureManager.registerAnimation(new TextureLavaFX());
             this.minecraft.textureManager.registerAnimation(new TextureWaterFX());
             this.minecraft.textureManager.registerAnimation(new TextureWaterFlowFX());
          } else {
             this.minecraft.textureManager.animations.clear();
          }

          this.minecraft.textureManager.refreshTextures();       
      }
      
      if (var1 == 13) {
          this.particles = !this.particles;
      }

      this.save();
   }

   public String getSetting(int var1) {
      return var1 == 0 ? "Music: " + (this.music ? "ON" : "OFF") : (var1 == 1 ? "Sound: " + (this.sound ? "ON" : "OFF") : (var1 == 2 ? "Invert mouse: " + (this.invertMouse ? "ON" : "OFF") : (var1 == 3 ? "Render distance: " + renderDistances[this.viewDistance] : (var1 == 4 ? "View bobbing: " + (this.viewBobbing ? "ON" : "OFF") : (var1 == 5 ? "Limit framerate: " + (this.limitFramerate ? "ON" : "OFF") : (var1 == 6 ? "Mouse click: " + (this.handleClick ? "Switch" : "Default") : (var1 == 7 ? "Fullscreen: " + (this.minecraft.fullscreen ? "ON" : "OFF") : (var1 == 8 ? "Shaders: " + (this.shaders ? "ON" : "OFF") : (var1 == 9 ? "3D Anaglyph: " + (this.anaglyph ? "ON" : "OFF") : (var1 == 10 ? "Graphics: " + (this.fancyGraphics ? "Fancy" : "Fast") : (var1 == 11 ? "Clouds: " + (this.viewClouds ? "ON" : "OFF") : (var1 == 12 ? "Animations: " + (this.animations ? "ON" : "OFF") : (var1 == 13 ? "Particles: " + (this.particles ? "ON" : "OFF") : "")))))))))))));
   }

   private void load() {
      try {
         if (this.settingsFile.exists()) {
            FileReader var1 = new FileReader(this.settingsFile);
            BufferedReader var2 = new BufferedReader(var1);
            String var3 = null;

            while((var3 = var2.readLine()) != null) {
               String[] var4 = var3.split(":");
               if (var4[0].equals("music")) {
                  this.music = var4[1].equals("true");
               }

               if (var4[0].equals("sound")) {
                  this.sound = var4[1].equals("true");
               }

               if (var4[0].equals("invertYMouse")) {
                  this.invertMouse = var4[1].equals("true");
               }
               
               if (var4[0].equals("viewDistance")) {
                  this.viewDistance = Integer.parseInt(var4[1]);
               }

               if (var4[0].equals("bobView")) {
                  this.viewBobbing = var4[1].equals("true");
               }

               if (var4[0].equals("limitFramerate")) {
                  this.limitFramerate = var4[1].equals("true");
               }
               
               if (var4[0].equals("handleClick")) {
            	  this.handleClick = var4[1].equals("true");
               }
               
               if (var4[0].equals("shaders")) {
                  this.anaglyph = var4[1].equals("true");
               }

               if (var4[0].equals("anaglyph3d")) {
                  this.anaglyph = var4[1].equals("true");
               }

               if (var4[0].equals("fancyGraphics")) {
                  this.fancyGraphics = var4[1].equals("true");
               }
               
               if (var4[0].equals("clouds")) {
                  this.viewClouds = var4[1].equals("true");
               }
               
               if (var4[0].equals("animations")) {
                  this.animations = var4[1].equals("true");
               }
               
               if (var4[0].equals("particles")) {
                  this.particles = var4[1].equals("true");
               }          

               if (var4[0].equals("skin")) {
                  this.skin = var4[1];
               }

               if (var4[0].equals("lastServer")) {
                  this.lastServer = var4[1];
               }

               for(int var5 = 0; var5 < this.bindings.length; ++var5) {
                  if (var4[0].equals("key_" + this.bindings[var5].name)) {
                     this.bindings[var5].key = Integer.parseInt(var4[1]);
                  }
               }
            }

            var2.close();
         }
      } catch (Exception var6) {
         System.out.println("Failed to load options");
         var6.printStackTrace();
      }

   }

   public void save() {
      try {
         FileWriter var1 = new FileWriter(this.settingsFile);
         PrintWriter var2 = new PrintWriter(var1);
         var2.println("music:" + this.music);
         var2.println("sound:" + this.sound);
         var2.println("invertYMouse:" + this.invertMouse);
         var2.println("viewDistance:" + this.viewDistance);
         var2.println("bobView:" + this.viewBobbing);
         var2.println("limitFramerate:" + this.limitFramerate);
         var2.println("handleClick:" + this.handleClick);
         var2.println("shaders:" + this.shaders);
         var2.println("anaglyph3d:" + this.anaglyph);
         var2.println("fancyGraphics:" + this.fancyGraphics);         
         var2.println("clouds:" + this.viewClouds);
         var2.println("animations:" + this.animations);
         var2.println("particles:" + this.particles);
         var2.println("skin:" + this.skin);
         var2.println("lastServer:" + this.lastServer);

         for(int var3 = 0; var3 < this.bindings.length; ++var3) {
            var2.println("key_" + this.bindings[var3].name + ":" + this.bindings[var3].key);
         }

         var2.close();
      } catch (Exception var4) {
         System.out.println("Failed to save options");
         var4.printStackTrace();
      }

   }
}
