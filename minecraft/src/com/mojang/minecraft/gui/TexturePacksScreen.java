package com.mojang.minecraft.gui;

import java.io.File;
import java.util.List;
import com.mojang.minecraft.Minecraft;
import com.mojang.minecraft.gui.Button;
import com.mojang.minecraft.gui.Screen;
import com.mojang.minecraft.render.ShapeRenderer;
import com.mojang.minecraft.skins.*;

import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class TexturePacksScreen extends GuiScreen {
   protected GuiScreen parent;
   private int yo = 0;
   private int y0 = 32;
   private int y1;
   private int x0;
   private int x1;
   private int yDrag;
   private int updateIn;
   private String instructions;
   private String skinspath = "texturepacks";

   public TexturePacksScreen(GuiScreen var1) {
      this.y1 = this.height - 55 + 4;
      this.x0 = 0;
      this.x1 = this.width;
      this.yDrag = -2;
      this.updateIn = -1;
      this.instructions = "";
      this.parent = var1;
   }

   public void onOpen() {
      this.buttons.clear();
	  this.buttons.add(new OptionButton(5, this.width / 2 - 154, this.height - 48, "Open texture pack folder"));
	  this.buttons.add(new OptionButton(6, this.width / 2 + 4, this.height - 48, "Done"));
      this.instructions = (new File(this.minecraft.minecraftDir, this.skinspath)).getAbsolutePath();
      this.y0 = 32;
      this.y1 = this.height - 58 + 4;
      this.x0 = 0;
      this.x1 = this.width;
   }

   protected final void onButtonClick(Button var1) {
       if (var1.id == 5) {
          Sys.openURL("file://" + this.instructions);
       }

       if (var1.id == 6) {
    	   this.minecraft.textureManager.refreshTextures();
      	   this.minecraft.setCurrentScreen(this.parent);
       }

 }

   protected void onKeyPress(char var1, int var2) {
	      if(var2 == 1 && this.minecraft.level != null) {
	         this.minecraft.setCurrentScreen((GuiScreen)null);
	         this.minecraft.grabMouse();
	      }  else if(var2 == 1 && this.minecraft.level == null) {
			   this.minecraft.setCurrentScreen(this.parent);
		  }

   }
   
   protected void onMouseClick(int x, int y, int buttonNum) {
      super.onMouseClick(x, y, buttonNum);
   }

   protected void mouseReleased(int x, int y, int buttonNum) {
      super.mouseReleased(x, y, buttonNum);
   }

   public void render(int var1, int var2) {
      ShapeRenderer var6 = ShapeRenderer.instance;
      this.drawBackground();
      
      if (this.updateIn <= 0) {
         this.minecraft.skins.updateList();
         this.updateIn += 20;
      }

      List<TexturePack> skins = this.minecraft.skins.getAll();
      int max;
      if (Mouse.isButtonDown(0)) {
         if (this.yDrag == -1) {
            if (var2 >= this.y0 && var2 <= this.y1) {
               max = this.width / 2 - 110;
               int x1 = this.width / 2 + 110;
               int slot = (var2 - this.y0 + this.yo - 2) / 36;
               if (var1 >= max && var1 <= x1 && slot >= 0 && slot < skins.size() && this.minecraft.skins.selectSkin((TexturePack)skins.get(slot))) {
                  this.minecraft.textureManager.refreshTextures();
               }

               this.yDrag = var2;
            } else {
               this.yDrag = -2;
            }
         } else if (this.yDrag >= 0) {
            this.yo -= var2 - this.yDrag;
            this.yDrag = var2;
         }
      } else {
         if (this.yDrag >= 0) {
         }

         this.yDrag = -1;
      }

      max = skins.size() * 36 - (this.y1 - this.y0 - 4);
      if (max < 0) {
         max /= 2;
      }

      if (this.yo < 0) {
         this.yo = 0;
      }

      if (this.yo > max) {
         this.yo = max;
      }

      GL11.glDisable(2896);
      GL11.glDisable(2912);
      GL11.glBindTexture(3553, this.minecraft.textureManager.load("/cobble.png"));
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      float s = 32.0F;
      var6.begin();
      var6.color(2105376);
      var6.vertexUV((float)this.x0, (float)this.y1, 0.0F, (float)((float)this.x0 / s), (float)((float)(this.y1 + this.yo) / s));
      var6.vertexUV((float)this.x1, (float)this.y1, 0.0F, (float)((float)this.x1 / s), (float)((float)(this.y1 + this.yo) / s));
      var6.vertexUV((float)this.x1, (float)this.y0, 0.0F, (float)((float)this.x1 / s), (float)((float)(this.y0 + this.yo) / s));
      var6.vertexUV((float)this.x0, (float)this.y0, 0.0F, (float)((float)this.x0 / s), (float)((float)(this.y0 + this.yo) / s));
      var6.end();


      for(int i = 0; i < skins.size(); ++i) {
         TexturePack skin = (TexturePack)skins.get(i);
         int x = this.width / 2 - 92 - 16;
         int y = 36 + i * 36 - this.yo;
         int h = 32;
         int w = 32;
         if (skin == this.minecraft.skins.selected) {
            int x0 = this.width / 2 - 110;
            int x1 = this.width / 2 + 110;
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glDisable(3553);
            var6.begin();
            var6.color(8421504);
            var6.vertexUV((float)x0, (float)(y + h + 2), 0.0F, 0.0F, 1.0F);
            var6.vertexUV((float)x1, (float)(y + h + 2), 0.0F, 1.0F, 1.0F);
            var6.vertexUV((float)x1, (float)(y - 2), 0.0F, 1.0F, 0.0F);
            var6.vertexUV((float)x0, (float)(y - 2), 0.0F, 0.0F, 0.0F);
            var6.color(0);
            var6.vertexUV((float)(x0 + 1), (float)(y + h + 1), 0.0F, 0.0F, 1.0F);
            var6.vertexUV((float)(x1 - 1), (float)(y + h + 1), 0.0F, 1.0F, 1.0F);
            var6.vertexUV((float)(x1 - 1), (float)(y - 1), 0.0F, 1.0F, 0.0F);
            var6.vertexUV((float)(x0 + 1), (float)(y - 1), 0.0F, 0.0F, 0.0F);
            var6.end();
            GL11.glEnable(3553);
         }

         skin.bindTexture(this.minecraft);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         var6.begin();
         var6.color(16777215);
         var6.vertexUV((float)x, (float)(y + h), 0.0F, 0.0F, 1.0F);
         var6.vertexUV((float)(x + w), (float)(y + h), 0.0F, 1.0F, 1.0F);
         var6.vertexUV((float)(x + w), (float)y, 0.0F, 1.0F, 0.0F);
         var6.vertexUV((float)x, (float)y, 0.0F, 0.0F, 0.0F);
         var6.end();
         this.drawString(this.fontRenderer, skin.name, x + w + 2, y + 1, 16777215);
         this.drawString(this.fontRenderer, skin.desc1, x + w + 2, y + 12, 8421504);
         this.drawString(this.fontRenderer, skin.desc2, x + w + 2, y + 12 + 10, 8421504);
      }

      this.renderHoleBackground(0, this.y0);
      this.renderHoleBackground(this.y1, this.height);
      GL11.glEnable(3553);
      GL11.glShadeModel(7424);
      GL11.glEnable(3008);
      GL11.glDisable(3042);
      byte byte0 = 4;
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glDisable(3008);
      GL11.glShadeModel(7425);
      GL11.glDisable(3553);
      GL11.glBegin(7);
      GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.0F);
      GL11.glVertex2f((float)this.x0, (float)(this.y0 + byte0));
      GL11.glVertex2f((float)this.x1, (float)(this.y0 + byte0));
      GL11.glColor4f(0.0F, 0.0F, 0.0F, 1.0F);
      GL11.glVertex2f((float)this.x1, (float)this.y0);
      GL11.glVertex2f((float)this.x0, (float)this.y0);
      GL11.glEnd();
      GL11.glBegin(7);
      GL11.glColor4f(0.0F, 0.0F, 0.0F, 1.0F);
      GL11.glVertex2f((float)this.x0, (float)this.y1);
      GL11.glVertex2f((float)this.x1, (float)this.y1);
      GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.0F);
      GL11.glVertex2f((float)this.x1, (float)(this.y1 - byte0));
      GL11.glVertex2f((float)this.x0, (float)(this.y1 - byte0));
      GL11.glEnd();
      GL11.glEnable(3553);
      GL11.glShadeModel(7424);
      GL11.glEnable(3008);
      GL11.glDisable(3042);
	  drawCenteredString(this.fontRenderer, "Select Texture Pack", this.width / 2, 16, 16777215);
	  drawCenteredString(this.fontRenderer, "(Place texture pack files here)", this.width / 2 - 77, this.height - 26, 8421504);
	  super.render(var1, var2);
   }

   public void tick() {
      super.tick();
      --this.updateIn;
   }
   
   public void renderHoleBackground(int y0, int y1) {
	   ShapeRenderer var6 = ShapeRenderer.instance;
	   GL11.glBindTexture(3553, this.minecraft.textureManager.load("/cobble.png"));
	   GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	   float s = 32.0F;
	   var6.begin();
	   var6.color(4210752);
	   var6.vertexUV(0.0F, (float)y1, 0.0F, 0.0F, (float)((float)y1 / s));
	   var6.vertexUV((float)this.width, (float)y1, 0.0F, (float)((float)this.width / s), (float)((float)y1 / s));
	   var6.color(4210752);
	   var6.vertexUV((float)this.width, (float)y0, 0.0F, (float)((float)this.width / s), (float)((float)y0 / s));
	   var6.vertexUV(0.0F, (float)y0, 0.0F, 0.0F, (float)((float)y0 / s));
	   var6.end();
    }
   
}
