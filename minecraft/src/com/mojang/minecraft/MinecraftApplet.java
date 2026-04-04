package com.mojang.minecraft;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Canvas;

public class MinecraftApplet extends Applet {
   private static final long serialVersionUID = 1L;
   private Canvas canvas;
   private Minecraft minecraft;
   private Thread thread = null;

   public void init() {
      this.canvas = new MinecraftApplet$1(this);
      boolean var1 = false;
      if (this.getParameter("fullscreen") != null) {
         var1 = this.getParameter("fullscreen").equalsIgnoreCase("true");
      }

      this.minecraft = new Minecraft(this.canvas, this, this.getWidth(), this.getHeight(), var1);
      this.minecraft.host = this.getDocumentBase().getHost();
      if (this.getDocumentBase().getPort() > 0) {
         this.minecraft.host = this.minecraft.host + ":" + this.getDocumentBase().getPort();
      }

      if (this.getParameter("username") != null && this.getParameter("sessionid") != null) {
         this.minecraft.session = new SessionData(this.getParameter("username"), this.getParameter("sessionid"));
         if (this.getParameter("mppass") != null) {
            this.minecraft.session.mppass = this.getParameter("mppass");
         }

         this.minecraft.session.haspaid = this.getParameter("haspaid").equalsIgnoreCase("true");
      }

      if (this.getParameter("loadmap_user") != null && this.getParameter("loadmap_id") != null) {
         this.minecraft.levelName = this.getParameter("loadmap_user");
         this.minecraft.levelId = Integer.parseInt(this.getParameter("loadmap_id"));
      } else if (this.getParameter("server") != null && this.getParameter("port") != null) {
         String var2 = this.getParameter("server");
         int var3 = Integer.parseInt(this.getParameter("port"));
         this.minecraft.server = var2;
         this.minecraft.port = var3;
      }

      this.minecraft.levelLoaded = true;
      this.setLayout(new BorderLayout());
      this.add(this.canvas, "Center");
      this.canvas.setFocusable(true);
      this.validate();
   }

   public void startGameThread() {
      if (this.thread == null) {
         this.thread = new Thread(this.minecraft);
         this.thread.start();
      }

   }

   public void start() {
      this.minecraft.waiting = false;
   }

   public void stop() {
      this.minecraft.waiting = true;
   }

   public void destroy() {
      this.stopGameThread();
   }

   public void stopGameThread() {
      if (this.thread != null) {
         this.minecraft.running = false;

         try {
            this.thread.join(1000L);
         } catch (InterruptedException var2) {
            this.minecraft.shutdown();
         }

         this.thread = null;
      }

   }
}
