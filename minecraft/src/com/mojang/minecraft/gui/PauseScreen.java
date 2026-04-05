package com.mojang.minecraft.gui;

import org.lwjgl.opengl.GL11;

import com.mojang.minecraft.Minecraft;
import com.mojang.minecraft.gui.Button;
import com.mojang.minecraft.gui.GenerateLevelScreen;
import com.mojang.minecraft.gui.GuiScreen;
import com.mojang.minecraft.gui.LoadLevelScreen;
import com.mojang.minecraft.gui.OptionsScreen;
import com.mojang.minecraft.gui.SaveLevelScreen;
import com.mojang.minecraft.gui.DisconnectScreen;
import com.mojang.minecraft.net.NetworkManager;

public final class PauseScreen extends GuiScreen {
   private GuiScreen parent;
   
   public final void onOpen() {
      this.buttons.clear();
      this.buttons.add(new Button(0, this.width / 2 - 100, this.height / 4, "Back to game"));
      this.buttons.add(new Button(1, this.width / 2 - 100, this.height / 4 + 24, "Generate new level..."));
      this.buttons.add(new Button(2, this.width / 2 - 92 - 8, this.height / 4 + 48, 98, 12, "Save level.."));
  	   this.buttons.add(new Button(3, this.width / 2 - -10 - 8, this.height / 4 + 48, 98, 12, "Load level.."));
      this.buttons.add(new Button(4, this.width / 2 - 100, this.height / 4 + 86, "Options..."));
      this.buttons.add(new Button(5, this.width / 2 - 100, this.height / 4 + 110, "Quit to title menu.."));

      if (this.minecraft.session == null) {
         ((Button)this.buttons.get(2)).active = false;
         ((Button)this.buttons.get(3)).active = false;
      }

      if (this.minecraft.networkManager != null) {
         ((Button)this.buttons.get(1)).active = false;
         ((Button)this.buttons.get(2)).active = false;
         ((Button)this.buttons.get(3)).active = false;
         ((Button)this.buttons.get(5)).text = "Disconnect";
      }

   }

   protected final void onButtonClick(Button var1) {
	  if (var1.id == 0) {
	     this.minecraft.setCurrentScreen((GuiScreen)null);
	     this.minecraft.grabMouse();
	  }
	  
      if (var1.id == 1) {
         this.minecraft.setCurrentScreen(new GenerateLevelScreen(this));
      }

      if (this.minecraft.session != null) {
         if (var1.id == 2) {
            this.minecraft.setCurrentScreen(new SaveLevelScreen(this));
         }

         if (var1.id == 3) {
            this.minecraft.setCurrentScreen(new LoadLevelScreen(this));
         }
      }
      
      if (var1.id == 4) {
          this.minecraft.setCurrentScreen(new OptionsScreen(this, this.minecraft.settings));
      }

      if (var1.id == 5 && this.minecraft.networkManager == null) {
          this.minecraft.setCurrentScreen(new SaveLevelDialog(this));
      } else if(var1.id == 5 && this.minecraft.networkManager != null) {
          this.minecraft.setCurrentScreen(new DisconnectScreen(this.parent));
      }

   }

   public final void render(int var1, int var2) {
      drawFadingBox(0, 0, this.width, this.height, 1610941696, -1607454624);
      drawCenteredString(this.fontRenderer, "Game menu", this.width / 2, 30, 16777215);
      super.render(var1, var2);
   }
}