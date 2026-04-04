package com.mojang.minecraft.gui;

import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.net.NetworkManager;
import com.mojang.minecraft.render.ShapeRenderer;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public final class MultiPlayerScreen extends GuiScreen {
   private GuiScreen parent;
   private int updateCounter = 0;
   private String server = "";

   public MultiPlayerScreen(GuiScreen var1) {
      this.parent = var1;
   }

   public void tick() {
      ++this.updateCounter;
   }

   public final void onOpen() {
      this.buttons.clear();
      Keyboard.enableRepeatEvents(true);
      this.buttons.add(new Button(0, this.width / 2 - 100, this.height / 4 + 96 + 12, "Connect"));
      this.buttons.add(new Button(1, this.width / 2 - 100, this.height / 4 + 120 + 12, "Cancel"));
      this.server = this.minecraft.settings.lastServer.replaceAll("_", ":");
      ((Button)this.buttons.get(0)).active = this.server.length() > 0;
   }

   public final void onClose() {
      Keyboard.enableRepeatEvents(false);
   }

   protected final void onButtonClick(Button var1) {

          if (var1.id == 1) {
             this.minecraft.setCurrentScreen(this.parent);
          }
          
          try {
            this.minecraft.setLevel((Level)null);
              if (var1.id == 0) {
                 if (!this.server.contains(":")) {
                    this.server = this.server + ":25565";
                 }
                 
                 this.minecraft.settings.lastServer = this.server.replaceAll(":", "_");
                 this.minecraft.settings.save();

                 String[] split = this.server.split(":");
                 String ip = split[0];
                 int port = 25565;

                 try {
                    port = Integer.parseInt(split[1]);
                 } catch (NumberFormatException var7) {
                    var7.printStackTrace();
                 }

                 try {
                     this.minecraft.setCurrentScreen(new ConnectScreen(this.parent));
                     this.minecraft.hud.chat.clear();
                     this.minecraft.server = ip + port;
                     this.minecraft.networkManager = new NetworkManager(this.minecraft, ip, port, this.minecraft.session.username, this.minecraft.applet.getParameter("sessionid"));
                 } catch (NullPointerException var6) {
                     this.minecraft.setCurrentScreen(new ConnectScreen(this.parent));
                     this.minecraft.hud.chat.clear();
                     this.minecraft.server = ip + port;
                     this.minecraft.networkManager = new NetworkManager(this.minecraft, ip, port, this.minecraft.session.username, this.minecraft.session.sessionId);
                 }

                 this.minecraft.setCurrentScreen((GuiScreen)null);
                 this.minecraft.grabMouse();

              }

           } catch (ArrayIndexOutOfBoundsException var8) {
              this.minecraft.setCurrentScreen(new ErrorScreen("Not a valid server!", this.server + " is not a valid server ip or port!"));
           } catch (NumberFormatException var9) {
              this.minecraft.setCurrentScreen(new ErrorScreen("Not a valid server!", this.server + " is not a valid server ip or port!"));
           }

   }

   protected final void onKeyPress(char var1, int var2) {
	  if (var2 == 1) {
		 this.minecraft.setCurrentScreen(this.parent);
	  }
	      
      if (var1 == 22) {
         String var3 = "" + GuiScreen.getClipboardString();
         int var4 = 32 - this.server.length();
         if (var4 > var3.length()) {
            var4 = var3.length();
         }

         if (var4 > 0) {
            this.server = this.server + var3.substring(0, var4);
         }
      }

      if (var2 == 14 && this.server.length() > 0) {
         this.server = this.server.substring(0, this.server.length() - 1);
      }

      if ("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 ,.:-_\\'*!\\\\\\\"#%/()=+?[]{}<>@|$;".indexOf(var1) >= 0 && this.server.length() < 32) {
         this.server = this.server + var1;
      }

      ((Button)this.buttons.get(0)).active = this.server.length() > 0;
   }

   public final void render(int var1, int var2) {
      this.drawBackground();
      drawCenteredString(this.fontRenderer, "Play Multiplayer", this.width / 2, this.height / 4 - 10, 16777215);
      drawString(this.fontRenderer, "Enter the IP or Host of a server to connect to it:", this.width / 2 - 125, this.height / 4 - 60 + 60 + 36, 10526880);
      int var8 = this.width / 2 - 100;
      int var9 = this.height / 4 - 10 + 50 + 18;
      short var10 = 200;
      byte var11 = 20;
      drawBox(var8 - 1, var9 - 1, var8 + var10 + 1, var9 + var11 + 1, -6250336);
      drawBox(var8, var9, var8 + var10, var9 + var11, -16777216);
      drawString(this.fontRenderer, this.server + (this.updateCounter / 6 % 2 == 0 ? "_" : ""), var8 + 4, var9 + (var11 - 8) / 2, 14737632);
      super.render(var1, var2);
   }
   
   public boolean doesGuiPause() {
 	  return false;
   }
}
