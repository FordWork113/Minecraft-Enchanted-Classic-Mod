package com.mojang.minecraft.gui;

import com.mojang.minecraft.Minecraft;
import com.mojang.minecraft.SessionData;
import com.mojang.minecraft.gui.*;
import com.mojang.minecraft.render.ShapeRenderer;
import com.mojang.minecraft.render.TextureManager;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiScreen extends Screen {

   protected Minecraft minecraft;
   protected int width;
   protected int height;
   protected List buttons = new ArrayList();
   public boolean grabsMouse = false;
   protected FontRenderer fontRenderer;
   private Button clickedButton = null;
   public float ticks = 0.0F;
   public Random random = new Random();
   
   public void render(int var1, int var2) {
      for(int var3 = 0; var3 < this.buttons.size(); ++var3) {
         Button var10000 = (Button)this.buttons.get(var3);
         Minecraft var7 = this.minecraft;
         Button var4 = var10000;
         if(var10000.visible) {
            FontRenderer var8 = var7.fontRenderer;
            GL11.glBindTexture(3553, var7.textureManager.load("/gui/gui.png"));
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            byte var9 = 1;
            boolean var6 = var1 >= var4.x && var2 >= var4.y && var1 < var4.x + var4.width && var2 < var4.y + var4.height;
            if(!var4.active) {
               var9 = 0;
            } else if(var6) {
               var9 = 2;
            }

            var4.drawImage(var4.x, var4.y, 0, 46 + var9 * 20, var4.width / 2, var4.height);
            var4.drawImage(var4.x + var4.width / 2, var4.y, 200 - var4.width / 2, 46 + var9 * 20, var4.width / 2, var4.height);
            if(!var4.active) {
               Button.drawCenteredString(var8, var4.text, var4.x + var4.width / 2, var4.y + (var4.height - 8) / 2, -6250336);
            } else if(var6) {
               Button.drawCenteredString(var8, var4.text, var4.x + var4.width / 2, var4.y + (var4.height - 8) / 2, 16777120);
            } else {
               Button.drawCenteredString(var8, var4.text, var4.x + var4.width / 2, var4.y + (var4.height - 8) / 2, 14737632);
            }
         }
      }

   }

	public static String getClipboardString() {
		try {
			Transferable transferable0 = Toolkit.getDefaultToolkit().getSystemClipboard().getContents((Object)null);
			if(transferable0 != null && transferable0.isDataFlavorSupported(DataFlavor.stringFlavor)) {
				String string1 = (String)transferable0.getTransferData(DataFlavor.stringFlavor);
				return string1;
			}
		} catch (Exception exception2) {
		}

		return null;
   }
	
   protected void onKeyPress(char var1, int var2) {
      if(var2 == 1) {
         this.minecraft.setCurrentScreen((GuiScreen)null);
         this.minecraft.grabMouse();
      }

   }

   protected void onMouseClick(int var1, int var2, int var3) {
      if(var3 == 0) {
         for(var3 = 0; var3 < this.buttons.size(); ++var3) {
            Button var4;
            Button var7;
            if((var7 = var4 = (Button)this.buttons.get(var3)).active && var1 >= var7.x && var2 >= var7.y && var1 < var7.x + var7.width && var2 < var7.y + var7.height) {
               this.onButtonClick(var4); 
               //this.minecraft.sound.playSoundFx("random.click", 0.6F, 1.0F);
            }
         }
      }

   }
   
   protected void mouseReleased(int x, int y, int buttonNum) {
	      if (this.clickedButton != null && buttonNum == 0) {
	    	  this.clickedButton.released(x, y);
	    	  this.clickedButton = null;
	      }

	   }

   protected void onButtonClick(Button var1) {}

   public final void open(Minecraft var1, int var2, int var3) {
      this.minecraft = var1;
      this.fontRenderer = var1.fontRenderer;
      this.width = var2;
      this.height = var3;
      this.onOpen();
   }

   public void onOpen() {}

   public final void doInput() {
      while(Mouse.next()) {
         this.mouseEvent();
      }

      while(Keyboard.next()) {
         this.keyboardEvent();
      }

   }

   public void mouseEvent() {
      if(Mouse.getEventButtonState()) {
         int var1 = Mouse.getEventX() * this.width / this.minecraft.width;
         int var2 = this.height - Mouse.getEventY() * this.height / this.minecraft.height - 1;
         this.onMouseClick(var1, var2, Mouse.getEventButton());
      } else {
		 Mouse.getEventX();
		 Mouse.getEventY();
		 Mouse.getEventButton();
      }

   }

   public final void keyboardEvent() {
      if(Keyboard.getEventKeyState()) {
         this.onKeyPress(Keyboard.getEventCharacter(), Keyboard.getEventKey());
         if(Keyboard.getEventKey() == this.minecraft.settings.fullScreenKey.key) {
        	 this.minecraft.fullscreen = !this.minecraft.fullscreen;
			this.minecraft.toggleFullScreen();
			return;
		 }
      }
   }

   public void tick() {}

   public void onClose() {}
   
   public void drawAnimateBackground() {
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_FOG);
		ShapeRenderer var6 = ShapeRenderer.instance;
		int var11 = this.minecraft.textureManager.load("/cobble.png");
        GL11.glBindTexture(3553, var11);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        var6.begin();
        var6.color(4210752);
		var6.vertexUV(0.0F, (float)this.height, 0.0F, 0.0F, (float)this.height / 32.0F + this.ticks);
		var6.vertexUV((float)this.width, (float)this.height, 0.0F, (float)this.width / 32.0F, (float)this.height / 32.0F + this.ticks);
		var6.vertexUV((float)this.width, 0.0F, 0.0F, (float)this.width / 32.0F, 0.0F + this.ticks);
		var6.vertexUV(0.0F, 0.0F, 0.0F, 0.0F, 0.0F + this.ticks);
		var6.end();	   
  }
   
  public final void drawBackground() {
       ScaledResolution var69 = new ScaledResolution(this.minecraft.width, this.minecraft.height);
       int var4 = var69.getScaledWidth();
       int var5 = var69.getScaledHeight();
       GL11.glClear(16640);
       ShapeRenderer var6 = ShapeRenderer.instance;
       int var7 = this.minecraft.textureManager.load("/cobble.png");
       GL11.glBindTexture(3553, var7);
       float var10 = 32.0F;
       var6.begin();
       var6.color(4210752);
       var6.vertexUV(0.0F, (float)var5, 0.0F, 0.0F, (float)var5 / var10);
       var6.vertexUV((float)var4, (float)var5, 0.0F, (float)var4 / var10, (float)var5 / var10);
       var6.vertexUV((float)var4, 0.0F, 0.0F, (float)var4 / var10, 0.0F);
       var6.vertexUV(0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
       var6.end();   
   }
  
  public boolean doesGuiPause() {
	   if(this.minecraft.isOnline() && this.minecraft.networkManager.isConnected() && this.minecraft.networkManager != null) {
	     return false;
	   }
	   
	   return true;
  }
 
}
