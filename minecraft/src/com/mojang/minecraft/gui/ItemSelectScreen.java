package com.mojang.minecraft.gui;

import com.mojang.minecraft.gui.GuiScreen;
import com.mojang.minecraft.item.ItemStack;
import com.mojang.minecraft.player.Inventory;
import com.mojang.minecraft.render.ShapeRenderer;
import com.mojang.minecraft.render.TextureManager;
import com.mojang.minecraft.item.Items;
import com.mojang.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

public final class ItemSelectScreen extends GuiScreen {

  private int getItemOnScreen(int var1, int var2) {
		 for(int var3 = 256; var3 < 1024; ++var3) {
	        int var4 = this.width / 2 + var3 % 9 * 24 + -108 - 3;
	        int var5 = this.height / 2 + var3 / 9 * 24 + -60 + 3;
	        if(var1 >= var4 && var1 <= var4 + 24 && var2 >= var5 - 12 && var2 <= var5 + 12) {
	          return var3;
	       }
		 }

		return -1;
	}
	   
	public final void render(int var1, int var2) {
		var1 = this.getItemOnScreen(var1, var2);
		drawFadingBox(this.width / 2 - 120, 30, this.width / 2 + 120, 180, -1878719232, -1070583712);
        drawCenteredString(this.fontRenderer, "Select item", this.width / 2, 40, 16777215);
	 }

	protected final void onKeyPress(char var1, int var2) {
		if(var2 == this.minecraft.settings.inventoryKey.key | var2 == 1) {
			this.minecraft.setCurrentScreen((GuiScreen)null);
	        this.minecraft.grabMouse();
		}

	}
	
    public boolean doesGuiPause() {
  	  return false;
    }
}
