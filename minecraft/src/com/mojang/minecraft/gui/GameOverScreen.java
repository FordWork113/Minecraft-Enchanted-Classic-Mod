package com.mojang.minecraft.gui;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.Minecraft;
import com.mojang.minecraft.gui.Button;
import com.mojang.minecraft.gui.GenerateLevelScreen;
import com.mojang.minecraft.gui.GuiScreen;
import com.mojang.minecraft.gui.LoadLevelScreen;
import com.mojang.minecraft.gui.OptionsScreen;
import com.mojang.minecraft.level.item.Item;
import com.mojang.minecraft.level.item.Arrow;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.player.Player;
import com.mojang.minecraft.mob.Mob;
import com.mojang.minecraft.net.NetworkManager;

import org.lwjgl.opengl.GL11;

public final class GameOverScreen extends GuiScreen {
	
   public Player player;

   public final void onOpen() {
      this.buttons.clear();
      this.buttons.add(new Button(0, this.width / 2 - 100, this.height / 4 + 62, "Respawn"));
      this.buttons.add(new Button(1, this.width / 2 - 100, this.height / 4 + 86, "Generate new level..."));
      this.buttons.add(new Button(2, this.width / 2 - 100, this.height / 4 + 110, "Load level.."));
      this.buttons.add(new Button(3, this.width / 2 - 100, this.height / 4 + 134, "Quit to title..."));
		
      if(this.minecraft.level.gamemode == 2) {
          ((Button)this.buttons.get(0)).active = false;
       }
      
      if(this.minecraft.session == null) {
         ((Button)this.buttons.get(2)).active = false;
      }
      
      if (this.minecraft.networkManager != null) {
         ((Button)this.buttons.get(1)).active = false;
         ((Button)this.buttons.get(2)).active = false;
      }

   }

   protected final void onButtonClick(Button var1) {
	  
	  if(var1.id == 0) {
	     this.minecraft.player.resetPos();
	     this.minecraft.setCurrentScreen((GuiScreen)null);
	     this.minecraft.grabMouse();
	  }
	      
      if(var1.id == 1) {
         this.minecraft.setCurrentScreen(new GenerateLevelScreen(this));
      }

      if(this.minecraft.session != null && var1.id == 2) {
         this.minecraft.setCurrentScreen(new LoadLevelScreen(this));
      }
      
      if (var1.id == 3 && this.minecraft.networkManager == null) {
    	  this.minecraft.level = null;
          this.minecraft.setCurrentScreen(new TitleScreen());
      } else if(var1.id == 3 && this.minecraft.networkManager != null){	  
    	  NetworkManager var9 = this.minecraft.networkManager;
    	  var9.minecraft.online = false;
          var9.netHandler.close();
          var9.minecraft.networkManager = null;
          this.minecraft.hud.chat.clear();
          this.minecraft.level = null;
          this.minecraft.setCurrentScreen(new TitleScreen());
      }
      
   }

   public final void render(int var1, int var2) {
      drawFadingBox(0, 0, this.width, this.height, 1615855616, -1602211792);
      GL11.glPushMatrix();
      GL11.glScalef(2.0F, 2.0F, 2.0F);
      drawCenteredString(this.fontRenderer, "Game over!", this.width / 2 / 2, 20, 16777215);
      GL11.glPopMatrix();
      drawCenteredString(this.fontRenderer, "Score: &e" + this.minecraft.player.getScore(), this.width / 2, 90, 16777215);
      super.render(var1, var2);
   }
   
   public boolean doesGuiPause() {
	  return false;
   }
}
