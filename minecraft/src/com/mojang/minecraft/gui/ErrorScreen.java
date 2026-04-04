package com.mojang.minecraft.gui;

import org.lwjgl.opengl.Display;

import com.mojang.minecraft.gui.GuiScreen;

public final class ErrorScreen extends GuiScreen {

   private String title;
   private String text;


   public ErrorScreen(String var1, String var2) {
      this.title = var1;
      this.text = var2;
   }

   public final void onOpen() {}

   public final void render(int var1, int var2) {
      drawFadingBox(0, 0, this.width, this.height, -12574688, -11530224);
      drawCenteredString(this.fontRenderer, this.title, this.width / 2, 90, 16777215);
      drawCenteredString(this.fontRenderer, this.text, this.width / 2, 110, 16777215);
      this.buttons.add(new Button(0, this.width / 2 - 100, this.height / 4 + 120, "Quit game"));
      super.render(var1, var2);
   }
   
   protected final void onButtonClick(Button var1) {
	  if (var1.id == 0) {
	        this.minecraft.shutdown();
	  }

   }
   
   protected final void onKeyPress(char var1, int var2) {}
}
