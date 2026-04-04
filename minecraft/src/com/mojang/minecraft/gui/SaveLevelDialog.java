package com.mojang.minecraft.gui;

import org.lwjgl.opengl.GL11;

import com.mojang.minecraft.gui.Button;
import com.mojang.minecraft.gui.GenerateLevelScreen;
import com.mojang.minecraft.gui.GuiScreen;
import com.mojang.minecraft.gui.LoadLevelScreen;
import com.mojang.minecraft.gui.OptionsScreen;
import com.mojang.minecraft.gui.SaveLevelScreen;

public final class SaveLevelDialog extends GuiScreen {
	
   private GuiScreen parent;
   String title = "Do you want quit to title menu, without saving level?";
   
   public SaveLevelDialog(GuiScreen var1) {
		  this.parent = var1;
   }
	   
   public final void onOpen() {
	      this.buttons.clear();
	      this.buttons.add(new Button(0, this.width / 2 - 98 - 8, this.height / 4 + 80, 112, 12, "Save level.."));
	      this.buttons.add(new Button(1, this.width / 2 - -16 - 8, this.height / 4 + 80, 98, 12, "Quit"));
	      if (this.minecraft.session == null) {
	         ((Button)this.buttons.get(0)).active = false;
	      }

   }

   protected final void onButtonClick(Button var1) {
	   if (this.minecraft.session != null) {
	     if (var1.id == 0) {
	       this.minecraft.setCurrentScreen(new SaveLevelScreen(this));
	     }
	   }
       
       if (var1.id == 1) {
           this.minecraft.level = null;
           this.minecraft.setCurrentScreen(new TitleScreen());
       }

   }

   public final void render(int var1, int var2) {
      drawFadingBox(0, 0, this.width, this.height, 1610941696, -1607454624);
	  drawCenteredString(this.fontRenderer, this.title, this.width / 2, 80, 16777215);
      super.render(var1, var2);
   }
}