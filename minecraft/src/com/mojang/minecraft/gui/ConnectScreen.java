package com.mojang.minecraft.gui;

import java.net.ConnectException;
import java.net.UnknownHostException;

import com.mojang.minecraft.Minecraft;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.net.NetworkManager;

public class ConnectScreen extends GuiScreen {
   private GuiScreen parent;
   public String title = "Connecting to the server...";
	   
   public ConnectScreen(GuiScreen var1) {
	  this.parent = var1;
   }

   public void tick() {
     ++this.ticks;
   }

   protected final void onKeyPress(char var1, int var2) {
   }

   public final void onOpen() {
      this.buttons.clear();
      this.buttons.add(new Button(0, this.width / 2 - 100, this.height / 4 + 120 + 12, "Cancel"));
   }

   protected final void onButtonClick(Button var1) {
      if (var1.id == 0) {
    	  this.minecraft.setCurrentScreen(this.parent);
      }

   }

   public final void render(int var1, int var2) {
      this.drawBackground();
      this.drawCenteredString(this.fontRenderer, this.title, this.width / 2, this.height / 2 - 50, 16777215);
      this.minecraft.progressBar.setProgress(0);
      
      super.render(var1, var2);
   }
}
