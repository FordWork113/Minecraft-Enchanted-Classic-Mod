package com.mojang.minecraft.gui;

import com.mojang.minecraft.gui.Button;
import com.mojang.minecraft.gui.Screen;
import com.mojang.minecraft.item.ItemStack;
import com.mojang.minecraft.level.item.Sign;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class SignEditScreen extends GuiScreen {
   protected String title = "Edit sign message:";
   public String[] messages = new String[4];
   private int line = 0;
   private int updateCounter = 0;

   public SignEditScreen() {
	  for(int var1 = 0; var1 < this.messages.length; ++var1) {
		  this.messages[var1] = "";
      }
   }

   public final void onOpen() {
      this.buttons.clear();
      Keyboard.enableRepeatEvents(true);
      this.buttons.add(new Button(0, this.width / 2 - 100, this.height / 4 + 120, "Done"));
   }

   public final void onClose() {
      Keyboard.enableRepeatEvents(false);
   }
   public void tick() {
      ++this.updateCounter;
   }

   protected final void onButtonClick(Button button) {
         if (button.active && button.id == 0) {
            this.minecraft.level.addEntity(new Sign(this.minecraft.level, this.minecraft.player.x, this.minecraft.player.y, this.minecraft.player.z, this.minecraft.player.yRot, this.messages));
            ItemStack var9 = this.minecraft.player.inventory.getSelected();
            --var9.stackSize;
            this.minecraft.setCurrentScreen((GuiScreen)null);
            this.minecraft.grabMouse();
         }
   }

   protected final void onKeyPress(char var1, int var2) {
	      if (var2 == 1) {
	         this.minecraft.setCurrentScreen((GuiScreen)null);
	      }

	      if (var2 != 28 && var2 != 208) {
	         if (var2 == 200) {
	            this.line = (this.line + 3) % 4;
	         } else {
	            if (var2 == 14 && this.messages[this.line].length() > 0) {
	               this.messages[this.line] = this.messages[this.line].substring(0, this.messages[this.line].length() - 1);
	            }

	            if ("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 ,.:-_\'*!\\\"#%/()=+?[]{}<>@|$;".indexOf(var1) >= 0 && this.messages[this.line].length() <= 15) {
	               this.messages[this.line] = this.messages[this.line] + var1;
	            }
	         }
	      } else {
	         this.line = (this.line + 1) % 4;
	      }

	   }

   public final void render(int var1, int var2) {
	      drawFadingBox(0, 0, this.width, this.height, 1610941696, -1607454624);
	      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	      GL11.glEnable(3042);
	      GL11.glBindTexture(3553, this.minecraft.textureManager.load("/gui/sign.png"));
	      this.drawImage(this.width / 2 - 48, this.height / 2 - 58, 0, 0, 96, 104);
	      GL11.glDisable(3042);
	      drawCenteredString(this.fontRenderer, "Enter sign message:", this.width / 2, 40, 16777215);
	      drawCenteredStringNoShadow(this.fontRenderer, (this.updateCounter / 6 % 2 == 0 ? ">" : "") + this.messages[this.line] + (this.updateCounter / 6 % 2 == 0 ? "<" : ""), this.width / 2, this.height / 2 - 55 + 10 * this.line, 2105376);

	      for(int i = 0; i < this.messages.length; ++i) {
	    	  drawCenteredStringNoShadow(this.fontRenderer, this.messages[i], this.width / 2, this.height / 2 - 55 + 10 * i, 2105376);
	      }

	      super.render(var1, var2);
	}
   
    public boolean doesGuiPause() {
	 	  return false;
	}
}
