package com.mojang.minecraft.gui;

import com.mojang.minecraft.GameSettings;
import com.mojang.minecraft.Minecraft;
import com.mojang.minecraft.render.ShapeRenderer;
import java.util.List;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class ControlsScreen extends GuiScreen {
   protected GuiScreen parent;
   private int amountScrolled = 0;
   private int top = 32;
   private int bottom;
   private int left;
   private int right;
   private int initialClickY;
   private GameSettings settings;
   private int selected = -1;

   public ControlsScreen(GuiScreen var1, GameSettings var2) {
      this.bottom = this.height - 55 + 4;
      this.left = 0;
      this.right = this.width;
      this.initialClickY = -2;
      this.parent = var1;
      this.settings = var2;
   }

   public void onOpen() {
      this.buttons.clear();

      for(int var1 = 0; var1 < this.settings.bindings.length; ++var1) {
         this.buttons.add(new OptionButton(var1, this.width / 2 - 155 + var1 % 2 * 160, this.height / 6 + 24 * (var1 >> 1), this.settings.getBinding(var1)));
      }

      this.buttons.add(new Button(200, this.width / 2 - 100, this.height - 40, "Done"));
      this.top = 32;
      this.bottom = this.height - 58 + 4;
      this.left = 0;
      this.right = this.width;
   }

   protected void onButtonClick(Button var1) {
      if (var1.active) {
         for(int var2 = 0; var2 < this.settings.bindings.length; ++var2) {
            ((Button)this.buttons.get(var2)).text = this.settings.getBinding(var2);
         }

         if (var1.id == 200) {
            this.minecraft.setCurrentScreen(this.parent);
         } else {
            this.selected = var1.id;
            var1.text = "> " + this.settings.getBinding(var1.id) + " <";
         }

      }
   }

   protected final void onKeyPress(char var1, int var2) {
      if (this.selected >= 0) {
         this.settings.setBinding(this.selected, var2);
         ((Button)this.buttons.get(this.selected)).text = this.settings.getBinding(this.selected);
         this.selected = -1;
      } else {
         super.onKeyPress(var1, var2);
      }

   }

   protected void onMouseClick(int i, int j, int k) {
      super.onMouseClick(i, j, k);
   }

   public void mouseEvent() {
      super.mouseEvent();
      if ((float)Mouse.getEventDWheel() < 0.0F) {
         this.amountScrolled = (int)((float)this.amountScrolled - (float)this.initialClickY * 10.0F);
      } else if ((float)Mouse.getEventDWheel() > 0.0F) {
         this.amountScrolled = (int)((float)this.amountScrolled + (float)this.initialClickY * 10.0F);
      }

   }

   public void render(int i, int j) {
      ScaledResolution var69 = new ScaledResolution(this.minecraft.width, this.minecraft.height);
      int var4 = var69.getScaledWidth();
      int var5 = var69.getScaledHeight();
      int k;
      if (this.minecraft.level == null) {
         GL11.glClear(16640);
         ShapeRenderer var6 = ShapeRenderer.instance;
         k = this.minecraft.textureManager.load("/cobble.png");
         GL11.glBindTexture(3553, k);
         float var10 = 32.0F;
         var6.begin();
         var6.color(4210752);
         var6.vertexUV(0.0F, (float)var5, 0.0F, 0.0F, (float)var5 / var10);
         var6.vertexUV((float)var4, (float)var5, 0.0F, (float)var4 / var10, (float)var5 / var10);
         var6.vertexUV((float)var4, 0.0F, 0.0F, (float)var4 / var10, 0.0F);
         var6.vertexUV(0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
         var6.end();  
      } else {
    	 drawFadingBox(0, 0, this.width, this.height, 1610941696, -1607454624);
      }

      List<Button> list = this.buttons;
      int l;
      int sz;
      if (Mouse.isButtonDown(0)) {
         if (this.initialClickY == -1) {
            if (j >= this.top && j <= this.bottom) {
               k = this.width / 2 - 110;
               sz = this.width / 2 + 110;
               l = (j - this.top + this.amountScrolled - 2) / 36;
               if (i >= k && i <= sz && l >= 0 && l < list.size()) {
                  this.minecraft.textureManager.refreshTextures();
               }

               this.initialClickY = j;
            } else {
               this.initialClickY = -2;
            }
         } else if (this.initialClickY >= 0) {
            this.amountScrolled -= j - this.initialClickY;
            this.initialClickY = j;
         }
      } else {
         if (this.initialClickY >= 0 && this.initialClickY != j) {
         }

         this.initialClickY = -1;
      }

      int scrollAmount = 24;
      sz = (list.size() - 1) / 2 * scrollAmount - 1;
      l = sz - (this.bottom - 6 - (this.top + 6));
      if (l < 0) {
         l /= 2;
      }

      if (this.amountScrolled < 0) {
         this.amountScrolled = 0;
      }

      if (this.amountScrolled > l) {
         this.amountScrolled = l;
      }

      GL11.glDisable(2896);
      GL11.glDisable(2912);
      ShapeRenderer shaperenderer = ShapeRenderer.instance;
      GL11.glBindTexture(3553, this.minecraft.textureManager.load("/cobble.png"));
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      float f1 = 32.0F;
      if (this.minecraft.level == null) {
         shaperenderer.begin();
         shaperenderer.color(2105376);
         shaperenderer.vertexUV((float)this.left, (float)this.bottom, 0.0F, (float)this.left / f1, (float)(this.bottom + this.amountScrolled) / f1);
         shaperenderer.vertexUV((float)this.right, (float)this.bottom, 0.0F, (float)this.right / f1, (float)(this.bottom + this.amountScrolled) / f1);
         shaperenderer.vertexUV((float)this.right, (float)this.top, 0.0F, (float)this.right / f1, (float)(this.top + this.amountScrolled) / f1);
         shaperenderer.vertexUV((float)this.left, (float)this.top, 0.0F, (float)this.left / f1, (float)(this.top + this.amountScrolled) / f1);
         shaperenderer.end();
      }

      for(int k1 = 0; k1 < list.size() - 1; ++k1) {
         int l1 = this.width / 2 - 75 + k1 % 2 * 160;
         int i2 = 40 + 24 * (k1 >> 1) - this.amountScrolled;
         Button var10000 = (Button)this.buttons.get(k1);
         Minecraft var7 = this.minecraft;
         var10000.y = i2;
         if (var10000.y <= this.bottom && var10000.y + 20 >= this.top) {
            var10000.active = true;
         } else {
            var10000.active = false;
         }
      }


      super.render(i, j);
      byte byte0 = 4;
      this.renderHoleBackground(0, this.top, 255, 255);
      this.renderHoleBackground(this.bottom, this.height, 255, 255);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glDisable(3008);
      GL11.glShadeModel(7425);
      GL11.glDisable(3553);
      GL11.glBegin(7);
      GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.0F);
      GL11.glVertex2f((float)this.left, (float)(this.top + byte0));
      GL11.glVertex2f((float)this.right, (float)(this.top + byte0));
      GL11.glColor4f(0.0F, 0.0F, 0.0F, 1.0F);
      GL11.glVertex2f((float)this.right, (float)this.top);
      GL11.glVertex2f((float)this.left, (float)this.top);
      GL11.glEnd();
      GL11.glBegin(7);
      GL11.glColor4f(0.0F, 0.0F, 0.0F, 1.0F);
      GL11.glVertex2f((float)this.left, (float)this.bottom);
      GL11.glVertex2f((float)this.right, (float)this.bottom);
      GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.0F);
      GL11.glVertex2f((float)this.right, (float)(this.bottom - byte0));
      GL11.glVertex2f((float)this.left, (float)(this.bottom - byte0));
      GL11.glEnd();
      GL11.glEnable(3553);
      GL11.glShadeModel(7424);
      GL11.glEnable(3008);
      GL11.glDisable(3042);
      drawCenteredString(this.fontRenderer, "Controls", this.width / 2, 16, 16777215);
      Button var10000 = (Button)this.buttons.get(18);
      Minecraft var7 = this.minecraft;
      if (var10000.visible) {
         FontRenderer var8 = var7.fontRenderer;
         GL11.glBindTexture(3553, var7.textureManager.load("/gui/gui.png"));

         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         byte var9 = 1;
         boolean var6 = i >= var10000.x && j >= var10000.y && i < var10000.x + var10000.width && j < var10000.y + var10000.height;
         if (var10000.active && !var10000.slider) {
            if (var6) {
               var9 = 2;
            }
         } else {
            var9 = 0;
         }
            var10000.drawImage(var10000.x, var10000.y, 0, 46 + var9 * 20, var10000.width / 2, var10000.height);
            var10000.drawImage(var10000.x + var10000.width / 2, var10000.y, 200 - var10000.width / 2, 46 + var9 * 20, var10000.width / 2, var10000.height);

         if (!var10000.active) {
            Button.drawCenteredString(var8, var10000.text, var10000.x + var10000.width / 2, var10000.y + (var10000.height - 8) / 2, -6250336);
         } else if (var6) {
            Button.drawCenteredString(var8, var10000.text, var10000.x + var10000.width / 2, var10000.y + (var10000.height - 8) / 2, 16777120);
         } else {
            Button.drawCenteredString(var8, var10000.text, var10000.x + var10000.width / 2, var10000.y + (var10000.height - 8) / 2, 14737632);
         }
      }

   }

   public void renderHoleBackground(int i, int j, int k, int l) {
      ShapeRenderer shaperenderer = ShapeRenderer.instance;
      GL11.glBindTexture(3553, this.minecraft.textureManager.load("/cobble.png"));
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      float f = 32.0F;
      shaperenderer.begin();
      shaperenderer.color(4210752);

      GL11.glColor4f(0.25F, 0.25F, 0.25F, (float)l / 255.0F);
      shaperenderer.vertexUV(0.0F, (float)j, 0.0F, 0.0F, (float)j / f);
      shaperenderer.vertexUV((float)this.width, (float)j, 0.0F, (float)this.width / f, (float)j / f);
      GL11.glColor4f(0.25F, 0.25F, 0.25F, (float)k / 255.0F);
      shaperenderer.vertexUV((float)this.width, (float)i, 0.0F, (float)this.width / f, (float)i / f);
      shaperenderer.vertexUV(0.0F, (float)i, 0.0F, 0.0F, (float)i / f);
      shaperenderer.end();
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
   }
}
